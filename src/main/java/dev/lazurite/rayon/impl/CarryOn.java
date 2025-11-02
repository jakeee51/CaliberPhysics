package dev.lazurite.rayon.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.lazurite.rayon.impl.bullet.collision.space.generator.PressureGenerator;
import dev.lazurite.rayon.impl.bullet.collision.space.generator.TerrainGenerator;
import dev.lazurite.rayon.impl.bullet.natives.NativeLoader;
import dev.lazurite.rayon.impl.event.ClientEventHandler;
import dev.lazurite.rayon.impl.event.ServerEventHandler;
import dev.lazurite.rayon.example.client.init.RayonExampleEntityRenderers;
import dev.lazurite.rayon.example.init.RayonExampleEntities;
import dev.lazurite.rayon.impl.packet.RayonPacketHandlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(CarryOn.MOD_ID)
public class CarryOn {
	public static final String MOD_ID = "carryon";
	public static final Logger LOGGER = LogManager.getLogger("CarryOn");

	public CarryOn(FMLJavaModLoadingContext context) {
		NativeLoader.load();

        IEventBus modBus = context.getModEventBus();
		modBus.addListener(this::clientInit);
		modBus.addListener(this::commonInit);
		modBus.addListener(RayonExampleEntityRenderers::registerEntityRenderers);

		RayonExampleEntities.register(modBus);

		// prevent annoying libbulletjme spam
		java.util.logging.LogManager.getLogManager().reset();
	}

	private void clientInit(FMLClientSetupEvent event) {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.register(ClientEventHandler.class);
		RayonPacketHandlers.registerPackets();
	}

	private void commonInit(FMLCommonSetupEvent event) {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.register(ServerEventHandler.class);
		forgeBus.register(PressureGenerator.class);
		forgeBus.register(TerrainGenerator.class);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
package zeh.resourcefullveins;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import zeh.resourcefullveins.common.CommonSetup;
//import zeh.resourcefullveins.client.ClientSetup;
import zeh.resourcefullveins.common.registry.*;
import zeh.resourcefullveins.common.Configuration;

import org.slf4j.Logger;

@Mod(ResourcefullVeins.MODID)
public class ResourcefullVeins {

    public static final String MODID = "resourcefullveins";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ResourcefullVeins() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CommonSetup::init);
        //modEventBus.addListener(ClientSetup::init);

        Configuration.setup();
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
		//ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);

        //ModItems.ITEMS.register(modEventBus);
		//ModBlocks.BLOCKS.register(modEventBus);
		ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        ModBiomeFeatures.FEATURES.register(modEventBus);
		//ModBlockEntityTypes.TILES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

}

package zeh.peaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.common.MinecraftForge;

import zeh.peaks.common.registry.*;
import zeh.peaks.common.Configuration;

import org.slf4j.Logger;

@Mod(Peaks.MODID)
public class Peaks {

    public static final String MODID = "peaks";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Peaks() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        ModBiomeFeatures.FEATURES.register(modEventBus);
		ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}

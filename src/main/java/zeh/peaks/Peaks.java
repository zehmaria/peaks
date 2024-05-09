package zeh.peaks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import zeh.peaks.common.Configuration;
import zeh.peaks.common.registry.*;

import org.slf4j.Logger;

@Mod(Peaks.MODID)
public class Peaks {

    public static final String MODID = "peaks";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public Peaks(ModContainer modContainer, IEventBus modEventBus) {

        //modContainer.registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        ModBiomeFeatures.FEATURES.register(modEventBus);
        ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}

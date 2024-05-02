package zeh.resourcefullveins;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.common.MinecraftForge;

import zeh.resourcefullveins.common.registry.*;
import zeh.resourcefullveins.common.Configuration;

import org.slf4j.Logger;

@Mod(ResourcefullVeins.MODID)
public class ResourcefullVeins {

    public static final String MODID = "resourcefullveins";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public ResourcefullVeins() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Configuration.setup();

        ModBiomeFeatures.FEATURES.register(modEventBus);
		ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}

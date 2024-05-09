package zeh.peaks.common.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.configuration.VeinConfiguration;
import zeh.peaks.common.world.feature.VeinFeature;

public class ModBiomeFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, Peaks.MODID);

    public static final RegistryObject<Feature<VeinConfiguration>> VEINS =
            FEATURES.register("gen_vein", VeinFeature::new);

}
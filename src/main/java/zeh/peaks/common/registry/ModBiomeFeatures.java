package zeh.peaks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredRegister;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.configuration.VeinConfiguration;
import zeh.peaks.common.world.feature.VeinFeature;
import java.util.function.Supplier;

public class ModBiomeFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Peaks.MODID);

    public static final Supplier<Feature<VeinConfiguration>> VEINS = FEATURES.register("gen_vein", VeinFeature::new);

}
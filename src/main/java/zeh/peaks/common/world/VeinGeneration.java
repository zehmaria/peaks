package zeh.peaks.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import zeh.peaks.Peaks;

@SuppressWarnings("unused")
public class VeinGeneration {
    // Those are unused, but kept for reference just in case
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_VEINS =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Peaks.MODID, "veins"));

    public static ResourceKey<PlacedFeature> VEINS =
            ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Peaks.MODID, "veins"));

    public static void load() {
    }
}
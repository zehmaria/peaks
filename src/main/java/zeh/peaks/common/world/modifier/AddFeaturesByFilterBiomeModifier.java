package zeh.peaks.common.world.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.jetbrains.annotations.NotNull;
import zeh.peaks.common.registry.ModBiomeModifiers;
import java.util.Optional;

public record AddFeaturesByFilterBiomeModifier(
        HolderSet<Biome> allowedBiomes,
        Optional<HolderSet<Biome>> deniedBiomes,
        Optional<Float> minimumTemperature,
        Optional<Float> maximumTemperature,
        HolderSet<PlacedFeature> features,
        GenerationStep.Decoration step) implements BiomeModifier {

        public static final Codec<AddFeaturesByFilterBiomeModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(AddFeaturesByFilterBiomeModifier::allowedBiomes),
                Biome.LIST_CODEC.optionalFieldOf("denied_biomes").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::deniedBiomes),
                Codec.FLOAT.optionalFieldOf("min_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::minimumTemperature),
                Codec.FLOAT.optionalFieldOf("max_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::maximumTemperature),
                PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesByFilterBiomeModifier::features),
                GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesByFilterBiomeModifier::step)
        ).apply(builder, AddFeaturesByFilterBiomeModifier::new));

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
        if (phase == Phase.ADD && this.allowedBiomes.contains(biome)) {
            if (deniedBiomes.isPresent() && this.deniedBiomes.get().contains(biome)) {
                return;
            }
            if (minimumTemperature.isPresent() && biome.value().getBaseTemperature() < minimumTemperature.get()) {
                return;
            }
            if (maximumTemperature.isPresent() && biome.value().getBaseTemperature() > maximumTemperature.get()) {
                return;
            }
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
        }
    }

    @Override
    public @NotNull Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.ADD_FEATURES_BY_FILTER.get();
    }

}
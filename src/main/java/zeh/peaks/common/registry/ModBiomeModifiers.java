package zeh.peaks.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.modifier.AddFeaturesByFilterBiomeModifier;

import java.util.Optional;
import java.util.function.Supplier;

public class ModBiomeModifiers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Peaks.MODID);

    public static Supplier<Codec<AddFeaturesByFilterBiomeModifier>> ADD_FEATURES_BY_FILTER =
            BIOME_MODIFIER_SERIALIZERS.register("add_features_by_filter",
                    () -> RecordCodecBuilder.create(builder -> builder.group(
                            Biome.LIST_CODEC.fieldOf("allowed_biomes")
                                    .forGetter(AddFeaturesByFilterBiomeModifier::allowedBiomes),
                            Biome.LIST_CODEC.optionalFieldOf("denied_biomes")
                                    .orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::deniedBiomes),
                            Codec.FLOAT.optionalFieldOf("min_temperature")
                                    .orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::minimumTemperature),
                            Codec.FLOAT.optionalFieldOf("max_temperature")
                                    .orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::maximumTemperature),
                            PlacedFeature.LIST_CODEC.fieldOf("features")
                                    .forGetter(AddFeaturesByFilterBiomeModifier::features),
                            GenerationStep.Decoration.CODEC.fieldOf("step")
                                    .forGetter(AddFeaturesByFilterBiomeModifier::step)
                    ).apply(builder, AddFeaturesByFilterBiomeModifier::new)));

}
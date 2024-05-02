package zeh.resourcefullveins.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import zeh.resourcefullveins.ResourcefullVeins;
import zeh.resourcefullveins.common.Configuration;
import zeh.resourcefullveins.common.world.configuration.VeinConfiguration;
import zeh.resourcefullveins.common.registry.ModBiomeFeatures;

import java.util.List;

public class VeinGeneration {
    public static Holder<ConfiguredFeature<VeinConfiguration, ?>> CONFIGURED_VEINS;

    public static Holder<PlacedFeature> PLACED_VEINS;

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);

    public static void registerVeinGeneration() {
        CONFIGURED_VEINS = register(
                new ResourceLocation(ResourcefullVeins.MODID, "veins"),
                ModBiomeFeatures.VEINS.get(),
                veinConfig(BlockPredicate.matchesBlocks(BLOCK_BELOW,
                        List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.RED_SAND, Blocks.SAND)))
        );

        PLACED_VEINS = registerPlacement(
                new ResourceLocation(ResourcefullVeins.MODID, "veins"),
                CONFIGURED_VEINS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_ORE_VEIN.get()),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
    }

    public static VeinConfiguration veinConfig(BlockPredicate plantedOn) {
        return new VeinConfiguration(1, 6, 3, plantBlockConfig(Blocks.AIR, plantedOn), null);
    }

    public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
        return PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)
        );
    }

    // Registry stuff

    static Holder<PlacedFeature> registerPlacement(ResourceLocation id,
                                                   Holder<? extends ConfiguredFeature<?, ?>> feature,
                                                   PlacementModifier... modifiers) {
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
    }

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>>
    Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id,
                                              F feature, FC featureConfig) {
        return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
    }

    private static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
        return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
    }

}
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
import net.minecraft.tags.BiomeTags;

import zeh.resourcefullveins.ResourcefullVeins;
import zeh.resourcefullveins.common.Configuration;
import zeh.resourcefullveins.common.world.configuration.VeinConfiguration;
import zeh.resourcefullveins.common.registry.ModBiomeFeatures;
import zeh.resourcefullveins.common.world.filter.BiomeTagFilter;

import java.util.List;

public class VeinGeneration {
    public static Holder<ConfiguredFeature<VeinConfiguration, ?>> FEATURE_IRON_VEIN;

    public static Holder<PlacedFeature> IRON_VEIN;

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);
	public static final BlockPos BLOCK_ABOVE = new BlockPos(0, 1, 0);

    public static final BiomeTagFilter TAGGED_IS_OVERWORLD = BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD);

    public static void registerVeinGeneration() {
        FEATURE_IRON_VEIN = register(
            new ResourceLocation(ResourcefullVeins.MODID, "ore_vein"),
            ModBiomeFeatures.VEIN.get(),
            veinConfig(
                Blocks.IRON_BLOCK,
                Blocks.IRON_ORE,
                BlockPredicate.matchesBlocks(BLOCK_BELOW, List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.RED_SAND, Blocks.SAND))
            )
        );

        IRON_VEIN = registerPlacement(
            new ResourceLocation(ResourcefullVeins.MODID, "ore_vein"),
			FEATURE_IRON_VEIN, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_ORE_VEIN.get()),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome(),
            TAGGED_IS_OVERWORLD
        );
    }

    public static VeinConfiguration veinConfig(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
		return new VeinConfiguration(
            64, 6, 3,
            plantBlockConfig(primaryBlock, plantedOn),
            plantBlockConfig(secondaryBlock, plantedOn),
            null
        );
	}

    public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
        return PlacementUtils.filtered(
            Feature.SIMPLE_BLOCK,
            new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
            BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)
        );
    }

    // Registry stuff

    static Holder<PlacedFeature> registerPlacement(
        ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers
    ) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
	}

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(
        ResourceLocation id, F feature, FC featureConfig
    ) {
		return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
	}

    private static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
		return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
	}

}

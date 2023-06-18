package zeh.resourcefullveins.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;
import zeh.resourcefullveins.common.Configuration;
import zeh.resourcefullveins.common.VeinConfig;
import zeh.resourcefullveins.common.world.configuration.VeinConfiguration;

import java.util.Objects;

public class VeinFeature extends Feature<VeinConfiguration> {

    public VeinFeature(Codec<VeinConfiguration> codec) {
		super(codec);
	}

    final private int minRadius = Configuration.VEIN_MIN_RADIUS.get();
    final private int deltaRadius = Configuration.VEIN_RADIUS_DELTA.get();

    @Override
    public boolean place(FeaturePlaceContext<VeinConfiguration> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        RandomSource rand = featurePlaceContext.random();
        BlockPos at = featurePlaceContext.origin();
        Holder<Biome> wb = world.getBiome(at);

        int sumChance = 0;
        for (VeinConfig cfg : Configuration.veinsList) {
            if (wb.is(TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(cfg.biometag)))) sumChance += cfg.chance;
        }
        if (sumChance == 0) return false;

        int cc = rand.nextInt(sumChance), nn = 0;
        while (cc >= 0) {
            VeinConfig cfg = Configuration.veinsList.get(nn);
            if (wb.is(TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(cfg.biometag)))) cc -= cfg.chance;
            if (cc >= 0) nn++;
        }
        VeinConfig vein = Configuration.veinsList.get(nn);

        String blockCommon = vein.resource;
        String blockDeepslate = vein.deepslate;

        int er = vein.extraRadius;
        int edr = vein.extraDeltaRadius;
        int _radius = minRadius + er + rand.nextInt(deltaRadius + edr);

        if (!world.getBiome(featurePlaceContext.origin()).is(TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(vein.biometag)))) return false;

        for (int i = -_radius; i <= _radius; i++) {
            for (int k = -_radius; k <= _radius; k++) {
                int _r = rand.nextInt(deltaRadius + edr);
                if (i * i + k * k <= (minRadius + _r) * (minRadius + _r)) {
                    BlockPos ii = featurePlaceContext.origin().offset(0, 10, 0);
                    boolean bb = true;
                    while (ii.getY() > -63) {
                        BlockPos _ii = ii.offset(i, 0, k);
                        if (world.getBlockState(_ii).is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) &&
                            !(world.getBlockState(_ii).getMaterial() == Material.WATER)) {
                                if (world.getBlockState(_ii).is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                                    this.setBlock(world, _ii, Objects.requireNonNull(ForgeRegistries.BLOCKS
                                            .getValue(new ResourceLocation(blockDeepslate))).defaultBlockState());
                                } else {
                                    this.setBlock(world, _ii, Objects.requireNonNull(ForgeRegistries.BLOCKS
                                            .getValue(new ResourceLocation(blockCommon))).defaultBlockState());
                                }
                                if (bb) {
                                    for (int m = 0; m <= Math.PI * ((minRadius+_r)*(minRadius+_r) - (i*i+k*k)) / (minRadius+_r); m++) {
                                        this.setBlock(world, _ii.offset(0, m, 0), Objects.requireNonNull(ForgeRegistries.BLOCKS
                                                .getValue(new ResourceLocation(blockCommon))).defaultBlockState());
                                    }
                                    bb = false;
                                }
                            }
                        ii = ii.below();
                    }
                }
            }
        }

        return true;
    }

}

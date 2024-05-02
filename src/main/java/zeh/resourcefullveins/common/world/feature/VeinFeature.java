package zeh.resourcefullveins.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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

    public TagKey<Biome> getKey (String name) {
        return TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(name.substring(1)));
    }

    public BlockState getBlock(String block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block))).defaultBlockState();
    }

    @Override
    public boolean place(FeaturePlaceContext<VeinConfiguration> context) {
        WorldGenLevel _level = context.level();
        RandomSource _random = context.random();
        Holder<Biome> _biome = _level.getBiome(context.origin());

        int _totalChance = 0;
        for (VeinConfig cfg : Configuration.veinsList) {
            if (cfg.biome.startsWith("#")) {
                if (_biome.is(getKey(cfg.biome))) _totalChance += cfg.weight;
            } else {
                if (_biome.is(new ResourceLocation(cfg.biome))) _totalChance += cfg.weight;
            }
        }
        if (_totalChance == 0) return false;

        int picker = _random.nextInt(_totalChance), _picked = 0;
        while (picker >= 0) {
            VeinConfig cfg = Configuration.veinsList.get(_picked);
            if (cfg.biome.startsWith("#")) {
                if (_biome.is(getKey(cfg.biome))) picker -= cfg.weight;
            } else {
                if (_biome.is(new ResourceLocation(cfg.biome))) picker -= cfg.weight;
            }
            if (picker >= 0) _picked++;
        }
        VeinConfig _vein = Configuration.veinsList.get(_picked);

        if (_vein.failure > _random.nextInt(100)) return false;

        int _radius = minRadius + _vein.extraRadius + _random.nextInt(deltaRadius + _vein.extraDeltaRadius);

        for (int i = -_radius; i <= _radius; i++) {
            for (int j = -_radius; j <= _radius; j++) {
                int _dr = minRadius + _random.nextInt(deltaRadius + _vein.extraDeltaRadius);
                if (i * i + j * j <= _dr * _dr) {
                    BlockPos _top = context.origin().offset(0, 10, 0);
                    boolean _extraBlocks = true;
                    while (_top.getY() > -63) {
                        BlockPos _offset = _top.offset(i, 0, j);
                        BlockState _blockState = _level.getBlockState(_offset);
                        if (_blockState.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) && !_blockState.is(Blocks.WATER)) {
                                if (_blockState.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                                    setBlock(_level, _offset, getBlock(_vein.deepslate));
                                } else {
                                    setBlock(_level, _offset, getBlock(_vein.stone));
                                }
                                if (_extraBlocks) {
                                    for (int m = 0;
                                         m <= Math.PI * (_dr * _dr - (i * i + j * j)) / _dr;
                                         m++) {
                                        setBlock(_level, _offset.offset(0, m, 0), getBlock(_vein.stone));
                                    }
                                    _extraBlocks = false;
                                }
                            }
                        _top = _top.below();
                    }
                }
            }
        }

        return true;
    }

}

package zeh.resourcefullveins.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
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
import zeh.resourcefullveins.common.world.configuration.VeinConfiguration;

public class VeinFeature extends Feature<VeinConfiguration> {

    public VeinFeature(Codec<VeinConfiguration> codec) {
		super(codec);
	}

    final private int radius = Configuration.VEIN_RADIUS.get();
    final private int deltaRadius = Configuration.VEIN_DELTA_RADIUS.get();

    public TagKey<Biome> getKey (String name) {
        return TagKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(name.substring(1)));
    }

    @Override
    public boolean place(FeaturePlaceContext<VeinConfiguration> context) {
        VeinConfiguration _vein = context.config();
        WorldGenLevel _level = context.level();
        RandomSource _random = context.random();
        BlockPos _pos = context.origin();
        Holder<Biome> _biome = _level.getBiome(context.origin());

        BlockState _replaceStone = _vein.replaceStone().getState(_random, _pos);
        BlockState _replaceDeepslate = _vein.replaceDeepslate().getState(_random, _pos);

        int _maxRadius = radius + _vein.extraRadius() + deltaRadius + _vein.extraDeltaRadius();

        for (int i = -_maxRadius; i <= _maxRadius; i++) {
            for (int j = -_maxRadius; j <= _maxRadius; j++) {
                int _dr = _maxRadius - _random.nextInt(deltaRadius + _vein.extraDeltaRadius());
                if (i * i + j * j < _dr * _dr) {
                    BlockPos _top = context.origin().offset(0, 10, 0);
                    boolean _extraBlocks = true;
                    while (_top.getY() > -63) {
                        BlockPos _offset = _top.offset(i, 0, j);
                        BlockState _blockState = _level.getBlockState(_offset);
                        if (_blockState.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) && !_blockState.is(Blocks.WATER)) {
                                if (_blockState.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                                    _level.setBlock(_offset, _replaceDeepslate, Block.UPDATE_CLIENTS);
                                } else {
                                    _level.setBlock(_offset, _replaceStone, Block.UPDATE_CLIENTS);
                                }

                                if (_extraBlocks) {
                                    for (int m = 0;
                                         m <= Math.PI * (_dr * _dr - (i * i + j * j)) / _dr;
                                         m++) {
                                        _level.setBlock(_offset.offset(0, m, 0), _replaceStone, Block.UPDATE_CLIENTS);
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

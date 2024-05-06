package zeh.peaks.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import zeh.peaks.Peaks;
import zeh.peaks.common.Configuration;
import zeh.peaks.common.world.configuration.VeinConfiguration;

public class VeinFeature extends Feature<VeinConfiguration> {

    public VeinFeature(Codec<VeinConfiguration> codec) {
		super(codec);
	}

    final private int radius = Configuration.VEIN_RADIUS.get();
    final private int deltaRadius = Configuration.VEIN_DELTA_RADIUS.get();

    @Override
    public boolean place(FeaturePlaceContext<VeinConfiguration> context) {
        VeinConfiguration _vein = context.config();
        WorldGenLevel _level = context.level();
        RandomSource _random = context.random();
        BlockPos _pos = context.origin();

        BlockState _replaceBottom = _vein.replaceBottom().getState(_random, _pos);
        BlockState _replaceCore = _vein.replaceCore().getState(_random, _pos);
        BlockState _replaceStone = _vein.replaceStone().getState(_random, _pos);
        BlockState _stoneWrapper = _vein.replaceStoneWrapper().getState(_random, _pos);
        BlockState _replaceDeepslate = _vein.replaceDeepslate().getState(_random, _pos);
        BlockState _deepslateWrapper = _vein.replaceDeepslateWrapper().getState(_random, _pos);

        int count = 0;
        int _maxRadius = radius + _vein.extraRadius() + _random.nextInt(deltaRadius + _vein.extraDeltaRadius());
        BlockPos.MutableBlockPos _anchor = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos _offset = new BlockPos.MutableBlockPos();
        BulkSectionAccess _bulk = new BulkSectionAccess(_level);

        try {
            for (int i = -_maxRadius; i <= _maxRadius; i++) {
                for (int j = -_maxRadius; j <= _maxRadius; j++) {
                    int _dr = _maxRadius - _random.nextInt(deltaRadius + _vein.extraDeltaRadius());

                    if (i * i + j * j <= _dr * _dr) {
                        _anchor.set(_pos.offset(0, _maxRadius + 1, 0));
                        boolean _extraBlocks = true;
                        boolean _l1 = i * i + j * j < _dr * _dr / 13;
                        boolean _l2 = i * i + j * j < _dr * _dr / 2;
                        int _peak = (int) (Math.PI * (_dr * _dr - (i * i + j * j)) / _dr);

                        while (_anchor.getY() > -63) {
                            _anchor.set(_anchor.below());

                            _offset.setWithOffset(_anchor, i, 0, j);
                            if (!_level.ensureCanWrite(_offset)) continue;
                            LevelChunkSection _section = _bulk.getSection(_offset);
                            if (_section == null) continue;
                            int _x = SectionPos.sectionRelative(_offset.getX());
                            int _y = SectionPos.sectionRelative(_offset.getY());
                            int _z = SectionPos.sectionRelative(_offset.getZ());

                            if (_section.getBlockState(_x, _y, _z).is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
                                if (_l1) {
                                    _section.setBlockState(_x, _y, _z, _replaceCore, false);
                                } else if (_section.getBlockState(_x, _y, _z).is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
                                    _section.setBlockState(_x, _y, _z, _l2 ?
                                            _replaceDeepslate : _deepslateWrapper, false);
                                } else {
                                    _section.setBlockState(_x, _y, _z, _l2 ?
                                            _replaceStone : _stoneWrapper, false);
                                }
                                count++;

                                if (_extraBlocks) {
                                    for (int m = 1; m <= _peak; m++) {
                                        _offset.setWithOffset(_anchor, i, m, j);
                                        if (!_level.ensureCanWrite(_offset)) continue;
                                        LevelChunkSection _top = _bulk.getSection(_offset);
                                        if (_top == null) continue;
                                        int _xx = SectionPos.sectionRelative(_offset.getX());
                                        int _yy = SectionPos.sectionRelative(_offset.getY());
                                        int _zz = SectionPos.sectionRelative(_offset.getZ());

                                        _top.setBlockState(_xx, _yy, _zz, _l1 ? _replaceCore :
                                                (_l2 ? _replaceStone : _stoneWrapper), false);
                                        count++;
                                    }
                                    _extraBlocks = false;
                                }
                            }
                        }

                        for (int m = 1; m <= _peak / 2; m++) {
                            _offset.setWithOffset(_anchor, i, m, j);
                            if (!_level.ensureCanWrite(_offset)) continue;
                            LevelChunkSection _bottom = _bulk.getSection(_offset);
                            if (_bottom == null) continue;
                            int _xx = SectionPos.sectionRelative(_offset.getX());
                            int _yy = SectionPos.sectionRelative(_offset.getY());
                            int _zz = SectionPos.sectionRelative(_offset.getZ());

                            _bottom.setBlockState(_xx, _yy, _zz, _l2 ? _replaceBottom : _deepslateWrapper, false);
                            count++;
                        }
                    }
                }
            }
        } catch (Throwable throwable1) {
            try {
                _bulk.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }
            throw throwable1;
        }

        _bulk.close();
        return count > 0;
    }

}

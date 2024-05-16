package zeh.peaks.common.world.feature;

import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import zeh.peaks.common.world.configuration.CylindricalSurface;
import zeh.peaks.common.world.configuration.ExpandedTargetBlockState;
import zeh.peaks.common.world.configuration.VeinConfiguration;
import zeh.peaks.common.world.providers.CompatAbstractSelector;

public class VeinFeature extends Feature<VeinConfiguration> {

    public VeinFeature() {
		super(VeinConfiguration.CODEC);
	}

    @Override
    public boolean place(@NotNull FeaturePlaceContext<VeinConfiguration> context) {
        VeinConfiguration _vein = context.config();
        WorldGenLevel _level = context.level();
        RandomSource _random = context.random();
        BlockPos _pos = context.origin();

        if (!_vein.checkValidCompat()) return false;

        int count = 0;
        int _maxRadius = _vein.radius() + (_vein.deltaRadius() > 0 ? _random.nextInt(_vein.deltaRadius()) : 0);
        BlockPos.MutableBlockPos _anchor = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos _offset = new BlockPos.MutableBlockPos();

        int _h = _pos.getY() + _maxRadius + 62;
        int[][] _vert = {
                {(int) (_h * 0.20 - 62 + _random.nextInt(10) - 5),
                        _random.nextInt(4) - 2, _random.nextInt(4) - 2},
                {(int) (_h * 0.40 - 62 + _random.nextInt(10) - 5),
                        _random.nextInt(4) - 2, _random.nextInt(4) - 2},
                {(int) (_h * 0.60 - 62 + _random.nextInt(10) - 5),
                        _random.nextInt(4) - 2, _random.nextInt(4) - 2},
                {(int) (_h * 0.80 - 62 + _random.nextInt(10) - 5),
                        _random.nextInt(4) - 2, _random.nextInt(4) - 2}
        };

        BulkSectionAccess _bulk = new BulkSectionAccess(_level);
        try {
            float _scale = 1.15f;
            for (int i = (int) -_scale * _maxRadius; i <= _scale * _maxRadius ; i++) {
                for (int j = (int) -_scale * _maxRadius; j <= _scale * _maxRadius; j++) {
                    double _dr = _scale * _maxRadius + _random.nextGaussian() * 0.3;
                    int _top = 0;
                    while (_pos.getY() + _top > 0) {
                        _top--;
                        if (i * i + j * j >= _dr * _dr) continue;
                        _offset.set(_pos.offset(i, _top, j));
                        if (FeatureUtils.cannotWrite(_level, _offset)) continue;
                        LevelChunkSection _check = _bulk.getSection(_offset);
                        if (_check == null) continue;
                        int _x = SectionPos.sectionRelative(_offset.getX());
                        int _y = SectionPos.sectionRelative(_offset.getY());
                        int _z = SectionPos.sectionRelative(_offset.getZ());

                        BlockState _state = _check.getBlockState(_x, _y, _z);
                        if (_state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) && _state.getFluidState().isEmpty()) {
                            double kMax = 1 + _random.nextGaussian() * 0.3 + 0.6 * (_dr * _dr - (i * i + j * j)) / _dr;
                            for (int k = 0; k <= kMax; k++) {
                                _offset.set(_pos.offset(i, _top + k, j));
                                if (FeatureUtils.cannotWrite(_level, _offset)) continue;
                                LevelChunkSection _border = _bulk.getSection(_offset);
                                if (_border == null) continue;
                                int _xx = SectionPos.sectionRelative(_offset.getX());
                                int _yy = SectionPos.sectionRelative(_offset.getY());
                                int _zz = SectionPos.sectionRelative(_offset.getZ());

                                _border.setBlockState(_xx, _yy, _zz, _state, false);
                            }
                            _top = -1000;
                        }
                    }
                }
            }
            
            for (int i = -_maxRadius; i <= _maxRadius; i++) {
                for (int j = -_maxRadius; j <= _maxRadius; j++) {
                    float _dr = _maxRadius - (_vein.deltaRadius() > 0 ? _random.nextInt(_vein.deltaRadius()) : 0);

                    if (i * i + j * j <= _dr * _dr) {
                        _anchor.set(_pos.offset(0, _maxRadius + 1, 0));
                        boolean _extraBlocks = true;
                        float _radius = Mth.sqrt((i * i + j * j) / (_dr * _dr));
                        double _peak = (Math.PI * (_dr * _dr - (i * i + j * j)) / _dr);

                        while (_anchor.getY() > -63) {
                            _anchor.set(_anchor.below());
                            for (int[] _v : _vert) {
                                if (_anchor.getY() == _v[0]) {
                                    _anchor.setWithOffset(_anchor, _v[1], 0, _v[2]);
                                }
                            }

                            _offset.setWithOffset(_anchor, i, 0, j);
                            if (FeatureUtils.cannotWrite(_level, _offset)) continue;
                            LevelChunkSection _section = _bulk.getSection(_offset);

                            if (_section == null) continue;
                            int _x = SectionPos.sectionRelative(_offset.getX());
                            int _y = SectionPos.sectionRelative(_offset.getY());
                            int _z = SectionPos.sectionRelative(_offset.getZ());

                            BlockState _state = _section.getBlockState(_x, _y, _z);
                            for (CylindricalSurface surface : _vein.surfaces()) {
                                if (_radius >= surface.minRadius() && _radius <= surface.maxRadius()) {
                                    for (ExpandedTargetBlockState targetState : surface.targets()) {
                                        BlockState _replace = targetState.provider().getState(_random, _offset);
                                        if (canPlaceOre(_state, _random, targetState)) {
                                            _section.setBlockState(_x, _y, _z, _replace, false);
                                            ++count;

                                            if (_extraBlocks && _vein.topMultiplier() > 0) {
                                                for (int m = 0; m <= _peak * _vein.topMultiplier(); m++) {
                                                    _offset.setWithOffset(_anchor, i, m, j);
                                                    if (FeatureUtils.cannotWrite(_level, _offset)) continue;
                                                    LevelChunkSection _top = _bulk.getSection(_offset);
                                                    if (_top == null) continue;
                                                    int _xx = SectionPos.sectionRelative(_offset.getX());
                                                    int _yy = SectionPos.sectionRelative(_offset.getY());
                                                    int _zz = SectionPos.sectionRelative(_offset.getZ());

                                                    _top.setBlockState(_xx, _yy, _zz, _replace, false);
                                                    count++;
                                                }
                                                _extraBlocks = false;
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (_vein.bottomMultiplier() > 0) {
                            for (int m = 1; m <= _peak * _vein.bottomMultiplier(); m++) {
                                _offset.setWithOffset(_anchor, i, m, j);
                                if (FeatureUtils.cannotWrite(_level, _offset)) continue;
                                LevelChunkSection _bottom = _bulk.getSection(_offset);
                                if (_bottom == null) continue;
                                int _xx = SectionPos.sectionRelative(_offset.getX());
                                int _yy = SectionPos.sectionRelative(_offset.getY());
                                int _zz = SectionPos.sectionRelative(_offset.getZ());

                                BlockState _state = _bottom.getBlockState(_xx, _yy, _zz);
                                BlockState _replace = _vein.bottomDeposit().provider().getState(_random, _offset);
                                if (canPlaceOre(_state, _random, _vein.bottomDeposit())) {
                                    _bottom.setBlockState(_xx, _yy, _zz, _replace, false);
                                    count++;
                                }
                            }
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

    private static boolean canPlaceOre(BlockState state, RandomSource random, ExpandedTargetBlockState targetState) {
        if (targetState.target().isEmpty()) return true;

        if (targetState.provider() instanceof CompatAbstractSelector) {
            if (!((CompatAbstractSelector) targetState.provider()).hasSomethingToPlace()) return false;
        }

        return targetState.target().get().test(state, random);
    }

}

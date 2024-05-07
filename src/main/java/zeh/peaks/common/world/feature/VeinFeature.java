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
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import zeh.peaks.common.Configuration;
import zeh.peaks.common.world.configuration.CylindricalSurface;
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

        BlockState _replaceBottom = _vein.bottomDeposit().getState(_random, _pos);

        int count = 0;
        int _maxRadius = radius + _vein.extraRadius() + _random.nextInt(deltaRadius + _vein.extraDeltaRadius());
        BlockPos.MutableBlockPos _anchor = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos _offset = new BlockPos.MutableBlockPos();
        BulkSectionAccess _bulk = new BulkSectionAccess(_level);

        try {
            for (int i = -_maxRadius; i <= _maxRadius; i++) {
                for (int j = -_maxRadius; j <= _maxRadius; j++) {
                    int _dr = _maxRadius - _random.nextInt(deltaRadius + _vein.extraDeltaRadius();

                    if (i * i + j * j <= _dr * _dr) {
                        _anchor.set(_pos.offset(0, _maxRadius + 1, 0));
                        boolean _extraBlocks = true;
                        float _radius = Mth.sqrt((float) (i * i + j * j) / (_dr * _dr));
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

                            BlockState state = _section.getBlockState(_x, _y, _z);
                            for (CylindricalSurface surface : _vein.surfaces()) {
                                if (_radius >= surface.minRadius() && _radius <= surface.maxRadius()) {
                                    for (TargetBlockState targetState : surface.targets()) {
                                        if (canPlaceOre(state, _random, targetState)) {
                                            _section.setBlockState(_x, _y, _z, targetState.state, false);
                                            ++count;

                                            if (_extraBlocks && _vein.topEnabled()) {
                                                for (int m = 0; m <= _peak * _vein.topMultiplier(); m++) {
                                                    _offset.setWithOffset(_anchor, i, m, j);
                                                    if (!_level.ensureCanWrite(_offset)) continue;
                                                    LevelChunkSection _top = _bulk.getSection(_offset);
                                                    if (_top == null) continue;
                                                    int _xx = SectionPos.sectionRelative(_offset.getX());
                                                    int _yy = SectionPos.sectionRelative(_offset.getY());
                                                    int _zz = SectionPos.sectionRelative(_offset.getZ());
                                                    _top.setBlockState(_xx, _yy, _zz, targetState.state, false);
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

                        if (_vein.bottomEnabled()) {
                            for (int m = 1; m <= _peak * _vein.bottomMultiplier(); m++) {
                                _offset.setWithOffset(_anchor, i, m, j);
                                if (!_level.ensureCanWrite(_offset)) continue;
                                LevelChunkSection _bottom = _bulk.getSection(_offset);
                                if (_bottom == null) continue;
                                int _xx = SectionPos.sectionRelative(_offset.getX());
                                int _yy = SectionPos.sectionRelative(_offset.getY());
                                int _zz = SectionPos.sectionRelative(_offset.getZ());

                                _bottom.setBlockState(_xx, _yy, _zz, _replaceBottom, false);
                                count++;
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

    private static boolean canPlaceOre(BlockState state, RandomSource random, TargetBlockState targetState) {
        return targetState.target.test(state, random);
    }

}

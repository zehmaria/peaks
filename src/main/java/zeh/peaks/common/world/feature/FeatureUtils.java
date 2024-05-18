package zeh.peaks.common.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;

public class FeatureUtils {
    public static boolean cannotWrite(WorldGenLevel level, BlockPos pos) {
        if (level instanceof WorldGenRegion region) {
            int i = SectionPos.blockToSectionCoord(pos.getX());
            int j = SectionPos.blockToSectionCoord(pos.getZ());

            ChunkPos center = region.getCenter();
            int k = Math.abs(center.x - i);
            int l = Math.abs(center.z - j);

            return k > region.writeRadiusCutoff || l > region.writeRadiusCutoff;
        } return true;
    }
}

package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;

import java.util.List;

public record CylindricalSurface(float minRadius, float maxRadius,
                                 List<TargetBlockState> targets) implements FeatureConfiguration {

    public static final Codec<CylindricalSurface> CODEC = RecordCodecBuilder.create(config -> config.group(
            Codec.floatRange(0, 1).fieldOf("min_radius").forGetter(CylindricalSurface::minRadius),
            Codec.floatRange(0, 1).fieldOf("max_radius").forGetter(CylindricalSurface::maxRadius),
            Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(CylindricalSurface::targets)
    ).apply(config, CylindricalSurface::new));

}

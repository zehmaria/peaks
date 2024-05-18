package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import zeh.peaks.common.world.providers.CompatAbstractSelector;

import java.util.List;

public record VeinConfiguration(int radius, int deltaRadius,
								float topMultiplier, float bottomMultiplier, ExpandedTargetBlockState bottomDeposit,
								List<CylindricalSurface> surfaces) implements FeatureConfiguration, CompatAbstractConfiguration {

	public static final Codec<VeinConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
			Codec.INT.fieldOf("radius").forGetter(VeinConfiguration::radius),
			Codec.INT.fieldOf("delta_radius").forGetter(VeinConfiguration::deltaRadius),

			Codec.FLOAT.fieldOf("top_height_multiplier").forGetter(VeinConfiguration::topMultiplier),
			Codec.FLOAT.fieldOf("bottom_height_multiplier").forGetter(VeinConfiguration::bottomMultiplier),
			ExpandedTargetBlockState.CODEC.fieldOf("bottom_deposit").forGetter(VeinConfiguration::bottomDeposit),

			Codec.list(CylindricalSurface.CODEC).fieldOf("cylindrical_surfaces").forGetter(VeinConfiguration::surfaces)
	).apply(config, VeinConfiguration::new));

	public boolean checkValidCompat() {
		int __check = 0;
		for (CylindricalSurface surface : this.surfaces()) {
			for (ExpandedTargetBlockState targetState : surface.targets()) {
				if (targetState.provider() instanceof CompatAbstractSelector) {
					if (((CompatAbstractSelector) targetState.provider()).hasSomethingToPlace()) __check++;
				} else __check++;
			}
		}
		return __check > 0;
	}
}
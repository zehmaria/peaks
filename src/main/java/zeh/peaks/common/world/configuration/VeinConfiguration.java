package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;


public record VeinConfiguration(int extraRadius, int extraDeltaRadius,
								boolean topEnabled, float topMultiplier,
								boolean bottomEnabled, float bottomMultiplier, BlockStateProvider bottomDeposit,
								List<CylindricalSurface> surfaces) implements FeatureConfiguration {

	public static final Codec<VeinConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
			Codec.INT.fieldOf("extra_radius").forGetter(VeinConfiguration::extraRadius),
			Codec.INT.fieldOf("extra_delta_radius").forGetter(VeinConfiguration::extraDeltaRadius),

			Codec.BOOL.fieldOf("top_enabled").forGetter(VeinConfiguration::topEnabled),
			Codec.FLOAT.fieldOf("top_height_multiplier").forGetter(VeinConfiguration::topMultiplier),

			Codec.BOOL.fieldOf("bottom_enabled").forGetter(VeinConfiguration::bottomEnabled),
			Codec.FLOAT.fieldOf("bottom_height_multiplier").forGetter(VeinConfiguration::bottomMultiplier),
			BlockStateProvider.CODEC.fieldOf("bottom_deposit").forGetter(VeinConfiguration::bottomDeposit),

			Codec.list(CylindricalSurface.CODEC).fieldOf("cylindrical_surfaces").forGetter(VeinConfiguration::surfaces)
		).apply(config, VeinConfiguration::new));

}

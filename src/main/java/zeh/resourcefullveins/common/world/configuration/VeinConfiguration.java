package zeh.resourcefullveins.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;


public record VeinConfiguration(int extraRadius, int extraDeltaRadius, BlockStateProvider replaceStone,
								BlockStateProvider replaceDeepslate) implements FeatureConfiguration {

	public static final Codec<VeinConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
			Codec.INT.fieldOf("extra_radius").forGetter(VeinConfiguration::extraRadius),
			Codec.INT.fieldOf("extra_delta_radius").forGetter(VeinConfiguration::extraDeltaRadius),
			BlockStateProvider.CODEC.fieldOf("replace_stone").forGetter(VeinConfiguration::replaceStone),
			BlockStateProvider.CODEC.fieldOf("replace_deepslate").forGetter(VeinConfiguration::replaceDeepslate)
		).apply(config, VeinConfiguration::new));

    public int extraRadius() { return this.extraRadius; }
	public int extraDeltaRadius() { return this.extraDeltaRadius; }
	public BlockStateProvider replaceStone() { return this.replaceStone; }
	public BlockStateProvider replaceDeepslate() { return this.replaceDeepslate; }

}

package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;


public record VeinConfiguration(int extraRadius, int extraDeltaRadius,
								BlockStateProvider replaceBottom, BlockStateProvider replaceCore,
								BlockStateProvider replaceStone, BlockStateProvider replaceStoneWrapper,
								BlockStateProvider replaceDeepslate, BlockStateProvider replaceDeepslateWrapper) implements FeatureConfiguration {

	public static final Codec<VeinConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
			Codec.INT.fieldOf("extra_radius").forGetter(VeinConfiguration::extraRadius),
			Codec.INT.fieldOf("extra_delta_radius").forGetter(VeinConfiguration::extraDeltaRadius),
			BlockStateProvider.CODEC.fieldOf("replace_bottom").forGetter(VeinConfiguration::replaceBottom),
			BlockStateProvider.CODEC.fieldOf("replace_core").forGetter(VeinConfiguration::replaceCore),
			BlockStateProvider.CODEC.fieldOf("replace_stone").forGetter(VeinConfiguration::replaceStone),
			BlockStateProvider.CODEC.fieldOf("replace_stone_wrapper").forGetter(VeinConfiguration::replaceStoneWrapper),
			BlockStateProvider.CODEC.fieldOf("replace_deepslate").forGetter(VeinConfiguration::replaceDeepslate),
			BlockStateProvider.CODEC.fieldOf("replace_deepslate_wrapper").forGetter(VeinConfiguration::replaceDeepslateWrapper)
		).apply(config, VeinConfiguration::new));

    public int extraRadius() { return this.extraRadius; }
	public int extraDeltaRadius() { return this.extraDeltaRadius; }
	public BlockStateProvider replaceBottom() { return this.replaceBottom; }
	public BlockStateProvider replaceCore() { return this.replaceCore; }
	public BlockStateProvider replaceStone() { return this.replaceStone; }
	public BlockStateProvider replaceStoneWrapper() { return this.replaceStoneWrapper; }
	public BlockStateProvider replaceDeepslate() { return this.replaceDeepslate; }
	public BlockStateProvider replaceDeepslateWrapper() { return this.replaceDeepslateWrapper; }

}

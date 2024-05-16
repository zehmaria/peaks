package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.Optional;

public record ExpandedTargetBlockState(BlockStateProvider provider,
                                       Optional<RuleTest> target) implements FeatureConfiguration {

    public static final Codec<ExpandedTargetBlockState> CODEC = RecordCodecBuilder.create(config -> config.group(
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(ExpandedTargetBlockState::provider),
            RuleTest.CODEC.optionalFieldOf("target").forGetter(ExpandedTargetBlockState::target)
    ).apply(config, ExpandedTargetBlockState::new));

}

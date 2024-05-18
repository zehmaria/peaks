package zeh.peaks.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import zeh.peaks.common.world.feature.WeightedFeature;

import java.util.ArrayList;
import java.util.List;

public record WeightedRandomConfiguration(List<WeightedFeature> features) implements FeatureConfiguration {

    public static final Codec<WeightedRandomConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
            WeightedFeature.CODEC.listOf().fieldOf("features").forGetter(instance -> instance.features)
    ).apply(config, WeightedRandomConfiguration::new));

    public int validTotalWeight() {
        int total = 0;
        for (WeightedFeature weighted : this.features) {
            if (weighted.feature.value().feature().value().config() instanceof CompatAbstractConfiguration) {
                if (((CompatAbstractConfiguration) weighted.feature.value().feature().value().config()).checkValidCompat())
                    total += weighted.weight;
            } else total += weighted.weight;
        }
        return total;
    }
    public List<WeightedFeature> validFeatures() {
        List<WeightedFeature> list = new ArrayList<>();
        for (WeightedFeature feature : this.features) {
            if (feature.feature.value().feature().value().config() instanceof CompatAbstractConfiguration) {
                if (((CompatAbstractConfiguration) feature.feature.value().feature().value().config()).checkValidCompat())
                    list.add(feature);
            } else list.add(feature);
        }
        return  list;
    }

}
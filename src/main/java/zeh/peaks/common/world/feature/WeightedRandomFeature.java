package zeh.peaks.common.world.feature;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import zeh.peaks.common.world.configuration.WeightedRandomConfiguration;

import java.util.List;

public class WeightedRandomFeature extends Feature<WeightedRandomConfiguration> {

    public WeightedRandomFeature() {
        super(WeightedRandomConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<WeightedRandomConfiguration> context) {
        WeightedRandomConfiguration config = context.config();
        RandomSource random = context.random();
        List<WeightedFeature> compatValid = config.validFeatures();
        int total = config.validTotalWeight();

        int picker = 0;
        for (double r = random.nextDouble() * total; picker < compatValid.size() - 1; ++picker) {
            r -= compatValid.get(picker).weight;
            if (r <= 0.0) break;
        }

        return compatValid.get(picker).place(context.level(), context.chunkGenerator(), random, context.origin());
    }

}

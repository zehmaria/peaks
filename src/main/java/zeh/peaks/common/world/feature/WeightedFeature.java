package zeh.peaks.common.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class WeightedFeature {

    public static final Codec<WeightedFeature> CODEC = RecordCodecBuilder.create(config -> config.group(
            PlacedFeature.CODEC.fieldOf("feature").forGetter(instance -> instance.feature),
            Codec.INT.fieldOf("weight").forGetter(instance -> instance.weight)
    ).apply(config, WeightedFeature::new));

    public final Holder<PlacedFeature> feature;
    public final int weight;

    public WeightedFeature(Holder<PlacedFeature> holder, int i) {
        this.feature = holder;
        this.weight = i;
    }

    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos) {
        return this.feature.value().place(level, generator, random, pos);
    }

}
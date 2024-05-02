package zeh.resourcefullveins.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.Optional;

public record VeinConfiguration(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> primaryFeature,
								@Nullable Holder<PlacedFeature> floorFeature) implements FeatureConfiguration {

    public static final Codec<VeinConfiguration> CODEC = RecordCodecBuilder.create((config) -> config.group(
			ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter(VeinConfiguration::tries),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter(VeinConfiguration::xzSpread),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(VeinConfiguration::ySpread),
			PlacedFeature.CODEC.fieldOf("primary_feature").forGetter(VeinConfiguration::primaryFeature),
			PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter(floorConfig -> Optional.ofNullable(floorConfig.floorFeature))
	    ).apply(config, (tries, xzSpread, ySpread, primary, floor) -> floor.map(
				placedFeatureHolder -> new VeinConfiguration(tries, xzSpread, ySpread, primary, placedFeatureHolder)
			).orElseGet(() -> new VeinConfiguration(tries, xzSpread, ySpread, primary, null))
        )
    );

    public int tries() { return this.tries; }
	public int xzSpread() { return this.xzSpread; }
	public int ySpread() { return this.ySpread; }
	public Holder<PlacedFeature> primaryFeature() { return this.primaryFeature; }
	public Holder<PlacedFeature> floorFeature() { return this.floorFeature; }

}

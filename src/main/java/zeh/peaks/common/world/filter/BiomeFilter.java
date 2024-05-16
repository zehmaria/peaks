package zeh.peaks.common.world.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;
import zeh.peaks.common.registry.ModPlacementModifiers;

public class BiomeFilter extends PlacementFilter implements PlacementModifierType<BiomeFilter> {

	public static final MapCodec<BiomeFilter> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
			Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter((instance) -> instance.allowedBiomes)
	).apply(builder, BiomeFilter::new));

	private final HolderSet<Biome> allowedBiomes;

	private BiomeFilter(HolderSet<Biome> allowedBiomes) {
		this.allowedBiomes = allowedBiomes;
	}

	@Override
	protected boolean shouldPlace(PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
		Holder<Biome> biome = context.getLevel().getBiome(pos);
		return this.allowedBiomes.contains(biome);
	}

	@Override
	public @NotNull PlacementModifierType<?> type() {
		return ModPlacementModifiers.BIOME_TAG.get();
	}

	@Override
	public @NotNull MapCodec<BiomeFilter> codec() {
		return CODEC;
	}
}
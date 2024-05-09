package zeh.peaks.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.filter.BiomeTagFilter;

public class ModPlacementModifiers {

	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
			DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.key(), Peaks.MODID);

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<BiomeTagFilter>> BIOME_TAG =
			PLACEMENT_MODIFIERS.register("biome_tag", () -> () -> BiomeTagFilter.CODEC);

}

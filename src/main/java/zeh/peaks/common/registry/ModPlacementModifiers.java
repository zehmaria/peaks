package zeh.peaks.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.filter.BiomeFilter;

public class ModPlacementModifiers {

	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
			DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Peaks.MODID);

	public static final RegistryObject<PlacementModifierType<BiomeFilter>> BIOME_TAG =
			PLACEMENT_MODIFIERS.register("biome_filter", () -> () -> BiomeFilter.CODEC);

}

package zeh.resourcefullveins.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zeh.resourcefullveins.ResourcefullVeins;
import zeh.resourcefullveins.common.world.filter.BiomeTagFilter;

public class ModPlacementModifiers {

	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
			DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, ResourcefullVeins.MODID);

	public static final RegistryObject<PlacementModifierType<BiomeTagFilter>> BIOME_TAG =
			PLACEMENT_MODIFIERS.register("biome_tag", () -> typeConvert(BiomeTagFilter.CODEC));

	private static <P extends PlacementModifier> PlacementModifierType<P> typeConvert(Codec<P> codec) {
		return () -> codec;
	}

}

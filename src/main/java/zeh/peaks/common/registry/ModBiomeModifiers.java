package zeh.peaks.common.registry;

import com.mojang.serialization.Codec;

import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.modifier.AddFeaturesByFilterBiomeModifier;

public class ModBiomeModifiers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Peaks.MODID);

    public static final DeferredHolder<Codec<? extends BiomeModifier>, Codec<AddFeaturesByFilterBiomeModifier>> ADD_FEATURES_BY_FILTER =
            BIOME_MODIFIER_SERIALIZERS.register("add_features_by_filter", () -> AddFeaturesByFilterBiomeModifier.CODEC);

}
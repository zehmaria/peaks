package zeh.peaks.common.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.modifier.AddFeaturesByFilterBiomeModifier;

public class ModBiomeModifiers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Peaks.MODID);

    public static RegistryObject<Codec<AddFeaturesByFilterBiomeModifier>> ADD_FEATURES_BY_FILTER =
            BIOME_MODIFIER_SERIALIZERS.register("add_features_by_filter", () -> AddFeaturesByFilterBiomeModifier.CODEC);

}
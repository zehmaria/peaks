package zeh.resourcefullveins.common.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zeh.resourcefullveins.ResourcefullVeins;
import zeh.resourcefullveins.common.world.feature.VeinFeature;

public class ModBiomeFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, ResourcefullVeins.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VEINS =
            FEATURES.register("gen_veins", () -> new VeinFeature(NoneFeatureConfiguration.CODEC));

}
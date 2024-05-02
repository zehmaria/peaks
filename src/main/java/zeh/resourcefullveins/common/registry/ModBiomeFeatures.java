package zeh.resourcefullveins.common.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import zeh.resourcefullveins.ResourcefullVeins;
import zeh.resourcefullveins.common.world.feature.VeinFeature;
import zeh.resourcefullveins.common.world.configuration.VeinConfiguration;

public class ModBiomeFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, ResourcefullVeins.MODID);

    public static final RegistryObject<Feature<VeinConfiguration>> VEINS =
            FEATURES.register("gen_veins", () -> new VeinFeature(VeinConfiguration.CODEC));

}
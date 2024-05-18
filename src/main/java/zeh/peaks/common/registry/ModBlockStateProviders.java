package zeh.peaks.common.registry;

import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.providers.CompatPrioritySelector;

public class ModBlockStateProviders {

    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS =
            DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, Peaks.MODID);

    public static final RegistryObject<BlockStateProviderType<CompatPrioritySelector>> COMPAT =
            BLOCK_STATE_PROVIDERS.register("compat_priority_selector", () -> new BlockStateProviderType<>(CompatPrioritySelector.CODEC));

}

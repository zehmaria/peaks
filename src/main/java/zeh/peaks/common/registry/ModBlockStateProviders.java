package zeh.peaks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.providers.CompatPrioritySelector;

public class ModBlockStateProviders {

    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS =
            DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, Peaks.MODID);

    public static final DeferredHolder<BlockStateProviderType<?>, BlockStateProviderType<CompatPrioritySelector>> COMPAT =
            BLOCK_STATE_PROVIDERS.register("compat_priority_selector", () -> new BlockStateProviderType<>(CompatPrioritySelector.CODEC));

}

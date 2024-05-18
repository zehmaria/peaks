package zeh.peaks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zeh.peaks.Peaks;
import zeh.peaks.common.world.providers.CompatPrioritySelector;

public class ModBlockStateProviders {

    public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS =
            DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, Peaks.MODID);

    public static final RegistryObject<BlockStateProviderType<CompatPrioritySelector>> COMPAT =
            BLOCK_STATE_PROVIDERS.register("compat_priority_selector", () -> new BlockStateProviderType<>(CompatPrioritySelector.CODEC));

}

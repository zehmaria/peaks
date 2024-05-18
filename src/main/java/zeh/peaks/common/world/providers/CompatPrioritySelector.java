package zeh.peaks.common.world.providers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import zeh.peaks.common.Configuration;
import zeh.peaks.common.registry.ModBlockStateProviders;

import java.util.List;

public class CompatPrioritySelector extends BlockStateProvider implements CompatAbstractSelector {

    public static final Codec<CompatPrioritySelector> CODEC = RecordCodecBuilder.create(config -> config.group(
            Codec.STRING.listOf().fieldOf("blocks").forGetter(instance -> instance.blocks)
    ).apply(config, CompatPrioritySelector::new));

    List<String> blocks;

    private CompatPrioritySelector(List<String> blocks) {
        this.blocks = blocks;
    }

    @Override
    protected @NotNull BlockStateProviderType<?> type() {
        return ModBlockStateProviders.COMPAT.get();
    }

    public boolean hasSomethingToPlace() {
        for (String _mod : Configuration.MOD_PRIORITY.get()) {
            if (ModList.get().isLoaded(_mod)) {
                for (String _block : this.blocks) {
                    if (_block.substring(0, _block.indexOf(":")).equals(_mod)) {
                        BlockState priority = BuiltInRegistries.BLOCK.get(new ResourceLocation(_block)).defaultBlockState();
                        if (!priority.isAir()) return true;
                    }
                }
            }
        }
        return false;
    }

    public @NotNull BlockState getState(@NotNull RandomSource random, @NotNull BlockPos pos) {
        for (String _mod : Configuration.MOD_PRIORITY.get()) {
            if (ModList.get().isLoaded(_mod)) {
                for (String _block : this.blocks) {
                    if (_block.substring(0, _block.indexOf(":")).equals(_mod)) {
                        BlockState priority = BuiltInRegistries.BLOCK.get(new ResourceLocation(_block)).defaultBlockState();
                        if (!priority.isAir()) return priority;
                    }
                }
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

}

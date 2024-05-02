package zeh.resourcefullveins.common;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import zeh.resourcefullveins.common.world.VeinGeneration;

public class CommonSetup {
    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            VeinGeneration.registerVeinGeneration();
        });
    }

}
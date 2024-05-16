package zeh.peaks.common;

import com.google.common.collect.ImmutableList;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Configuration {

    public static ModConfigSpec COMMON_CONFIG;

    //public static ModConfigSpec.IntValue VEIN_RADIUS;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MOD_PRIORITY;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        COMMON_BUILDER.comment("Vein generation").push("general_vein");

        //VEIN_RADIUS = COMMON_BUILDER.comment("Minimum radius for the veins.")
        //.defineInRange("minRadius", 8, 1, Integer.MAX_VALUE);

        MOD_PRIORITY = COMMON_BUILDER.comment("Compat Priority.")
                .defineList("compatPriority", ImmutableList.of("create", "immersiveengineering", "mekanism",
                        "thermal", "elementalcraft", "wizards_reborn", "biggerreactors", "evilcraft", "bigreactors",
                        "crossroads", "eidolon", "embers", "occultism"), obj -> true);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

}
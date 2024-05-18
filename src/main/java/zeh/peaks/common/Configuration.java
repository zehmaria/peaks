package zeh.peaks.common;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Configuration {

    public static ForgeConfigSpec COMMON_CONFIG;

    //public static ForgeConfigSpec.IntValue VEIN_RADIUS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> MOD_PRIORITY;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

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
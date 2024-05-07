package zeh.peaks.common;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Configuration {

    public static ModConfigSpec COMMON_CONFIG;

	public static ModConfigSpec.IntValue VEIN_RADIUS;
	public static ModConfigSpec.IntValue VEIN_DELTA_RADIUS;

    static {
		ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

	    COMMON_BUILDER.comment("Vein generation").push("general_vein");

		VEIN_RADIUS = COMMON_BUILDER.comment("Minimum radius for the veins.")
				.defineInRange("minRadius", 8, 1, Integer.MAX_VALUE);
		VEIN_DELTA_RADIUS = COMMON_BUILDER.comment("Random variance range for the radius.")
				.defineInRange("deltaRadius", 4, 0, Integer.MAX_VALUE);

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
    }

}

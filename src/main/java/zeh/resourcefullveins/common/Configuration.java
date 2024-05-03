package zeh.resourcefullveins.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Configuration {

    public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.IntValue VEIN_RADIUS;
	public static ForgeConfigSpec.IntValue VEIN_DELTA_RADIUS;

    static {
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	    COMMON_BUILDER.comment("Vein generation").push("general_vein");

		VEIN_RADIUS = COMMON_BUILDER.comment("Minimum radius for the veins.")
				.defineInRange("minRadius", 8, 1, Integer.MAX_VALUE);
		VEIN_DELTA_RADIUS = COMMON_BUILDER.comment("Random variance range for the radius.")
				.defineInRange("deltaRadius", 4, 0, Integer.MAX_VALUE);

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
    }

}

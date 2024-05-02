package zeh.resourcefullveins.common;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import zeh.resourcefullveins.ResourcefullVeins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ResourcefullVeins.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Configuration {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static final String CATEGORY_WORLD = "world";

	public static ForgeConfigSpec.IntValue VEIN_MIN_RADIUS;
	public static ForgeConfigSpec.IntValue VEIN_RADIUS_DELTA;

	public static final ForgeConfigSpec VEINS_SPEC;
	public static final Veins VEINS;
	public static List<VeinConfig> veinsList;

	private static final ObjectConverter CONVERTER = new ObjectConverter();

	public static class Veins {
		public Veins(ForgeConfigSpec.Builder builder) {
			builder.comment("List of veins").define("veins", new ArrayList<>());
			builder.build();
		}
	}

    static {
		final Pair<Veins, ForgeConfigSpec> groupsPair = new ForgeConfigSpec.Builder().configure(Veins::new);
		VEINS_SPEC = groupsPair.getRight();
		VEINS = groupsPair.getLeft();

		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("World generation").push(CATEGORY_WORLD);

	    COMMON_BUILDER.comment("Vein generation").push("general_vein");

		VEIN_MIN_RADIUS = COMMON_BUILDER.comment("Minimum radius for the veins.")
				.defineInRange("minRadius", 8, 1, Integer.MAX_VALUE);
		VEIN_RADIUS_DELTA = COMMON_BUILDER.comment("Random variance range for the radius.")
				.defineInRange("deltaRadius", 4, 0, Integer.MAX_VALUE);

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
    }

	public static void setup() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

		String name = ResourcefullVeins.MODID + "-veins.toml";
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.VEINS_SPEC, name);
		File df = new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()) + "/" + name);

		if (!df.exists()) {
			try {
				FileUtils.copyInputStreamToFile(Objects.requireNonNull(ResourcefullVeins.class.getClassLoader().getResourceAsStream(name)), df);
			} catch (IOException e) {
                ResourcefullVeins.LOGGER.error("Error creating default config for {}", name); }
		}
	}

	private static synchronized void load(final ModConfig config) {
		if (config.getModId().equals(ResourcefullVeins.MODID)) {
			IConfigSpec<?> spec = config.getSpec();
			CommentedConfig configData = config.getConfigData();
			if (spec == VEINS_SPEC) {
				Configuration.veinsList = CONVERTER.toObject(configData, VeinConfigList::new).veins;
			}
		}
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	static void configLoad(final ModConfigEvent.Loading evt) {
		load(evt.getConfig());
	}
	@SubscribeEvent
	@SuppressWarnings("unused")
	static void configReload(final ModConfigEvent.Reloading evt) { load(evt.getConfig()); }

}

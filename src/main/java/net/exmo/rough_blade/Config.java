package net.exmo.rough_blade;


import net.exmo.rough_blade.network.LSVARB;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Rough_blade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static boolean isLoaded = false;
    public static void tryLoad() {
        if (!isLoaded) {
            Rough_blade.LOGGER.info("Rough_blade: Config is loading...");
            Rough_blade.LOGGER.info("Rough_blade: Config is loaded.");
            isLoaded = true;
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
        }
    }
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 配置项定义
    public static final ForgeConfigSpec.BooleanValue POWER = BUILDER
            .comment("power mode open or close")
            .define("Power", false);
    // 配置项定义
    public static final ForgeConfigSpec.BooleanValue RANK_DAMAGE = BUILDER
            .comment("rank boost damage mode open or close")
            .define("Rank_damage", false);
    public static final ForgeConfigSpec.BooleanValue CANT_OTHER_ENTITY_TELEPORT_TO_DIMENSION = BUILDER
            .comment("Cant_other_entity_teleport_to_dimension mode open or close")
            .define("CANT_OTHER_ENTITY_TELEPORT_TO_DIMENSION", false); ;
    // 配置项定义
    public static final ForgeConfigSpec.BooleanValue CLOSE_TEXT_RENDER = BUILDER
            .comment("close text render")
            .define("CLOSE_TEXT_RENDER", false);
    public static final ForgeConfigSpec.BooleanValue OLD_TEXT_RENDER_ALAWAYS = BUILDER
            .comment("OLD_TEXT_RENDER_ALAWAYS mode open or close")
            .define("Old_Text_Render", false);
    public static final ForgeConfigSpec.BooleanValue NEW_TEXT_RENDER_ALAWAYS = BUILDER
            .comment("NEW_TEXT_RENDER_ALAWAYS mode open or close")
            .define("NEW_TEXT_RENDER_ALAWAYS", false);
    public static final ForgeConfigSpec.BooleanValue COMBO_SCREEN = BUILDER
            .comment("COMBO_SCREEN open or close")
            .define("COMBO_SCREEN", true);



    public static final ForgeConfigSpec SPEC ;

    static {
        SPEC = BUILDER.build();
    }
    // 配置值缓存



    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {

    }
}

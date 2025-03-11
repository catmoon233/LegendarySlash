package net.exmo.legendary_slash;

import net.exmo.exmodifier.Exmodifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Legendary_slash.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 配置项定义
    public static final ForgeConfigSpec.BooleanValue POWER = BUILDER
            .comment("power mode open or close")
            .define("Power", true);



    public static final ForgeConfigSpec SPEC ;

    static {
        SPEC = BUILDER.build();
    }
    // 配置值缓存



    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {

    }
}

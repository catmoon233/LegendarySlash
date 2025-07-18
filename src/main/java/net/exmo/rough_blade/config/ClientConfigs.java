package net.exmo.rough_blade.config;


import net.exmo.rough_blade.content.screen.PowerBarOverlay;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_BAR_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_BAR_X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_TEXT_X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_TEXT_Y_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MANA_BAR_TEXT_VISIBLE;
    public static final ForgeConfigSpec.ConfigValue<PowerBarOverlay.Anchor> MANA_BAR_ANCHOR;
    public static final ForgeConfigSpec.ConfigValue<PowerBarOverlay.Display> MANA_BAR_DISPLAY;



    public static final ForgeConfigSpec SPEC;

    static {


        BUILDER.push("UI");
        BUILDER.push("PowerBar");
        BUILDER.comment("By default (Contextual), the mana bar only appears when you are holding a magic item or are not at max mana.");
        MANA_BAR_DISPLAY = BUILDER.defineEnum("PowerBarDisplay", PowerBarOverlay.Display.Contextual);
        BUILDER.comment("Used to adjust mana bar's position (11 is one full hunger bar up).");
        MANA_BAR_X_OFFSET = BUILDER.define("PowerBarXOffset", 0);
        MANA_BAR_Y_OFFSET = BUILDER.define("PowerBarYOffset", 0);
        MANA_BAR_TEXT_VISIBLE = BUILDER.define("PowerBarTextVisible", true);
        MANA_BAR_ANCHOR = BUILDER.defineEnum("PowerBarAnchor", PowerBarOverlay.Anchor.Hunger);
        MANA_TEXT_X_OFFSET = BUILDER.define("manaTextXOffset", 0);
        MANA_TEXT_Y_OFFSET = BUILDER.define("manaTextYOffset", 0);
        BUILDER.pop();
        BUILDER.push("SpellBar");
        BUILDER.comment("By default (Always), the spell bar always shows the spells in your equipped spellbook. Contextual will hide them when not in use.");
        BUILDER.comment("Used to adjust spell bar's position.");

        BUILDER.pop();
        BUILDER.push("RecastOverlay");

        BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("Animations");
        BUILDER.comment("What to render in first person while casting.");

        BUILDER.pop();

        BUILDER.push("Renderers");
        BUILDER.comment("By default, both fireballs are replaced with an enhanced model used by fire spells.");

        BUILDER.pop();

        BUILDER.push("Music");

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}

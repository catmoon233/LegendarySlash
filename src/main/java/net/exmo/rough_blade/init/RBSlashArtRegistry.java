package net.exmo.rough_blade.init;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.exmo.rough_blade.Rough_blade.MODID;

public class RBSlashArtRegistry {
    public static final DeferredRegister<SlashArts> SLASH_ARTS;
    public static final RegistryObject<SlashArts> ZJ;
    public static final RegistryObject<SlashArts> RSA;
    public static final RegistryObject<SlashArts> SUPER_SWORD;
    public static final RegistryObject<SlashArts> FULL_FIRE;
    public static final RegistryObject<SlashArts> BREAK_SKY;
    public static final RegistryObject<SlashArts> FZZ;
    public static final RegistryObject<SlashArts> FZU;
    public static final RegistryObject<SlashArts> ZHEN_LI;
    public static final RegistryObject<SlashArts> STAR_TRACK_ATTACK;
    public static final RegistryObject<SlashArts> STAR_TRACK_ATTACK_EX;
    public static final RegistryObject<SlashArts> SkyFallenSa;
    public static final RegistryObject<SlashArts> THE_INFINITY_MOON;
    public RBSlashArtRegistry() {
    }

    static {
        SLASH_ARTS = DeferredRegister.create(SlashArts.REGISTRY_KEY, MODID);
        ZJ = SLASH_ARTS.register("zj", () -> new SlashArts((e) -> ComboStateRegistry.ZJ.getId()));
        SkyFallenSa = SLASH_ARTS.register("sky_fallen", () -> new SlashArts((e) -> ComboStateRegistry.ZJ.getId()));
        RSA = SLASH_ARTS.register("rsa", () -> new SlashArts((e) -> ComboStateRegistry.RANDOM_SA.getId()));
        SUPER_SWORD = SLASH_ARTS.register("super_sword", () -> new SlashArts((e) -> ComboStateRegistry.SUPER_SOWRD.getId()));
        FULL_FIRE = SLASH_ARTS.register("full_fire", () -> new SlashArts((e) -> ComboStateRegistry.FULL_FIRE.getId()));
        BREAK_SKY = SLASH_ARTS.register("break_sky", () -> new SlashArts((e) -> ComboStateRegistry.BREAK_SKY.getId()));
        FZZ = SLASH_ARTS.register("fzz", () -> new SlashArts((e) -> ComboStateRegistry.FZZ.getId()));
        ZHEN_LI = SLASH_ARTS.register("zhen_li", () -> new SlashArts((e) -> ComboStateRegistry.Zhen_Li.getId()));
        FZU = SLASH_ARTS.register("fzu", () -> new SlashArts((e) -> ComboStateRegistry.FZU.getId()));
        STAR_TRACK_ATTACK = SLASH_ARTS.register("star_track_attack", () -> new SlashArts((e) -> ComboStateRegistry.STAR_TRACK_ATTACK_CB.getId()));
        STAR_TRACK_ATTACK_EX = SLASH_ARTS.register("star_track_attack_ex", () -> new SlashArts((e) -> ComboStateRegistry.STAR_TRACK_ATTACK_CB_EX.getId()));
        THE_INFINITY_MOON = SLASH_ARTS.register("the_infinity_moon", () -> new SlashArts((e) -> ComboStateRegistry.The_Infinity_Moon.getId()));

    }

}

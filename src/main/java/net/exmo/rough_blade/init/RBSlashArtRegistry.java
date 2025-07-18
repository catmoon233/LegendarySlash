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
    public RBSlashArtRegistry() {
    }

    static {
        SLASH_ARTS = DeferredRegister.create(SlashArts.REGISTRY_KEY, MODID);
        ZJ = SLASH_ARTS.register("zj", () -> new SlashArts((e) -> ComboStateRegistry.ZJ.getId()));
        RSA = SLASH_ARTS.register("rsa", () -> new SlashArts((e) -> ComboStateRegistry.RANDOM_SA.getId()));
        SUPER_SWORD = SLASH_ARTS.register("super_sword", () -> new SlashArts((e) -> ComboStateRegistry.SUPER_SOWRD.getId()));
        FULL_FIRE = SLASH_ARTS.register("full_fire", () -> new SlashArts((e) -> ComboStateRegistry.FULL_FIRE.getId()));
        BREAK_SKY = SLASH_ARTS.register("break_sky", () -> new SlashArts((e) -> ComboStateRegistry.BREAK_SKY.getId()));
        FZZ = SLASH_ARTS.register("fzz", () -> new SlashArts((e) -> ComboStateRegistry.FZZ.getId()));
        ZHEN_LI = SLASH_ARTS.register("zhen_li", () -> new SlashArts((e) -> ComboStateRegistry.Zhen_Li.getId()));
        FZU = SLASH_ARTS.register("fzu", () -> new SlashArts((e) -> ComboStateRegistry.FZU.getId()));

    }

}

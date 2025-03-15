package net.exmo.legendary_slash.init;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.exmo.legendary_slash.Legendary_slash.MODID;

public class LSSlashArtRegistry {
    public static final DeferredRegister<SlashArts> SLASH_ARTS;
    public static final RegistryObject<SlashArts> ZJ;
    public static final RegistryObject<SlashArts> RSA;
    public static final RegistryObject<SlashArts> SUPER_SWORD;
    public static final RegistryObject<SlashArts> FULL_FIRE;
    public LSSlashArtRegistry() {
    }

    static {
        SLASH_ARTS = DeferredRegister.create(SlashArts.REGISTRY_KEY, MODID);
        ZJ = SLASH_ARTS.register("zj", () -> new SlashArts((e) -> ComboStateRegistry.ZJ.getId()));
        RSA = SLASH_ARTS.register("rsa", () -> new SlashArts((e) -> ComboStateRegistry.RANDOM_SA.getId()));
        SUPER_SWORD = SLASH_ARTS.register("super_sword", () -> new SlashArts((e) -> ComboStateRegistry.SUPER_SOWRD.getId()));
        FULL_FIRE = SLASH_ARTS.register("full_fire", () -> new SlashArts((e) -> ComboStateRegistry.FULL_FIRE.getId()));

    }

}

package net.exmo.legendary_slash.init;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.content.specialEffects.StarFireSE;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LSSpecialEffectRegistry {
    public static final DeferredRegister<SpecialEffect> REGISTRY_KEY2;
    public static final RegistryObject<SpecialEffect> STAR_FIRE;
    public LSSpecialEffectRegistry() {
    }

    static {
        REGISTRY_KEY2 = DeferredRegister.create(SpecialEffect.REGISTRY_KEY, Legendary_slash.MODID);
        STAR_FIRE = REGISTRY_KEY2.register("star_fire", StarFireSE::new);//神佑
    }
}

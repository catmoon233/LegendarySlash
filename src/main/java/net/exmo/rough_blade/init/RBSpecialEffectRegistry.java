package net.exmo.rough_blade.init;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.specialEffects.MingLiSe;
import net.exmo.rough_blade.content.specialEffects.StarFireSE;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RBSpecialEffectRegistry {
    public static final DeferredRegister<SpecialEffect> REGISTRY_KEY2;
    public static final RegistryObject<SpecialEffect> STAR_FIRE;
    public static final RegistryObject<SpecialEffect> MingLi;
    public RBSpecialEffectRegistry() {
    }

    static {
        REGISTRY_KEY2 = DeferredRegister.create(SpecialEffect.REGISTRY_KEY, Rough_blade.MODID);
        STAR_FIRE = REGISTRY_KEY2.register("star_fire", StarFireSE::new);
        MingLi = REGISTRY_KEY2.register("ming_li",MingLiSe::new );
    }
}

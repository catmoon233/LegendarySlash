package net.exmo.rough_blade.init;

import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.content.specialEffects.MingLiSe;
import net.exmo.rough_blade.content.specialEffects.StarFireSE;
import net.exmo.rough_blade.content.specialEffects.StarrySkySE;
import net.exmo.rough_blade.content.specialEffects.TheWaningMoonSe;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RBSpecialEffectRegistry {
    public static final DeferredRegister<SpecialEffect> REGISTRY_KEY2;
    public static final RegistryObject<SpecialEffect> STAR_FIRE;
    public static final RegistryObject<SpecialEffect> MingLi;
    public static final RegistryObject<SpecialEffect> StarrySky;
    public static final RegistryObject<SpecialEffect> The_Waning_Moon;


    //extra
    public static final RegistryObject<SpecialEffect> The_Star_Power;
    public static final RegistryObject<SpecialEffect> The_Weak_Power;
    public RBSpecialEffectRegistry() {
    }

    static {
        REGISTRY_KEY2 = DeferredRegister.create(SpecialEffect.REGISTRY_KEY, Rough_blade.MODID);
        STAR_FIRE = REGISTRY_KEY2.register("star_fire", StarFireSE::new);
        MingLi = REGISTRY_KEY2.register("ming_li",MingLiSe::new );
        StarrySky = REGISTRY_KEY2.register("starry_sky", () -> new StarrySkySE(0));
        The_Waning_Moon = REGISTRY_KEY2.register("the_waning_moon", () -> new TheWaningMoonSe(0));
        The_Star_Power = REGISTRY_KEY2.register("the_star_power", () -> new SpecialEffect(0));
        The_Weak_Power = REGISTRY_KEY2.register("the_weak_power", () -> new SpecialEffect(0));
    }

    @Mod.EventBusSubscriber
    public class extraEffects{
        @SubscribeEvent
        public static void OnHurt(LivingHurtEvent event){
            if (event.getSource().getEntity() instanceof LivingEntity livingEntity){
                 var mainHandItem = livingEntity.getMainHandItem();
                    SpecialEffectEx.hurtEventHandleSe(event, (player, entity,slashBladeState) -> {
                        if (entity.level().isDay() && entity.level().canSeeSky(entity.blockPosition())){
                            SpecialEffectEx.hurtEventAmountBase(event,-0.9f);

                        }else {
                            SpecialEffectEx.hurtEventAmountBase(event,0.25f);
                        }
                    }, The_Star_Power.getId());

                    SpecialEffectEx.hurtEventHandleSe(event, (player, entity,slashBladeState) -> {
                        SpecialEffectEx.hurtEventAmountBase(event,-0.5f);
                    }, The_Weak_Power.getId());
                }


        }

    }
}


package net.exmo.rough_blade.content.effects;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
@Mod.EventBusSubscriber
public class LSMingYunEffect extends MobEffect {
    protected final double multiplier;
    protected final double MaxLevel;
    private static final String DAMAGE_REDUCE_UUID =("e7b6b8b2-c8a3-4f02-b6b9-e7b6b8b2c9a4");
    public LSMingYunEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.red.getRGB());
        addAttributeModifier(Attributes.ATTACK_DAMAGE, DAMAGE_REDUCE_UUID,  0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.multiplier = -0.005;
        this.MaxLevel = 100;
    }
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void addEffect(MobEffectEvent. event){
//        if (event.getEffectInstance().getEffect().equals(Rough_blade.effectAbout.MingLiEffect.get()))event.setCanceled(false);
//        if (event.getEffectInstance().getEffect().equals(Rough_blade.effectAbout.ZhenLiEffect.get()))event.setCanceled(false);
//    }
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public double getAttributeModifierValue(int p_19430_, AttributeModifier p_19431_) {
        return Math.min(p_19430_, MaxLevel) * multiplier;
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int p_19468_) {
        super.applyEffectTick(livingEntity, p_19468_);

    }
    @SubscribeEvent
    public static void Crit(LivingHurtEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (MingLiEffectHandle.isMingLi(livingEntity)){
                int amp = Math.max(MingLiEffectHandle.getMingLi(livingEntity)+1,99);
                event.setAmount(event.getAmount()*(amp+1)*0.01f);
            }

    }

}

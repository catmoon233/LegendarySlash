package net.exmo.legendary_slash.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class LSGuardSuccessEffect extends MobEffect {

    public LSGuardSuccessEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.YELLOW.getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
    }
}

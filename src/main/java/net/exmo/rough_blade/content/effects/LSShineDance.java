package net.exmo.rough_blade.content.effects;


import mods.flammpfeil.slashblade.util.AttackManager;
import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.awt.*;
import java.util.Comparator;

import static net.exmo.rough_blade.utils.ExUtils.sendParticleCircle;

public class LSShineDance extends MobEffect {
    public LSShineDance() {
        super(MobEffectCategory.BENEFICIAL, Color.GREEN.getRGB());

    }



    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {

        Vec3 _center = entity.position();
        Level var5 = entity.level();
        AttackManager.doSlash(entity, entity.level().random.nextInt(0,100));
        if (var5 instanceof ServerLevel l) {
            sendParticleCircle(l, entity, ParticleTypes.GLOW_SQUID_INK, 1.0F, 1);
        }

        for(Entity entityiterator : entity.level().getEntitiesOfClass(Entity.class, (new AABB(_center, _center)).inflate((double)2.0F), (a) -> true).stream().sorted(Comparator.comparingDouble((_entcnd) -> _entcnd.distanceToSqr(_center))).toList()) {
            if (entityiterator != entity && entityiterator instanceof LivingEntity) {
                ((LivingEntity)entityiterator).addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1, false, false));
                ((LivingEntity)entityiterator).addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1, false, false));
                ((LivingEntity)entityiterator).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, false, false));
                ((LivingEntity)entityiterator).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 2, false, false));
                if (entityiterator.invulnerableTime <= 0) {
                    Level var8 = entityiterator.level();
                    if (var8 instanceof ServerLevel) {
                        ServerLevel l = (ServerLevel)var8;
                        sendParticleCircle(l, entityiterator, ParticleTypes.SQUID_INK, 2.0F, 6);
                    }

                    entity.playSound(SoundEvents.ANVIL_PLACE);
                }

                entityiterator.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), entity), (float)((entity.getAttributeValue(Attributes.ATTACK_DAMAGE)+20) * (double)75F * (double)(p_19468_ + 1)));
            }
        }
        super.applyEffectTick(entity, p_19468_);
    }
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}

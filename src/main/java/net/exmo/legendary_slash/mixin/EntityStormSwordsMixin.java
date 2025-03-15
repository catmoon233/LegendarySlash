package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityStormSwords.class)
public class EntityStormSwordsMixin {
    @Inject(method = "onHitEntity", at = @At("HEAD"), remap = false)
    public void onHitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
       if (entityHitResult.getEntity() instanceof LivingEntity entity){
           entity.invulnerableTime = 0;
           entity.setSecondsOnFire(10);
           if (entity.level() instanceof ServerLevel level){
               level.sendParticles(ParticleTypes.LAVA, entity.getX(), entity.getY(), entity.getZ(), 6, 0.3, 0.3, 0.3, 0.3);
           }
           entity.setDeltaMovement(entity.getDeltaMovement().add(0,-0.2,0));
           entity.addEffect(new MobEffectInstance(MobEffects.GLOWING,10,0));
           if (entity.hasEffect(MobEffects.WITHER)){
               int amount = entity.getEffect(MobEffects.WITHER).getAmplifier()+1;
               if (Math.random() <0.5) {

                   if (amount <= 10) {
                       entity.removeEffect(MobEffects.WITHER);
                       entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 70, amount));
                   }
               }
               EntityStormSwords entityStormSwords = (EntityStormSwords) (Object) this;
               Entity owner = entityStormSwords.getOwner();
               if (owner instanceof LivingEntity livingEntity){
                   livingEntity.heal(amount*0.1f);
               }
           }


       }
    }
}

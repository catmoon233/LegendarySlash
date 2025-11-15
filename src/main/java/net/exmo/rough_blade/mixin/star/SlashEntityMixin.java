package net.exmo.rough_blade.mixin.star;

import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author canyuesama
 */
@Mixin(EntitySlashEffect.class)
public class SlashEntityMixin {
    @Inject(at = @At("HEAD"), method = "setShooter",remap = false)
    public void setShooter(Entity shooter, CallbackInfo ci) {
        if (shooter instanceof LivingEntity entity){
            if (SpecialEffectEx.hasSpecialEffect(entity.getMainHandItem(), RBSpecialEffectRegistry.StarrySky.getId())){
                EntitySlashEffect slashEffect = (EntitySlashEffect) (Object) this;
                slashEffect.setGlowingTag(true);

            }
        }
    }

}

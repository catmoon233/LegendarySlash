package net.exmo.rough_blade.mixin.guard;


import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.rough_blade.Rough_blade;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {


    @Inject(method = "shouldEntityAppearGlowing", at = @At(value = "HEAD"), cancellable = true)
    public void changeGlowOutline1(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        var player = Minecraft.getInstance().player;
        if (pEntity instanceof LivingEntity livingEntity) {
            if (pEntity != null && livingEntity.hasEffect(Rough_blade.effectAbout.GuardEffect.get()) || livingEntity.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())) {
                cir.setReturnValue(true);
            }
        }
        if (player.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())){
            if (pEntity instanceof Projectile projectile ){
                if (projectile.getOwner() == player){
                    cir.setReturnValue(true);
                }
            }
            if (pEntity instanceof net.minecraft.world.entity.projectile.Projectile projectile ){
                if (projectile.getOwner() == player){
                    cir.setReturnValue(true);
                }
            }

        }
        if (player.hasEffect(Rough_blade.effectAbout.TheInfinityMoonEffect.get())) {
            cir.setReturnValue(true);
        }
        }
}

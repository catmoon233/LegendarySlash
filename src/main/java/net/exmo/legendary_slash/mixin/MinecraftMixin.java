package net.exmo.legendary_slash.mixin;


import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.legendary_slash.Legendary_slash;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {


    @Inject(method = "shouldEntityAppearGlowing", at = @At(value = "HEAD"), cancellable = true,remap = true)
    public void changeGlowOutline1(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        var player = Minecraft.getInstance().player;
        if (pEntity instanceof LivingEntity livingEntity) {
            if (pEntity != null && livingEntity.hasEffect(Legendary_slash.effectAbout.GuardEffect.get()) || livingEntity.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())) {
                cir.setReturnValue(true);
            }
        }
        if (player.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())){
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
    }
}

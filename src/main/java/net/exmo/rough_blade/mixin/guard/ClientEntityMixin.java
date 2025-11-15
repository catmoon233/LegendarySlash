package net.exmo.rough_blade.mixin.guard;

import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Entity.class)

public class ClientEntityMixin {


    @Inject(method = "getTeamColor", at = @At(value = "HEAD"), cancellable = true)
    public void changeGlowOutline(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player.hasEffect(Rough_blade.effectAbout.TheInfinityMoonEffect.get())) {
            cir.setReturnValue(Color.RED.getRGB());
        }
        if (entity.getUUID()== player.getUUID()) {
            if (player != null && player.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())) {
                cir.setReturnValue(Color.YELLOW.getRGB());
            } else if (player != null && player.hasEffect(Rough_blade.effectAbout.GuardEffect.get())) {
                cir.setReturnValue(16711680);
            }
        }
        if (entity instanceof EntitySlashEffect entitySlashEffect){
            Entity shooter = entitySlashEffect.getShooter();
            if (shooter instanceof LivingEntity entity1) {
                if (entity1 != null && entity1.getUUID() == player.getUUID()) {
                    if (SpecialEffectEx.hasSpecialEffect(entity1.getMainHandItem(), RBSpecialEffectRegistry.StarrySky.getId())){
                        cir.setReturnValue(Color.BLUE.getRGB());
                    }
                }
            }
        }
    }
}

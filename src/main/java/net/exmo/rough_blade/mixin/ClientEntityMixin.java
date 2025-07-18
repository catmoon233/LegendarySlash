package net.exmo.rough_blade.mixin;

import net.exmo.rough_blade.Rough_blade;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Entity.class)

public class ClientEntityMixin {

    /**
     * Necessary see color glowing mob outlines while we have the echolocation effect
     */
    @Inject(method = "getTeamColor", at = @At(value = "HEAD"), cancellable = true)
    public void changeGlowOutline(CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())) {
            cir.setReturnValue(Color.YELLOW.getRGB());
        }else if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(Rough_blade.effectAbout.GuardEffect.get())) {
            cir.setReturnValue(16711680);
        }
    }
}

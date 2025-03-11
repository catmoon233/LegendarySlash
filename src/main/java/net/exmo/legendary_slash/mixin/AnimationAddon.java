package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.compat.playerAnim.PlayerAnimationOverrider;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import net.exmo.legendary_slash.init.ComboStateRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerAnimationOverrider.class)
public class AnimationAddon {
    @Shadow(remap = false) @Final private static ResourceLocation MotionLocation;

    @Inject(at = @At("TAIL"), method = "initAnimations",remap = false, cancellable = true)
    public void initAnimations(CallbackInfoReturnable<Map<ResourceLocation, VmdAnimation>> cir) {
    Map<ResourceLocation, VmdAnimation> map = cir.getReturnValue();
        map.put(ComboStateRegistry.ZJ.getId(),
                new VmdAnimation(MotionLocation, 2200, 2299, false).setBlendLegs(false));
    cir.setReturnValue(map);
    }
}

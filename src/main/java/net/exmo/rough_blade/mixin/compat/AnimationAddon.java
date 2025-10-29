package net.exmo.rough_blade.mixin.compat;

import mods.flammpfeil.slashblade.compat.playerAnim.PlayerAnimationOverrider;
import mods.flammpfeil.slashblade.compat.playerAnim.VmdAnimation;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
        map.put(ComboStateRegistry.Zhen_Li.getId(),
                new VmdAnimation(MotionLocation, 2200, 2299, false).setBlendLegs(false));
        map.put(ComboStateRegistry.MLZhan.getId(),
                new VmdAnimation(MotionLocation, 2200, 2299, false).setBlendLegs(false));
        map.put(ComboStateRegistry.COMBO_A4_Plus.getId(),
                new VmdAnimation(MotionLocation, 800.0F, (double)894.0F, false).setBlendLegs(false));
    cir.setReturnValue(map);
    }
}

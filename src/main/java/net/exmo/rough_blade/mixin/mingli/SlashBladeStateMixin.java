package net.exmo.rough_blade.mixin.mingli;

import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(SlashBladeState.class)
public abstract class SlashBladeStateMixin {


    @Shadow public abstract Collection<ResourceLocation> getSpecialEffects();

    @Inject(method = "setBroken", at = @At("HEAD"),remap = false, cancellable = true)
    public void setBroken(boolean broken, CallbackInfo ci) {
        if (getSpecialEffects().contains(RBSpecialEffectRegistry.MingLi.getId())){
            ci.cancel();
        }
    }
}

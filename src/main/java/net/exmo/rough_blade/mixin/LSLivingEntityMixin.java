package net.exmo.rough_blade.mixin;

import net.exmo.rough_blade.Rough_blade;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LSLivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "isFallFlying", cancellable = true)
    public void tryToStartFallFlying$LS(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(Rough_blade.effectAbout.ArmorStart.get())){
            cir.setReturnValue(true);

        }
    }
}

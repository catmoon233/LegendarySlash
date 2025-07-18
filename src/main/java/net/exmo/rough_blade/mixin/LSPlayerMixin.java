package net.exmo.rough_blade.mixin;

import net.exmo.rough_blade.Rough_blade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Player.class)
public abstract class LSPlayerMixin {

//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z", shift = At.Shift.AFTER), method = "tick")
//    public void tick(CallbackInfo ci) {
//        Player player = (Player) (Object) this;
//
//        if (player.hasEffect(Rough_blade.effectAbout.ArmorStart.get())){
//            player.noPhysics = true;
//        }
//    }



}

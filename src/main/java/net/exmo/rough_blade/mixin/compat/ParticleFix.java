package net.exmo.rough_blade.mixin.compat;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.NoRenderParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({FireworkParticles.Starter.class})
public abstract class ParticleFix extends NoRenderParticle {
    protected ParticleFix(ClientLevel p_107149_, double p_107150_, double p_107151_, double p_107152_) {
        super(p_107149_, p_107150_, p_107151_, p_107152_);
    }
    @Inject(
            method = "createParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/FireworkParticles$SparkParticle;setTrail(Z)V",
                    shift = At.Shift.BEFORE // 在 setTrail 调用前插入
            ),

            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onBeforeSetTrail(
            double p_106768_, double p_106769_, double p_106770_, double p_106771_, double p_106772_, double p_106773_, int[] p_106774_, int[] p_106775_, boolean p_106776_, boolean p_106777_, CallbackInfo ci, FireworkParticles.SparkParticle fireworkparticles$sparkparticle
            // 捕获局部变量
    ) {
        if (fireworkparticles$sparkparticle == null) {
            // 如果粒子为 null，跳过后续逻辑
            ci.cancel();
            return;
        }


    }
}

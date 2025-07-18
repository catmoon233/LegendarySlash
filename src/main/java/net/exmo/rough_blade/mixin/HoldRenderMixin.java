package net.exmo.rough_blade.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.content.client.LSClientData;
import net.exmo.rough_blade.network.LSVARB;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState.getChargeEffect;
import static mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState.renderOverrided;
import static net.exmo.rough_blade.Rough_blade.MODID;

@Mixin(BladeRenderState.class)
public class HoldRenderMixin {
    @Inject(method = "renderChargeEffect", at = @At("HEAD"), cancellable = true,remap = false)
    private static void renderChargeEffect(ItemStack stack, float f, WavefrontObject model, String target, ResourceLocation texture, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CallbackInfo ci) {
        Player rdplayer = LSClientData.Rdplayer;
        if (!Config.POWER.get())return;
        if (rdplayer==null)return;
        LSVARB.PlayerVariables playerVariables = rdplayer.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
        if (playerVariables==null)return;
        int elapsedTime = ((int) playerVariables.playerSimpleVars.get(1).getValue());

        if (elapsedTime >=23 && elapsedTime < 25) {

            renderOverrided(stack, model, target, new ResourceLocation(MODID, "textures/label/charge1_5.png"), matrixStackIn, bufferIn, packedLightIn, (loc) -> getChargeEffect(loc, f * 0.1F % 1.0F, f * 0.01F % 1.0F), false);
            ci.cancel();

        }else
        if (elapsedTime >=25) {

                renderOverrided(stack, model, target, new ResourceLocation(MODID, "textures/label/charge2.png"), matrixStackIn, bufferIn, packedLightIn, (loc) -> getChargeEffect(loc, f * 0.1F % 1.0F, f * 0.01F % 1.0F), false);
                ci.cancel();

        }

    }
}

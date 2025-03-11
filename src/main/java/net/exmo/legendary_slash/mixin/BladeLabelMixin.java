package net.exmo.legendary_slash.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.client.renderer.layers.LayerMainBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.legendary_slash.content.client.LSClientData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerMainBlade.class)
public abstract class BladeLabelMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public BladeLabelMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }
    @Inject(at = @At("HEAD"), method = "render*",remap = false)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float v, float v1, float v2, float v3, float v4, float v5, CallbackInfo ci) {
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            ItemStack itemstack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemstack.getItem() instanceof ItemSlashBlade) {
                LSClientData.Rdplayer = (net.minecraft.world.entity.player.Player) entity;
            }

        }


    }
}

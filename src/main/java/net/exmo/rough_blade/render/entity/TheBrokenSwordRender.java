package net.exmo.rough_blade.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.shineArt.arts.TheBrokenSword;
import net.exmo.rough_blade.entity.EntityStormSwordsPlus;
import net.exmo.rough_blade.entity.SummonedSwordPlus;
import net.exmo.rough_blade.entity.TheBreakSwordPlus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TheBrokenSwordRender<T extends TheBreakSwordPlus> extends EntityRenderer<T> {
    @Nullable
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getTextureLoc();
    }

    public TheBrokenSwordRender(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn,
                       int packedLightIn) {

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            Entity hits = entity.getOwner();
            boolean hasHitEntity = hits != null;

            if (hasHitEntity) {
                matrixStack
                        .mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, hits.yRotO, hits.getYRot()) ));
                matrixStack.mulPose(Axis.YN.rotationDegrees(entity.getOffsetYaw()));
            }

            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));

            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRoll()));

            //    float scale = 0.0075f;
            float scale = Math.min(0.006f,entity.tickCount*0.0003f);
            matrixStack.scale(scale, scale, scale);
           // matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));


            float rotationAngle = (entity.tickCount + partialTicks) * 3.0F; // 1.0F 是旋转速度系数
            matrixStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));

            // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
            WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/sss.obj"));
            BladeRenderState.setCol(entity.getColor(), false);
            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "sss", getTextureLocation(entity),
                    matrixStack, bufferIn, packedLightIn);
        }
        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            Entity hits = entity.getOwner();
            boolean hasHitEntity = hits != null;

            if (hasHitEntity) {
                matrixStack
                        .mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, entity.getOwner().yRotO,entity.getOwner().getYRot()) - 90));
            }



            matrixStack.mulPose(Axis.YN.rotationDegrees(entity.getOffsetYaw()));

            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));

            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRoll()));

            //    float scale = 0.0075f;
            matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));

            //    float scale = 0.0075f;
            float scale = 0.006f;
            matrixStack.scale(scale, scale, scale);
           // matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));



//            matrixStack.mulPose(Axis.YP.rotationDegrees(entity.getOwner().getYRot()));
//            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getOwner().getXRot()));
            // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
            WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/sstar.obj"));
            BladeRenderState.setCol(entity.getColor(), false);
            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "ss", getTextureLocation(entity),
                    matrixStack, bufferIn, packedLightIn);
        }
    }
}
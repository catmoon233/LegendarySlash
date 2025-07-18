package net.exmo.rough_blade.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.entity.FaZhenBase;
import net.exmo.rough_blade.entity.TheBreakSwordPlus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;

public class FaZhenBaseRender<T extends FaZhenBase> extends EntityRenderer<T> {
    @Nullable
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getTextureLoc();
    }

    public FaZhenBaseRender(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn,
                       int packedLightIn) {

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            // 修改旋转速度计算：基于tickCount和scale实现曲速效果
            // 当scale较小时（初始阶段）转速快，随着scale增大转速逐渐变慢
            float scale = Math.min(0.01f,entity.tickCount*0.0005f);
            float speedFactor = 3.0f * (1.0f - scale / 0.01f) +1f; // 通过scale比例调整速度系数
            float rotationAngle = (entity.tickCount ) * speedFactor * 2.0f;
            Entity hits = entity.getHitEntity();
            boolean hasHitEntity = hits != null;

            if (hasHitEntity) {
                matrixStack
                        .mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, hits.yRotO, hits.getYRot()) - 90 ));
                matrixStack.mulPose(Axis.YN.rotationDegrees(entity.getOffsetYaw()));
            } else {
                matrixStack.mulPose(
                        Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()) ));
            }

            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));

            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRoll()));

//            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRoll()));

            //    float scale = 0.0075f;

            matrixStack.scale(scale, scale, scale);



           // matrixStack.mulPose(Axis.ZP.rotationDegrees());

            // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
            WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/zhenv.obj"));
            BladeRenderState.setCol(entity.getColor(), false);
            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "Circle", getTextureLocation(entity),
                    matrixStack, bufferIn, packedLightIn);
        }


    }
}
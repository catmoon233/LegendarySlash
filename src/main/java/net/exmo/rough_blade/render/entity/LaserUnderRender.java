package net.exmo.rough_blade.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.entity.FaZhenBase;
import net.exmo.rough_blade.entity.LaserUnder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.awt.*;

import static net.exmo.rough_blade.entity.FaZhenUnder.getTextureLoc;

public class LaserUnderRender<T extends LaserUnder> extends EntityRenderer<T> {
    @Nullable
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return getTextureLoc();
    }

    public LaserUnderRender(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn,
                       int packedLightIn) {

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            // 修改旋转速度计算：基于tickCount和scale实现曲速效果
            // 当scale较小时（初始阶段）转速快，随着scale增大转速逐渐变慢
            WavefrontObject model ;
            int tickCount = entity.getEntityData().get(LaserUnder.TICK);
            if (tickCount>80){
                // 修改为平方根增长曲线：初始增长快，后期增速逐渐放缓
                float sas = (float) Math.max(5 + 2.0f * Math.sqrt(tickCount - 80), 5);
                matrixStack.scale(sas, sas,sas);
                 model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/slashdim.obj"));
                BladeRenderState.setCol(Color.WHITE.getRGB(), false);
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "base", getTextureLocation(entity),
                        matrixStack, bufferIn, packedLightIn);
            }else {
                // 减小缩放系数：0.5f -> 0.3f 降低生长速度
                float scale = Math.min(0.01f, tickCount *0.3f);
                float speedFactor = 3.0f * (1.0f - scale / 0.01f) +1f;
                float rotationAngle = tickCount * speedFactor * 2.0f;
                matrixStack.mulPose(
                        Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()) +rotationAngle));

                matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));

//            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRoll()));

                //    float scale = 0.0075f;

                matrixStack.scale(scale*10f, scale* entity.getEntityData().get(LaserUnder.YSclae),scale *10f);
                // 新增向下位移：根据Y轴缩放比例动态调整下移距离
                // 当Y缩放越大时下移幅度越大，形成向下延伸效果
                // 减小下移幅度系数：50f -> 30f 降低垂直移动幅度
                matrixStack.translate(0, -scale * entity.getEntityData().get(LaserUnder.YSclae) * 30f, 0);

                // matrixStack.mulPose(Axis.ZP.rotationDegrees());

                // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
                 model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/huan.obj"));
                BladeRenderState.setCol(Color.WHITE.getRGB(), false);
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "huan", getTextureLocation(entity),
                        matrixStack, bufferIn, packedLightIn);
            }
        }


    }
}
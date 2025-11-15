package net.exmo.rough_blade.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class EmptyRender<T extends Projectile> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = SlashBlade.prefix("model/util/ss.png");
    private static final ResourceLocation MODEL = SlashBlade.prefix("model/util/drive.obj");

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

    public EmptyRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn,
                       int packedLightIn) {

//        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
//            float lifetime = entity.getLifetime();
//            double deathTime = lifetime;
//            double baseAlpha = (Math.min(deathTime, Math.max(0, (lifetime - (entity.tickCount))))
//                    / deathTime);
//            baseAlpha = Math.max(0, -Math.pow(baseAlpha - 1, 4.0) + 0.75);
//
//            matrixStack.mulPose(
//                    Axis.YP.rotationDegrees(Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
//            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));
//            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getRotationRoll()-90F));
//
//            //  float scale = 0.015f;
//            float scale = entity.getBaseSize();
//            matrixStack.scale(scale, scale, scale);
//            matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
//            int color = entity.getColor() & 0xFFFFFF;
//            int alpha = ((0xFF & (int) (0xFF * baseAlpha)) << 24);
//            WavefrontObject model = BladeModelManager.getInstance().getModel(MODEL);
//
//            BladeRenderState.setCol(color | alpha);
//            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "base", TEXTURE, matrixStack, bufferIn,
//                    packedLightIn);
//        }
    }
}
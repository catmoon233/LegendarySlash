package net.exmo.rough_blade.render.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.exmo.rough_blade.Rough_blade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class MingLiLEffectayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation Ming_Li_Tex = new ResourceLocation(Rough_blade.MODID, "textures/model/ming_li.png");
    private final AngelWingsModel<T> angelWingsModel;

    public MingLiLEffectayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.angelWingsModel = new AngelWingsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(AngelWingsModel.ANGEL_WINGS_LAYER));
    }
    public static float[] fixedAngles = {0.0F, 36.0F, 72.0F, 108.0F, 144.0F,
            180.0F, 216.0F, 252.0F, 288.0F, 324.0F};
    public void render(PoseStack matrixStack, MultiBufferSource Buffer, int PackedLight, T entity, float LimbSwing, float LimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (shouldRender(entity)) {

            MobEffectInstance effect = entity.getEffect(Rough_blade.effectAbout.MingYunEffect.get());
            int amplifier = effect.getAmplifier();
            if (amplifier > 0) {
                // 改为渲染多根线，最多10根，使用固定角度数组（枚举方式）
                int lineCount = Math.min(amplifier, 10);
                // 固定角度数组（每36度一根线）


                for (int i = 0; i < lineCount; i++) {
                    try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
                        float scale = 1f;
                        matrixStack.scale(scale, scale, scale);
                        // 使用固定角度数组中的角度
                        matrixStack.mulPose(Axis.ZP.rotationDegrees(fixedAngles[i]));

                        WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/xian.obj"));
                        BladeRenderState.setCol(Color.RED.getRGB(), false);
                        BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "xian", defaultTexture,
                                matrixStack, Buffer, PackedLight);
                    }
                }
            }
        }
    }

    public boolean shouldRender(T entity) {
        //irons_spellbooks.LOGGER.debug("AngelWingsLayer.shouldRender {} {}", entity.getName().getString(), entity.getActiveEffects().stream().map(x -> x.getEffect().getDisplayName().getString()).collect(Collectors.toSet()));
        return entity.hasEffect(Rough_blade.effectAbout.MingYunEffect.get());
    }

    public static final ResourceLocation defaultTexture = new ResourceLocation("slashblade:model/util/ss.png");
    public static ResourceLocation getTextureLoc() {
        return (defaultTexture);
    }
}

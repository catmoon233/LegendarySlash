package net.exmo.rough_blade.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.entity.FaZhenBase;
import net.exmo.rough_blade.entity.FaZhenUnder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;

public class FaZhenUnderRender<T extends FaZhenUnder> extends EntityRenderer<T> {

    // 模型资源
    private static final ResourceLocation CORE_CIRCLE = Rough_blade.prefix("models/zhen.obj");
    private static final ResourceLocation INNER_RING = Rough_blade.prefix("models/huan.obj");
    private static final ResourceLocation OUTER_RING = Rough_blade.prefix("models/huan.obj");
    private static final ResourceLocation RUNE_PATTERNS = Rough_blade.prefix("models/huan.obj");

    // 动画阶段常量
    private static final int CHARGE_DURATION = 540; // 总蓄力时间
    private static final int PHASE_RISE = 300;       // 上升阶段持续时间
    private static final int PHASE_EXPAND = 160;      // 扩展阶段
    private static final int PHASE_ACTIVATE = 80;    // 激活阶段

    public FaZhenUnderRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack,
                       MultiBufferSource buffer, int packedLight) {

        int tickCount = entity.tickCount;
        float totalProgress = Mth.clamp(tickCount / (float)CHARGE_DURATION, 0, 1);

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            // 基础旋转
            matrixStack.mulPose(Axis.YP.rotationDegrees(
                    Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())));

            // 动画主系统
            renderRisingPhase(entity, matrixStack, buffer, packedLight, tickCount, partialTicks);
            renderExpansionPhase(entity, matrixStack, buffer, packedLight, tickCount, partialTicks);
            renderActivationPhase(entity, matrixStack, buffer, packedLight, tickCount, partialTicks);
            renderFinalCharge(entity, matrixStack, buffer, packedLight, tickCount, partialTicks);
        }
    }

    // 第一阶段：多重法阵上升
    private void renderRisingPhase(T entity, PoseStack matrixStack, MultiBufferSource buffer,
                                   int packedLight, int tickCount, float partialTicks) {

        float phaseProgress = Mth.clamp(tickCount / (float)PHASE_RISE, 0, 1);

        // 四层法阵上升动画
        for(int i = 0; i < 4; i++) {
            matrixStack.pushPose();
            try {
                // 层级偏移计算（调整缩放参数实现更大层间差异和整体缩小）
                float layerHeight = Mth.lerp(phaseProgress, -0.5f + i*0.15f, 2.0f + i*1.2f);
                float scale = Mth.lerp(phaseProgress, 0.01f, 0.8f + i*0.6f); // 修改基础值从1.2->0.8，层间系数从0.4->0.6
                float alpha = Mth.lerp(phaseProgress, 0.3f, 1.0f);

                matrixStack.translate(0, layerHeight*50, 0);
                matrixStack.scale(scale, scale, scale);

                // 调整旋转速度（降低转速）
                float rotation = (tickCount+partialTicks) *i; // 原5+2i改为3+i
                matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));

                // 渲染核心法阵
                WavefrontObject model = BladeModelManager.getInstance().getModel(CORE_CIRCLE);
                BladeRenderState.setCol(getLayerColor(i, phaseProgress).getRGB(), alpha < 0.9f);
                BladeRenderState.renderOverridedLuminous(
                        ItemStack.EMPTY, model, "Circle", getTextureLocation(entity),
                        matrixStack, buffer, packedLight);

            } finally {
                matrixStack.popPose();
            }
        }
    }

    // 第二阶段：环形结构扩展
    private void renderExpansionPhase(T entity, PoseStack matrixStack, MultiBufferSource buffer,
                                      int packedLight, int tickCount, float partialTicks) {

        if (tickCount < PHASE_RISE) return;
        float phaseProgress = Mth.clamp((tickCount - PHASE_RISE) / (float) PHASE_EXPAND, 0, 1);

        // 内环和外环动画
        for (int a = 0; a < 3; a++) {
            for (int i = 0; i < 2; i++) {
                matrixStack.pushPose();
                try {
                    // 动态缩
                    float layerHeight = Mth.lerp(phaseProgress, -0.5f + i*0.15f, 2.0f + i*1.2f);
                    float scale = Mth.lerp(phaseProgress, 0.5f, 3.0f + i * 1.2f) * a *0.6f;
                    float rotationSpeed = i == 0 ? -1.5f : 2.0f;
                    float currentRotation = (tickCount * rotationSpeed) * partialTicks;

                    matrixStack.mulPose(Axis.YP.rotationDegrees(currentRotation));
                    matrixStack.scale(scale, scale * 0.5f, scale);
                    matrixStack.translate(0, layerHeight * 50 * (3-a)*0.6f, 0);

                    // 颜色脉冲
                    Color pulseColor = Color.getHSBColor(
                            (tickCount % 40) / 40f,
                            0.8f,
                            Mth.sin((tickCount + i * 20) * 0.1f) * 0.5f + 0.5f
                    );

                    BladeRenderState.setCol(pulseColor.getRGB(), true);
                    WavefrontObject ringModel = BladeModelManager.getInstance().getModel(
                            i == 0 ? INNER_RING : OUTER_RING);

                    BladeRenderState.renderOverridedLuminous(
                            ItemStack.EMPTY, ringModel, "huan", getTextureLocation(entity),
                            matrixStack, buffer, packedLight);

                } finally {
                    matrixStack.popPose();
                }
            }
        }
    }

    // 第三阶段：符文激活
    private void renderActivationPhase(T entity, PoseStack matrixStack, MultiBufferSource buffer,
                                       int packedLight, int tickCount, float partialTicks) {

        if(tickCount < PHASE_RISE + PHASE_EXPAND) return;
        float phaseProgress = Mth.clamp(
                (tickCount - PHASE_RISE - PHASE_EXPAND) / (float)PHASE_ACTIVATE, 0, 1);

        matrixStack.pushPose();
        try {
            // 整体缩放
            float globalScale = Mth.lerp(phaseProgress, 1.0f, 2.5f);
            matrixStack.scale(globalScale, globalScale * 0.2f, globalScale);

            // 符文旋转动画
            float rotationSpeed = Mth.sin(tickCount * 0.1f) * 2.0f;
            matrixStack.mulPose(Axis.YP.rotationDegrees(tickCount * 3.0f));
            matrixStack.mulPose(Axis.XP.rotationDegrees(rotationSpeed));

            // 符文逐一亮起
            WavefrontObject runeModel = BladeModelManager.getInstance().getModel(RUNE_PATTERNS);
            for(int i = 0; i < 8; i++) {
                matrixStack.pushPose();
                float angle = 45 * i + tickCount * 2.0f;
                matrixStack.mulPose(Axis.YP.rotationDegrees(angle));
                matrixStack.translate(0, 0, 1.5f);

                float runeAlpha = Mth.clamp((tickCount - i*5) / 10f, 0, 1);
                BladeRenderState.setCol(getRuneColor(i), runeAlpha > 0.9f);
                BladeRenderState.renderOverridedLuminous(
                        ItemStack.EMPTY, runeModel, "huan", getTextureLocation(entity),
                        matrixStack, buffer, packedLight);

                matrixStack.popPose();
            }
        } finally {
            matrixStack.popPose();
        }
    }

    // 最终蓄力阶段
    private void renderFinalCharge(T entity, PoseStack matrixStack, MultiBufferSource buffer,
                                   int packedLight, int tickCount, float partialTicks) {

        if(tickCount < CHARGE_DURATION - 40) return;
        float chargeProgress = (tickCount - (CHARGE_DURATION - 40)) / 40f;

        matrixStack.pushPose();
        try {
            // 能量聚集效果
            float pulseScale = Mth.lerp(Mth.sin(chargeProgress * Mth.PI), 1.0f, 1.5f);
            matrixStack.scale(pulseScale, pulseScale * 0.3f, pulseScale);

            // 中心能量球
            BladeRenderState.setCol(Color.WHITE.getRGB(), false);
            WavefrontObject coreModel = BladeModelManager.getInstance().getModel(OUTER_RING);
            BladeRenderState.renderOverridedLuminous(
                    ItemStack.EMPTY, coreModel, "huan", getTextureLocation(entity),
                    matrixStack, buffer, packedLight);

            // 修改后的四个竖直旋转的核心法阵
            for(int i = 0; i < 4; i++) {
                matrixStack.pushPose();
                try {
                    // 统一旋转轴点（先平移后旋转）
                    matrixStack.translate(0, 0.5f, 0); // 调整轴心高度
                    
                    // 环绕分布（每个法阵间隔90度）
                    float baseAngle = tickCount * 3.0f; // 基础旋转速度
                    matrixStack.mulPose(Axis.YP.rotationDegrees(90 * i + baseAngle));
                    
                    // 位置偏移（沿法阵半径分布）
                    float radius = 18f * Mth.lerp(chargeProgress, 0.5f, 1.0f);
                    matrixStack.translate(0, 0, radius);
                    
                    // 竖直方向旋转（调整X轴旋转角度为80度形成倾斜）
                    matrixStack.mulPose(Axis.XP.rotationDegrees(80));
                    
                    // 动态缩放（调整缩放曲线）
                    float scale = Mth.lerp(chargeProgress, 0.3f, 1.1f)/1.2f; // 改为线性插值实现稳定增长到最大值
                    matrixStack.scale(scale * 0.2f, scale * 0.2f, scale * 0.2f);
                    
                    // 添加自转动画
                    matrixStack.mulPose(Axis.YP.rotationDegrees(tickCount * 5.0f * (i % 2 == 0 ? 1 : -1)));
                    
                    // 透明度控制（使用缓动函数）
                    float alpha = Mth.clamp(Mth.sqrt(chargeProgress) * 1.2f, 0, 1);
                    
                    // 设置法阵颜色（使用层级颜色）
                    BladeRenderState.setCol(getLayerColor(i, 1.0f).getRGB(), alpha < 0.9f);
                    
                    // 渲染核心法阵
                    WavefrontObject model = BladeModelManager.getInstance().getModel(CORE_CIRCLE);
                    BladeRenderState.renderOverridedLuminous(
                            ItemStack.EMPTY, model, "Circle", getTextureLocation(entity),
                            matrixStack, buffer, packedLight);
                } finally {
                    matrixStack.popPose();
                }
            }

            // 添加粒子效果（需配合粒子系统）
            if(tickCount % 2 == 0) {
                spawnChargeParticles(entity, matrixStack, chargeProgress);
            }
        } finally {
            matrixStack.popPose();
        }
    }

    // 辅助方法：获取层级颜色（调整颜色参数适应4层）
    private Color getLayerColor(int layer, float progress) {
        float hue = 0.6f + layer * 0.07f; // 缩小色相间隔
        float saturation = 0.8f - progress * 0.3f;
        float brightness = 0.5f + progress * 0.5f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    // 辅助方法：获取符文颜色
    private int getRuneColor(int index) {
        float hue = (index % 8) / 8f;
        return Color.getHSBColor(hue, 0.9f, 1.0f).getRGB();
    }

    // 辅助方法：生成蓄力粒子
    private void spawnChargeParticles(T entity, PoseStack matrixStack, float progress) {
        // 实现粒子生成逻辑（需要配合粒子系统）
        // 示例：在法阵周围生成旋转粒子
        for(int i = 0; i < 36; i++) {
            double angle = Math.toRadians(i * 10 + entity.tickCount * 5);
            double radius = 3.0 * progress;
            double x = entity.getX() + radius * Math.cos(angle);
            double z = entity.getZ() + radius * Math.sin(angle);
            double y = entity.getY() + 2.5;

            // 生成粒子实体
    entity.level().addParticle(
                    ParticleTypes.END_ROD,
                    x, y, z,
                    0.0, 0.0, 0.0
            );
        }
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getTextureLoc();
    }
}
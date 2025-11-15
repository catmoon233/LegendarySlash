package net.exmo.rough_blade.render.other;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class MingLiLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation Ming_Li_Tex = new ResourceLocation(Rough_blade.MODID, "textures/model/ming_li.png");
    private final AngelWingsModel<T> angelWingsModel;

    public MingLiLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.angelWingsModel = new AngelWingsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(AngelWingsModel.ANGEL_WINGS_LAYER));
    }
    /**
     * 返回玩家视线方向上最远的非空气方块的位置。
     * 如果未找到非空气方块，则返回最大距离时的方块位置（可能为空气）。
     *
     * @param entity	 用于确定视线方向和位置的玩家实体。
     * @param maxDistance 视线追踪的最大距离。
     * @return 最远非空气方块的位置，如果未找到则返回最大距离时的位置。
     */
    public static Vec3 findFarthestNonAirBlock(LivingEntity entity, double maxDistance) {
        Level world = entity.level();
        Vec3 eyePosition = entity.getEyePosition(1.0F); // 当前时刻玩家眼睛的位置
        Vec3 lookVec = entity.getViewVector(1.0F).scale(maxDistance); // 玩家视线方向的单位向量，乘以最大距离
        Vec3 endPos = eyePosition.add(lookVec); // 眼睛位置加上视线向量得到终点位置



        // 如果没有找到非空气方块，则返回最大距离时的方块位置
        return endPos;
    }
    public void render(PoseStack matrixStack, MultiBufferSource Buffer, int PackedLight, T entity, float LimbSwing, float LimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (shouldRender(entity)) {
            Vec3 eyePosition = findFarthestNonAirBlock(entity,-30);
            var move = entity.getEyePosition().subtract(eyePosition).scale(1);
            try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {

                matrixStack.mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, entity.getYHeadRot(), entity.yHeadRotO)));
               // matrixStack.mulPose(Axis.XN.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));



                //    float scale = 0.0075f;
                float scale = Math.min(0.005f,entity.tickCount*0.0003f);
                matrixStack.scale(scale, scale, scale);
                // matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));


                float rotationAngle = (entity.tickCount + partialTicks) * 3.0F; // 1.0F 是旋转速度系数
                matrixStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));
                matrixStack.translate(move.x, move.y, move.z);
                // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
                WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/sss.obj"));
                BladeRenderState.setCol(Color.RED.getRGB(), false);
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "sss", getTextureLocation(entity),
                        matrixStack, Buffer, PackedLight);
            }
//            try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
//
//
//                matrixStack.mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, entity.yRotO,entity.getYRot()) - 90));
//                matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot())));
//
//
//
//                //    float scale = 0.0075f;
//                matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
//
//                //    float scale = 0.0075f;
//                float scale = 0.005f;
//                matrixStack.scale(scale, scale, scale);
//                // matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
//                matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));
//
//                matrixStack.translate(move.x, move.y, move.z);
////            matrixStack.mulPose(Axis.YP.rotationDegrees(entity.getOwner().getYRot()));
////            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getOwner().getXRot()));
//                // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);
//                WavefrontObject model = BladeModelManager.getInstance().getModel(Rough_blade.prefix("models/sstar.obj"));
//                BladeRenderState.setCol(Color.red.getRGB(), false);
//                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "ss", getTextureLocation(entity),
//                        matrixStack, Buffer, PackedLight);
//            }
        }
        }


    public boolean shouldRender(T entity) {
        //irons_spellbooks.LOGGER.debug("AngelWingsLayer.shouldRender {} {}", entity.getName().getString(), entity.getActiveEffects().stream().map(x -> x.getEffect().getDisplayName().getString()).collect(Collectors.toSet()));
        return entity.getMainHandItem().getTag() !=null &&entity.getMainHandItem().getItem() instanceof ItemSlashBlade &&entity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(entity.getMainHandItem())).hasSpecialEffect(RBSpecialEffectRegistry.MingLi.getId());
    }

    public ResourceLocation getAngelWingsTexture(T entity) {
        return Ming_Li_Tex;
    }
}

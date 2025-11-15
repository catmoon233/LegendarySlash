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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
public class StarryGalaxyLiLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    //private static final ResourceLocation Ming_Li_Tex = new ResourceLocation(Rough_blade.MODID, "textures/model/ming_li.png");
    private final AngelWingsModel<T> angelWingsModel;
    public static final ResourceLocation defaultTexture = new ResourceLocation("slashblade:model/util/ss.png");
    public StarryGalaxyLiLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.angelWingsModel = new AngelWingsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(AngelWingsModel.ANGEL_WINGS_LAYER));
    }
    public static final ResourceLocation prefix = Rough_blade.prefix("models/star_ring.obj");
    private Vec3 calculateBehindPlayer(Player player, double distance) {
        float yaw = player.getYHeadRot();
        float pitch = player.getXRot();

        return player.position().add(
                -Math.sin(Math.toRadians(yaw)) * distance,
                -Math.sin(Math.toRadians(pitch)) * distance * 0.5,
                Math.cos(Math.toRadians(yaw)) * distance
        );
    }
    public void render(PoseStack matrixStack, MultiBufferSource Buffer, int PackedLight, T entity, float LimbSwing, float LimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (shouldRender(entity)) {
           // Vec3 eyePosition = calculateBehindPlayer((Player) entity,-50f);
           // var move = entity.getEyePosition().subtract(eyePosition).scale(1);
            try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {

//                if (Screen.hasShiftDown()) {
//                    matrixStack.mulPose(Axis.XN.rotationDegrees(Mth.rotLerp(partialTicks, entity.yBodyRot, entity.yBodyRotO)));
//                    //   matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRotO)));
//                }else if (Screen.hasAltDown()){
//                    matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.rotLerp(partialTicks, entity.yBodyRot, entity.yBodyRotO)));
//
//               }else if (Screen.hasControlDown()){
//                    matrixStack.mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, entity.yBodyRot, entity.yBodyRotO)));
//
//                }
               // matrixStack.mulPose(Axis.YN.rotationDegrees(Mth.rotLerp(partialTicks, entity.yHeadRot, entity.yHeadRotO)));

                //    float scale = 0.0075f;

                // matrixStack.mulPose(Axis.YP.rotationDegrees(90.0F));


                float rotationAngle = (entity.tickCount + partialTicks) * 3.0F; // 1.0F 是旋转速度系数

                matrixStack.pushPose();
                float scale = Math.min(0.02f,entity.tickCount*0.0003f);
                matrixStack.scale(scale, scale, scale);
            //    matrixStack.translate(move.x, move.y, move.z);
                matrixStack.translate(0,23,0);
                matrixStack.mulPose(Axis.YP.rotationDegrees(rotationAngle));
                // matrixStack.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);

                WavefrontObject model = BladeModelManager.getInstance().getModel(prefix);
                BladeRenderState.setCol(Color.BLUE.getRGB(), false);
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "ss", getTextureLocation(entity),
                        matrixStack, Buffer, PackedLight);
                matrixStack.popPose();
            }

        }
        }


    public boolean shouldRender(T entity) {
        return entity.getMainHandItem().getTag() !=null &&entity.getMainHandItem().getItem() instanceof ItemSlashBlade &&entity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(entity.getMainHandItem())).hasSpecialEffect(RBSpecialEffectRegistry.StarrySky.getId());
    }


}

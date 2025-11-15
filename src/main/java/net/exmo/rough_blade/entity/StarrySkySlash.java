package net.exmo.rough_blade.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.hurts.octostudios.octolib.modules.particles.OctoRenderManager;
import it.hurts.octostudios.octolib.util.ColorUtils;
import it.hurts.octostudios.octolib.util.TesselatorUtils;
import it.hurts.octostudios.octolib.util.VectorUtils;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.slasharts.Drive;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data.TrailConfigData;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.misc.ITrailConfigProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Unique;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.exmo.rough_blade.content.specialEffects.StarrySkySE.isPower;

public class StarrySkySlash extends EntityDrive implements ITrailConfigProvider {
    public StarrySkySlash(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);

    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity owner = getOwner();
        if (owner ==null) {
            return;
        }
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity){
            if (livingEntity.isAlive() &&Math.random() <=0.5){

                livingEntity.getPersistentData().putString("star_imprint_uuid", owner.getStringUUID());
                if (!livingEntity.hasEffect(Rough_blade.effectAbout.StarImprintEffect.get())) {
                    livingEntity.addEffect(new MobEffectInstance(Rough_blade.effectAbout.StarImprintEffect.get(), 200, 0, true, true));
                }else {
                    int amplifier = livingEntity.getEffect(Rough_blade.effectAbout.StarImprintEffect.get()).getAmplifier();
                    livingEntity.addEffect(new MobEffectInstance(Rough_blade.effectAbout.StarImprintEffect.get(), 200, amplifier + 1, true, true));
                }
            }
        }
    }

    public static StarrySkySlash createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new StarrySkySlash(RBEntityRegistry.STARRY_SKY_SLASH, worldIn);
    }

    @Unique
    private TrailConfigData perception$trailData = new TrailConfigData();

    @Override
    public TrailConfigData getTrailConfigData() {
        return this.perception$trailData;
    }

    @Override
    public void setTrailConfigData(TrailConfigData data) {
        this.perception$trailData = data;
    }

    @Override
    public int getTrailMaxLength() {
        return getTrailConfigData().getMaxPoints();
    }

    @Override
    public int getTrailUpdateFrequency() {
        return getTrailConfigData().getUpdateFrequency();
    }

    @Override
    public double getTrailScale() {
        return getTrailConfigData().getSize();
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel) {
            if (Math.random() < 0.2) {
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                        getX(), getY() + 1, getZ(),
                        1,
                        0.05f, 0.05f, 0.05f,
                        0.05f

                );
            }
        }
    }

    @Override
    public Vec3 getTrailPosition(float partialTicks) {
        var entity = (Entity) (Object) this;

        var data = getTrailConfigData();
        var offset = data.getPositionOffset();

        var entityPosition = (entity.tickCount > 1 ? entity.getPosition(partialTicks) : entity.position()).add(entity.getDeltaMovement().normalize().scale(-data.getMotionShift())).add(offset.x(), offset.y(), offset.z());

        var player = entity.getCommandSenderWorld().getNearestPlayer(entity, getTrailRenderDistance());

        if (player == null)
            return entityPosition;

        var playerPosition = player.getEyePosition(partialTicks);

        entityPosition = entityPosition.add(entityPosition.subtract(playerPosition).normalize().scale(data.getBackwardShift()));

        return entityPosition;
    }

    @Override
    public boolean isTrailGrowing() {
        var entity = (Entity) (Object) this;

        return entity.getDeltaMovement().length() >= getTrailConfigData().getMinSpeed();
    }

    @Override
    public boolean isTrailAlive() {
        var entity = (Entity) (Object) this;

        return entity.isAlive();
    }

    @Override
    public int getTrailFadeInColor() {
        return (int) Long.parseLong(getTrailConfigData().getFadeInColor().replace("#", ""), 16);
    }

    @Override
    public int getTrailFadeOutColor() {
        return (int) Long.parseLong(getTrailConfigData().getFadeOutColor().replace("#", ""), 16);
    }

    @Override
    public void renderTrail(float pTicks, PoseStack poseStack, MultiBufferSource bufferSourceList) {
        this.partialTicks = pTicks;
        ITrailConfigProvider.super.renderTrail(pTicks, poseStack, bufferSourceList);
    }

    private float partialTicks;

    @Override
    public void draw3dTrail(List<Vec3> partialPoses, PoseStack poseStack, MultiBufferSource bufferSourceList) {
        if (partialPoses.size() < 2) return;


        float verticalScale = 1.5f; // 垂直缩放
        float horizontalScale = 0.8f; // 水平缩放
        Vec3 rotationAxis = new Vec3(0, 0, 0); // 旋转轴 (Y轴)
        double rotationAngle = this.getRotationRoll(); // 旋转角度（弧度）
        Vec3 positionOffset = new Vec3(0, 0, 0); // 位置偏移

        Vec3[][] crossVecs = new Vec3[partialPoses.size()][4];
        poseStack.pushPose();


        boolean test = isPower.test(this.getRotationRoll());


//        if ( test){
//            positionOffset = positionOffset.add(-4,0,0);
//        }
        Vec3 finalPositionOffset = positionOffset;
        partialPoses = partialPoses.stream().map(
                a-> a.subtract(finalPositionOffset)
        ).toList();
        for(int i = 1; i < partialPoses.size(); ++i) {
            Vec3 pos1 = partialPoses.get(i - 1);
            Vec3 pos2 = partialPoses.get(i);
            Vec3 vec1 = pos2.subtract(pos1);
            Vec3 vec1n = vec1.normalize();

            Vec3 rightVec = Math.abs(vec1n.y) > 0.9 ?
                    vec1.cross(new Vec3(1, 0, 0)).normalize() :
                    vec1.cross(VectorUtils.Y_VEC).normalize();

            Vec3 upVec = rightVec.cross(vec1n).normalize();

            if (rotationAngle != 0.0) {
                rightVec = VectorUtils.rotate(rightVec, rotationAxis, rotationAngle).normalize();
                upVec = VectorUtils.rotate(upVec, rotationAxis, rotationAngle).normalize();
            }

            double len = (double)(1.0F - (float)(i - 1) / (float)partialPoses.size());
            double scale = this.getTrailScale() * len *(test ? 4f : 1f);

//            if (test) {
//                crossVecs[i - 1][0] = rightVec.scale(scale * horizontalScale)  // 右点
//                        .add(upVec.scale(thickness * verticalScale));
//                crossVecs[i - 1][1] = rightVec.scale(-scale * horizontalScale * 0.2) // 左点
//                        .add(upVec.scale(thickness * verticalScale));
//                crossVecs[i - 1][2] = rightVec.scale(-scale * horizontalScale * 0.2) // 左点
//                        .add(upVec.scale(-thickness * verticalScale));
//                crossVecs[i - 1][3] = rightVec.scale(scale * horizontalScale)  // 右点
//                        .add(upVec.scale(-thickness * verticalScale));
//            } else {
                crossVecs[i - 1][0] = upVec.scale(scale * verticalScale)  // 上点
                        .add(rightVec.scale(thickness * horizontalScale));
                crossVecs[i - 1][1] = upVec.scale(-scale * verticalScale * 0.2) // 下点
                        .add(rightVec.scale(thickness * horizontalScale));
                crossVecs[i - 1][2] = upVec.scale(-scale * verticalScale * 0.2) // 下点
                        .add(rightVec.scale(-thickness * horizontalScale));
                crossVecs[i - 1][3] = upVec.scale(scale * verticalScale)  // 上点
                        .add(rightVec.scale(-thickness * horizontalScale));
  //          }
        }

        Color color1 = new Color(this.getTrailFadeInColor(), true);
        Color color2 = new Color(this.getTrailFadeOutColor(), true);

        Matrix4f matrix4f = poseStack.last().pose();

        for(int i = 0; i < partialPoses.size() - 1; ++i) {
            if (crossVecs[i][0] == null) continue;

            Color c1 = ColorUtils.blend(color1, color2, (double)((float)i / (float)(partialPoses.size() - 1)));
            Color c2 = ColorUtils.blend(color1, color2, (double)(((float)i + 1.0F) / (float)(partialPoses.size() - 1)));

            Vec3 pos_i = partialPoses.get(i);
            Vec3 pos_i1 = partialPoses.get(i + 1);


            Vec3[] currentPoints = new Vec3[4];
            for (int j = 0; j < 4; j++) {
                currentPoints[j] = pos_i.add(crossVecs[i][j]);
            }


            Vec3[] nextPoints = new Vec3[4];
            if (i < partialPoses.size() - 2 && crossVecs[i + 1][0] != null) {
                for (int j = 0; j < 4; j++) {
                    nextPoints[j] = pos_i1.add(crossVecs[i + 1][j]);
                }
            } else {

                Vec3 endDir = pos_i1.subtract(pos_i).normalize().scale(thickness * 0.5);
                for (int j = 0; j < 4; j++) {
                    nextPoints[j] = pos_i1.add(endDir);
                }
            }

            VertexConsumer tes = bufferSourceList.getBuffer(TesselatorUtils.TRAIL_RENDER_TYPE);


            drawQuadSide(tes, matrix4f, currentPoints[0], currentPoints[1], nextPoints[1], nextPoints[0], c1, c2);
            drawQuadSide(tes, matrix4f, currentPoints[1], currentPoints[2], nextPoints[2], nextPoints[1], c1, c2);
            drawQuadSide(tes, matrix4f, currentPoints[2], currentPoints[3], nextPoints[3], nextPoints[2], c1, c2);
            drawQuadSide(tes, matrix4f, currentPoints[3], currentPoints[0], nextPoints[0], nextPoints[3], c1, c2);


        }

        poseStack.popPose();
    }


    private void drawQuadSide(VertexConsumer tes, Matrix4f matrix, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4, Color c1, Color c2) {
        TesselatorUtils.drawQuadGradient(tes, matrix,
                (float)p1.x, (float)p1.y, (float)p1.z,
                (float)p2.x, (float)p2.y, (float)p2.z,
                (float)p3.x, (float)p3.y, (float)p3.z,
                (float)p4.x, (float)p4.y, (float)p4.z,
                c1, c2);
    }


    public void setRotation(double angleRadians, Vec3 axis) {
        // 设置旋转角度和轴
        this.rotationAngle = angleRadians;
        this.rotationAxis = axis.normalize();
    }

    public void setPositionOffset(Vec3 offset) {
        // 设置位置偏移
        this.positionOffset = offset;
    }

    public void setScaling(float vertical, float horizontal) {
        // 设置缩放
        this.verticalScale = vertical;
        this.horizontalScale = horizontal;
    }

    public void setThickness(float thickness) {
        // 设置厚度
        this.thickness = thickness;
    }

    private float thickness = 0.05f;
    private float verticalScale = 1.5f;
    private float horizontalScale = 0.8f;
    private Vec3 rotationAxis = new Vec3(0, 1, 0);
    private double rotationAngle = 0.0;
    private Vec3 positionOffset = new Vec3(0, 0, 0);
}
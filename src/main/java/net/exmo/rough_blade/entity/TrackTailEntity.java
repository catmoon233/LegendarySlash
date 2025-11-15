package net.exmo.rough_blade.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data.TrailConfigData;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.misc.ITrailConfigProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.spongepowered.asm.mixin.Unique;

public class TrackTailEntity extends SummonSwordPROEntity implements ITrailConfigProvider {
    @Unique
    private TrailConfigData perception$trailData = new TrailConfigData();

    public TrackTailEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public static TrackTailEntity createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new TrackTailEntity(RBEntityRegistry.TRACK_TAIL_ENTITY, worldIn);
    }

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
    public Vec3 getTrailPosition(float partialTicks) {
        var entity = (Entity) (Object) this;

        var data = getTrailConfigData();
        var offset = data.getPositionOffset();

        var entityPosition = (entity.tickCount > 1 ? entity.getPosition(partialTicks) : entity.position()).add(entity.getDeltaMovement().normalize().scale(-data.getMotionShift())).add(offset.x(), offset.y(), offset.z());

        var player = entity.getCommandSenderWorld().getNearestPlayer(entity, getTrailRenderDistance());

        if (player == null) {
            return entityPosition;
        }

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

        ITrailConfigProvider.super.renderTrail(pTicks, poseStack, bufferSourceList);
    }
}

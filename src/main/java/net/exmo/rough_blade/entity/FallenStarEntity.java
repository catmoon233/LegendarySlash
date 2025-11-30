package net.exmo.rough_blade.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityDrive;

import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.init.RBParticlesTypeRegistry;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data.TrailConfigData;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.misc.ITrailConfigProvider;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.spongepowered.asm.mixin.Unique;

public class FallenStarEntity extends EntityDrive implements ITrailConfigProvider {



    @Unique
    private TrailConfigData perception$trailData = new TrailConfigData();

    public FallenStarEntity(EntityType<? extends EntityDrive> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public static FallenStarEntity createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new FallenStarEntity(RBEntityRegistry.FALLEN_STAR_ENTITY, worldIn);
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
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        explode();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        explode();
    }

    private void explode() {
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity owner = this.getOwner();
            if (!(owner instanceof LivingEntity)) {
                return;
            }
            ItemStack mainHandItem = ((LivingEntity) owner).getMainHandItem();
            ISlashBladeState slashBlade = ExUtils.getSlashBlade(mainHandItem);
            if (slashBlade == null) {
                return;
            }
            // 创建爆炸粒子效果
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(),
                    3, 0.5, 0.5, 0.5, 0.1);
            
            // 播放爆炸声音
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), 
                    SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
            
            // 对周围实体造成伤害
            int radius = 5;
            var entities = level().getEntities(this, AABB.ofSize(this.position(), radius * 2, radius * 2, radius * 2));


            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity && entity != this && entity != owner) {
                    // 计算距离衰减
                    double distance = entity.distanceTo(this);
                    if (distance <= radius) {
                        // 造成伤害，伤害随距离增加而减少
                        float damage = (float) (3f*(20.0+((LivingEntity) owner).getAttributeValue(Attributes.ATTACK_DAMAGE))*slashBlade.getRefine()*0.02 * (1.0 - distance / radius));
                        livingEntity.hurt(new DamageSource(owner.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), owner), damage);


                        
                        // 击退效果
                        Vec3 direction = entity.position().subtract(this.position()).normalize();
                        livingEntity.knockback(0.5F, -direction.x, -direction.z);
                    }
                }
            }
            
            // 移除实体
            this.discard();
        }
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
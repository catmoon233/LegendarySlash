package net.exmo.rough_blade.entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;
import java.util.Map;

public class SummonedSwordPlus extends EntityHeavyRainSwords {
    public SummonedSwordPlus(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.noCulling = true;

    }

    public static SummonedSwordPlus createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new SummonedSwordPlus(RBEntityRegistry.SUMMONEDSWORDPLUS, worldIn);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        Entity targetEntity = p_213868_1_.getEntity();
        Level level = this.level();

        // 使用 NBT 记录最后一次被击中的时间
        int lastHitTick = targetEntity.getPersistentData().getInt("LastHitTick");
        if (level.getGameTime() - lastHitTick < 10) {
            return; // 冷却期内不重复处理
        }

        // 更新最后一次击中时间
        targetEntity.getPersistentData().putInt("LastHitTick", (int) level.getGameTime());

        if (targetEntity instanceof LivingEntity) {
            KnockBacks.cancel.action.accept((LivingEntity)targetEntity);
            StunManager.setStun((LivingEntity)targetEntity);
        }

        super.onHitEntity(p_213868_1_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BASESIZE, 1.0f);
        this.entityData.define(Particle, true);
        this.entityData.define(ISHit, "");
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        NBTHelper.getNBTCoupler(compound)
          .put("BaseSize", this.getBaseSize())
                .put("Particle", this.getParticle())
        ;

    }

    public float getBaseSize() {
        return this.getEntityData().get(BASESIZE);
    }

    public void setBaseSize(float value) {
        this.getEntityData().set(BASESIZE, value);
    }
    public boolean getParticle() {
        return this.getEntityData().get(Particle);
    }

    public void setParticle(boolean value) {
        this.getEntityData().set(Particle, value);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        NBTHelper.getNBTCoupler(compound)
         .get("BaseSize", this::setBaseSize)
                .get("Particle", this::setParticle)
        ;

    }
    private static final EntityDataAccessor<Float> BASESIZE = SynchedEntityData.<Float>defineId(SummonedSwordPlus.class,
            EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> Particle = SynchedEntityData.<Boolean>defineId(SummonedSwordPlus.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> ISHit = SynchedEntityData.<String>defineId(SummonedSwordPlus.class,
            EntityDataSerializers.STRING);
    @Override
    public void tick() {
        Level level = this.level();
        float radius = getBaseSize()*60f;
        if (getPersistentData().contains("redius") && getPersistentData().getFloat("redius") != radius) getPersistentData().putFloat("redius", radius);
       // if (!getParticle()){

        if (getOwner() instanceof LivingEntity owner) {
            var entities = level.getEntities(this, AABB.ofSize(this.position(), radius, radius, radius));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity != owner) {
                        livingEntity.knockback(1, 1, 1);
                        StunManager.setStun(livingEntity, 20);
                        livingEntity.invulnerableTime = 0;
                        var damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), this, owner);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));

                        livingEntity.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * getDamage() + 5f ));
                        onHitEntity(new EntityHitResult(livingEntity));

                    }
                }
            }
        }
           // }
        super.tick();

        if (!getParticle())return;
        for (int i = 0; i < 35; ++i) {
            if (this.level().isClientSide()) {
                double baseSize = getBaseSize(); // 假设 baseSize 是 0.5，可以根据实际情况调整
                double randomX = this.getX() + (this.random.nextDouble() - 0.5) * baseSize * 200;
                double randomY = this.getY() + (this.random.nextDouble() - 0.5) * baseSize * 200;
                double randomZ = this.getZ() + (this.random.nextDouble() - 0.5) * baseSize * 200;

                this.level().addParticle(ParticleTypes.SOUL, randomX, randomY, randomZ, 0.0D, 0.0D, 0.0D);
            }
        }
        //        if (!this.itFired() && this.level().isClientSide() && this.getVehicle() == null) {
//            this.startRiding(this.getOwner(), true);
//        }
//        this.baseTick();
//        if (!this.level().isClientSide() && 100 < this.tickCount) {
//            this.remove(RemovalReason.DISCARDED);
//        }
    }
}

package net.exmo.legendary_slash.entity;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.exmo.legendary_slash.init.LSEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.PlayMessages;

import java.util.HashMap;
import java.util.Map;

public class SummonedSwordPlus extends EntityHeavyRainSwords {
    public SummonedSwordPlus(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true; // 设置投射物可以穿墙
        this.noCulling = true;

    }

    public static SummonedSwordPlus createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new SummonedSwordPlus(LSEntityRegistry.SUMMONEDSWORDPLUS, worldIn);
    }


    private final Map<Entity,Integer> hitEntites = new HashMap<>();
    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        Entity targetEntity = p_213868_1_.getEntity();
        if (hitEntites.containsKey(targetEntity) && hitEntites.get(targetEntity)>0){
            hitEntites.put(targetEntity, hitEntites.get(targetEntity)-1);
            return;
        }
        hitEntites.put(targetEntity,10);
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
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        NBTHelper.getNBTCoupler(compound)
          .put("BaseSize", this.getBaseSize());

    }

    public float getBaseSize() {
        return this.getEntityData().get(BASESIZE);
    }

    public void setBaseSize(float value) {
        this.getEntityData().set(BASESIZE, value);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        NBTHelper.getNBTCoupler(compound)
         .get("BaseSize", this::setBaseSize);

    }
    private static final EntityDataAccessor<Float> BASESIZE = SynchedEntityData.<Float>defineId(SummonedSwordPlus.class,
            EntityDataSerializers.FLOAT);
    @Override
    public void tick() {
        Level level = this.level();
        float radius = getBaseSize()*60f;
        if (getOwner() instanceof LivingEntity owner){
        var entities = level.getEntities(this, AABB.ofSize(this.position(), radius , radius, radius ));
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity != owner) {
                    livingEntity.knockback(1, 1, 1);
                    StunManager.setStun(livingEntity, 20);
                    livingEntity.invulnerableTime = 0;
                    var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), owner);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));

                    livingEntity.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * getDamage() + 5f + livingEntity.getHealth() * 0.05f));
                    onHitEntity(new EntityHitResult(livingEntity));

                }
            }
        }
            }
        super.tick();
        for (int i = 0; i < 35; ++i) {
            if (this.level().isClientSide()) {
                double baseSize = getBaseSize(); // 假设 baseSize 是 0.5，可以根据实际情况调整
                double randomX = this.getX() + (this.random.nextDouble() - 0.5) * baseSize * 200;
                double randomY = this.getY() + (this.random.nextDouble() - 0.5) * baseSize * 200;
                double randomZ = this.getZ() + (this.random.nextDouble() - 0.5) * baseSize * 200;

                this.level().addParticle(ParticleTypes.SOUL, randomX, randomY, randomZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}

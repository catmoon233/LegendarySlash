package net.exmo.legendary_slash.entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.legendary_slash.init.LSEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
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

public class EntityDrivePlus extends EntityDrive {
    public EntityDrivePlus(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
   //     this.noPhysics = true; // 设置投射物可以穿墙
        this.noCulling = true;

    }

    public static EntityDrivePlus createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new EntityDrivePlus(LSEntityRegistry.DRIVEPLUS, worldIn);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockraytraceresult) {
    }
    private Map<Entity,Integer> hitEntitys = new HashMap<>();
    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        Entity targetEntity = p_213868_1_.getEntity();
        int i = Mth.ceil(this.getDamage());
        if (hitEntitys.containsKey(targetEntity) && hitEntitys.get(targetEntity)>0){
            hitEntitys.put(targetEntity,hitEntitys.get(targetEntity)-1);
            return;
        }
        hitEntitys.put(targetEntity,8);
        if (this.getIsCritical()) {
            i += this.random.nextInt(i / 2 + 2);
        }

        Entity shooter = this.getShooter();
        DamageSource damagesource;
        if (shooter == null) {
            damagesource = this.damageSources().indirectMagic(this, this);
        } else {
            damagesource = this.damageSources().indirectMagic(this, shooter);
            if (shooter instanceof LivingEntity) {
                Entity hits = targetEntity;
                if (targetEntity instanceof PartEntity) {
                    hits = ((PartEntity<?>) targetEntity).getParent();
                }
                ((LivingEntity) shooter).setLastHurtMob(hits);
            }
        }

        int fireTime = targetEntity.getRemainingFireTicks();
        if (this.isOnFire() && !(targetEntity instanceof EnderMan)) {
            targetEntity.setSecondsOnFire(5);
        }

        // todo: attack manager
        targetEntity.invulnerableTime = 0;
        float damageValue = (float) i;
        if(this.getOwner() instanceof LivingEntity living) {
            damageValue *= (float) living.getAttributeValue(Attributes.ATTACK_DAMAGE);
        }

        if (targetEntity.hurt(damagesource, damageValue)) {
            if (targetEntity.level() instanceof ServerLevel level1) {
                for (int i1 = 0; i1 < 3; i1++) {
                    int x = (int) (Math.random() * 2) - 1;
                    int y = (int) (Math.random() * 2) - 1;
                    int z = (int) (Math.random() * 2) - 1;
                    level1.sendParticles(ParticleTypes.CLOUD, targetEntity.getX() + x, targetEntity.getY() + 1 + y, targetEntity.getZ() + z, 5, 0.1, 0.1, 0.1, 0.1);

                }
            }
            Entity hits = targetEntity;
            if (targetEntity instanceof PartEntity) {
                hits = ((PartEntity<?>) targetEntity).getParent();
            }

            if (hits instanceof LivingEntity) {
                LivingEntity targetLivingEntity = (LivingEntity) hits;

                StunManager.setStun(targetLivingEntity);
                if (!this.level().isClientSide() && shooter instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(targetLivingEntity, shooter);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) shooter, targetLivingEntity);
                }

                affectEntity(targetLivingEntity, getPotionEffects(), 1.0f);

                if (shooter != null && targetLivingEntity != shooter && targetLivingEntity instanceof Player
                        && shooter instanceof ServerPlayer) {
                    ((ServerPlayer) shooter).playNotifySound(this.getHitEntityPlayerSound(), SoundSource.PLAYERS, 0.18F,
                            0.45F);
                }
            }

            this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            targetEntity.setRemainingFireTicks(fireTime);
        }
    }

    @Override
    public void tick() {
        Level level = this.level();
        float radius = getBaseSize()*100f;
        if (getOwner() instanceof LivingEntity owner){
        var entities = level.getEntities(this, AABB.ofSize(this.position(), radius , radius, radius ));
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity != owner) {
                    livingEntity.knockback(1, 1, 1);
                    StunManager.setStun(livingEntity, 20);

                   // var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
                   // livingEntity.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 10f + 5f + livingEntity.getHealth() * 0.1f));
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

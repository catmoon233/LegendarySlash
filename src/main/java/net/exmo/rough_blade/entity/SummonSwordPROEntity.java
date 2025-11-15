package net.exmo.rough_blade.entity;


import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PlayMessages;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SummonSwordPROEntity extends SummonedSwordPlus {
    private static final EntityDataAccessor<Boolean> IS_BLOCK = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IT_FIRED = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> PSO = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> OFFSET = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> IT_COR = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IT_RAY = SynchedEntityData.defineId(SummonSwordPROEntity.class, EntityDataSerializers.BOOLEAN);
    long fireTime = -1;


    public SummonSwordPROEntity(EntityType<? extends Projectile> entityTypeIn, Level worldIn)
    {
        super(entityTypeIn, worldIn);

        this.setPierce((byte) 5);
    }

    public static SummonSwordPROEntity createInstance(PlayMessages.SpawnEntity packet, Level worldIn)
    {
        return new SummonSwordPROEntity(RBEntityRegistry.SUMMONEDSWORDPPROLUS, worldIn);
    }


    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();

        this.entityData.define(IS_BLOCK, false);
        this.entityData.define(IT_FIRED, false);
        this.entityData.define(IT_COR, false);
        this.entityData.define(IT_RAY, false);
        this.entityData.define(SPEED, 3.0f);
        this.entityData.define(PSO, 0);
        this.entityData.define(OFFSET, Vec3.ZERO.toVector3f());
    }

    public boolean isBlock() {
        return this.entityData.get(IS_BLOCK);
    }

    public void setBlock(boolean value) {
        this.entityData.set(IS_BLOCK, value);
    }

    public void doFire()
    {
        this.getEntityData().set(IT_FIRED, true);
    }

    public boolean itFired()
    {
        return this.getEntityData().get(IT_FIRED);
    }

    public void setSpeed(float speed)
    {
        this.getEntityData().set(SPEED, speed);
    }

    public float getSpeed() {return this.getEntityData().get(SPEED);}

    public void setOffset(Vec3 offset)
    {
        this.getEntityData().set(OFFSET, offset.toVector3f());
    }

    public Vec3 getOffset() {return new Vec3(this.getEntityData().get(OFFSET));}
    public boolean itCor(){
        return this.getEntityData().get(IT_COR);
    }
    public void doCor()
    {
        this.getEntityData().set(IT_COR, true);
    }

    public void setPso(int value)
    {
        this.getEntityData().set(PSO, value);
    }
    public int getPso()
    {
        return this.getEntityData().get(PSO);
    }
    @Override
    public void tick() {
        if (!itFired() && level().isClientSide() && getVehicle() == null)
            startRiding(this.getOwner(), true);

        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }

        ;
        super.tick();
    }


    @Override
    protected void onHit(HitResult raytraceResultIn) {

        if (raytraceResultIn.getType() != HitResult.Type.MISS) {
            if (this.level() instanceof ServerLevel level) {
                var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), getOwner());


                int radius = 5;
                if (getParticle()) {
                    level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 3, 0.5, 0.5, 0.5, 0.5);
                }else {
                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(), 8, 0.5, 0.5, 0.5, 0.5);
                }
                if (getOwner() instanceof LivingEntity owner) {
                    var entities = level.getEntities(this, AABB.ofSize(this.position(), radius, radius, radius));
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            if (entity != this) {
                                if (livingEntity != owner) {
                                    livingEntity.knockback(1, 1, 1);
                                    StunManager.setStun(livingEntity, 20);
                                    livingEntity.invulnerableTime = 0;
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
                                    setBlock(true);
                                    livingEntity.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * getDamage() * 2f + 5f));
                                    //onHitEntity(new EntityHitResult(livingEntity));

                                }
                            }
                        }
                    }
                }


            }
            if (level().isClientSide) {
                level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level().random.nextFloat() - level().random.nextFloat()) * 0.2F) * 0.7F, false);
            }
            this.discard();
        }

        super.onHit(raytraceResultIn);
    }

    @Override
    public void rideTick()
    {
        if (itFired() && fireTime <= tickCount)
        {
            faceEntityStandby();
            Entity vehicle = getVehicle();
            Vec3 dir1 = this.getViewVector(0);
            if (!(vehicle instanceof LivingEntity))
            {
                this.shoot(dir1.x, dir1.y, dir1.z, getSpeed(), 1.0f);
                return;
            }

            LivingEntity sender = (LivingEntity) getVehicle();
            this.stopRiding();

            this.tickCount = 0;

            Level worldIn = sender.level();


            if (!itCor()) {
                Vec3 dir2 = this.getViewVector(0);

                int isRight = Math.random() > 0.5 ? 1 : -1;
                Entity entity = this.getOwner();
                Vec3 lookVec = entity.getLookAngle();
                 double    x =  (Math.cos(Math.toRadians(entity.getYRot())) * 2) * isRight;
                double    z = (Math.sin(Math.toRadians(entity.getYRot())) * 2) * isRight;
                Vec3 totalPos3 = new Vec3(x,dir2.y,z);
                Vec3 totalPos4 = totalPos3.subtract(dir2).normalize();
                this.shoot(totalPos4.x,totalPos4.y,totalPos4.z,getSpeed(),1.0f);

                this.getEntityData().set(IT_FIRED, true); // 将itFired设置为true
                Runnable runnable = () -> {
                    if (this  ==null)return;
                    if (!this.isAlive())return;
                    Vec3 dir = this.getViewVector(0);
                    final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
                    List<Entity> _entfound = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(80 / 2d), a -> true)
                            .stream()
                            .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                            .toList();

                    Optional<Entity> entityiterator = _entfound.stream()
                            .filter(e -> e instanceof LivingEntity && ((LivingEntity) e).getHealth() > 0 && e != entity && e != this)
                            .findFirst();
                    Vec3 targetPos = entityiterator.map((e) -> new Vec3(e.getX(), e.getY() + e.getEyeHeight() * 0.5, e.getZ()))
                            .orElseGet(() ->
                            {
                                Vec3 start = sender.getEyePosition(1.0f);
                                Vec3 end = start.add(sender.getLookAngle().scale(40));
                                HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, sender));
                                return result.getLocation();
                            });
                    Vec3 pos = this.getPosition(0.0f);
                    dir = targetPos.subtract(pos).normalize();
                    this.shoot(dir.x, dir.y, dir.z, getSpeed(), 1f);


                };
                Rough_blade.queueServerWork(4, runnable);
                Rough_blade.queueServerWork(6, runnable);

            }





            if (canUpdate()) this.baseTick();

            if (sender instanceof ServerPlayer)
            {
                ((ServerPlayer) sender).playNotifySound(SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            return;
        }

        this.setDeltaMovement(Vec3.ZERO);
        if (canUpdate()) this.baseTick();

        faceEntityStandby();

        // lifetime check
        if (!itFired() && getVehicle() instanceof LivingEntity)
        {
            if (tickCount >= getDelay())
            {
                fireTime = tickCount + getDelay();
                doFire();
            }
        }
    }

    protected void faceEntityStandby()
    {
        Vec3 pos = this.getVehicle().position();
        Vec3 offset = this.getOffset();

        if (this.getVehicle() == null)
        {
            doFire();
            return;
        }

        offset = offset.xRot((float) Math.toRadians(-this.getVehicle().getXRot()));
        offset = offset.yRot((float) Math.toRadians(-this.getVehicle().getYRot()));

        pos = pos.add(offset);

        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        setPos(pos);
        setRot(-this.getVehicle().getYRot(), -this.getVehicle().getXRot());
    }

    @Override
    protected void onHitBlock(BlockHitResult blockraytraceresult) {


    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
 //   super.onHitEntity( result);
//        if (tickCount < getDelay())return;
//        if (isBlock()) return;
//        Entity targetEntity = result.getEntity();
//        if (targetEntity instanceof Player )return;
//        if (targetEntity instanceof LivingEntity livingEntity)
//        {
//
//            KnockBacks.cancel.action.accept(livingEntity);
//            StunManager.setStun(livingEntity);
//
//            if (this.level() instanceof ServerLevel level){
//           //     var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),getOwner());
//
//                var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),getOwner());
//
//             //   int radius = 5;
//                int radius = 5;
//                level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 0.5, 0.5, 0.5, 0.5);
//                if (getOwner() instanceof LivingEntity owner) {
//                    var entities = level.getEntities(this, AABB.ofSize(this.position(), radius, radius, radius));
//                    for (Entity entity : entities) {
//                        if (entity!=this) {
//                            if (entity instanceof LivingEntity livingEntity1) {
//                                if (livingEntity1 != owner) {
//                                    livingEntity1.knockback(1, 1, 1);
//                                    StunManager.setStun(livingEntity1, 20);
//                                    livingEntity1.invulnerableTime = 0;
//                                    livingEntity1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
//
//                                    livingEntity1.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * getDamage() * 2f + 5f));
//                                    //onHitEntity(new EntityHitResult(livingEntity));
//
//                                }
//                            }
//                        }
//                    }
//                }
////                if (getOwner() instanceof LivingEntity owner) {
////                    var entities = level.getEntities(this, AABB.ofSize(this.position(), radius, radius, radius));
////                    for (Entity entity : entities) {
////                        if (entity instanceof LivingEntity livingEntity1) {
////                            if (livingEntity1 != livingEntity) {
////                                if (livingEntity1 != owner) {
////                                    livingEntity1.knockback(1, 1, 1);
////                                    StunManager.setStun(livingEntity1, 20);
////                                    livingEntity1.invulnerableTime = 0;
////                                    livingEntity1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
////
////                               //     livingEntity1.hurt(damageSource, (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * getDamage() + 5f + livingEntity1.getHealth() * 0.05f));
////                                    onHitEntity(new EntityHitResult(livingEntity1));
////
////                                }
////                            }
////                        }
////                    }
////                }
//
//            }
//            if (level().isClientSide) {
//                level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level().random.nextFloat() - level().random.nextFloat()) * 0.2F) * 0.7F, false);
//            }
//
//
//        }
//        this.discard();

    }

    }




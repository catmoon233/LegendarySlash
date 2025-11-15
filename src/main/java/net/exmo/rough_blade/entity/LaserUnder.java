package net.exmo.rough_blade.entity;

import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class LaserUnder extends Projectile {

    private static final EntityDataAccessor<Integer> COLOR;
    public static final EntityDataAccessor<Float> RX;
    public static final EntityDataAccessor<Float> RY;
    public static final EntityDataAccessor<Float> YSclae;
    static {
        RX = SynchedEntityData.defineId(LaserUnder.class, EntityDataSerializers.FLOAT);
        RY = SynchedEntityData.defineId(LaserUnder.class, EntityDataSerializers.FLOAT);
        COLOR = SynchedEntityData.defineId(LaserUnder.class, EntityDataSerializers.INT);
        YSclae = SynchedEntityData.defineId(LaserUnder.class, EntityDataSerializers.FLOAT);
    }

    public int getColor() {
        return (Integer)this.getEntityData().get(COLOR);
    }

    public void setColor(int value) {
        this.getEntityData().set(COLOR, value);
    }
    public static final EntityDataAccessor<Integer> TICK = SynchedEntityData.defineId(LaserUnder.class, EntityDataSerializers.INT); ;
    public LaserUnder(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.noCulling=true;
    }
    public static LaserUnder createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new LaserUnder(RBEntityRegistry.JIGUAN, worldIn);
    }
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        NBTHelper.getNBTCoupler(compound).put("Color", new Integer[]{this.getColor()});
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        NBTHelper.getNBTCoupler(compound).get("Color", this::setColor, new Integer[0]);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TICK, 0);
        this.entityData.define(COLOR, 3355647);
        this.entityData.define(RX, 0.0f);
        this.entityData.define(RY, 0.0f);
        this.entityData.define(YSclae, 1.0f);
    }
    public void setTick(int value) {
        this.getEntityData().set(TICK, value);
    }

    public Integer getTick() {
        return this.getEntityData().get(TICK);
    }
    private Vec3 calculateBehindEntity(Entity player, double distance) {
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        return player.position().add(
                -Math.sin(Math.toRadians(yaw)) * distance,
                -Math.sin(Math.toRadians(pitch)) * distance * 0.5,
                Math.cos(Math.toRadians(yaw)) * distance
        );
    }
    public void poseA(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        float f = Mth.sqrt((float)vec3d.horizontalDistanceSqr());
        this.setPos(this.position());
        this.setYRot((float)(Mth.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

    }

    @Override
    public void setOwner(@Nullable Entity entity) {
//        if (!(entity instanceof Player))return;
//        Vec3 start = entity.getEyePosition(1.0f);
//        Vec3 end = start.add(entity.getLookAngle().scale(40));
//        HitResult result = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
//                ClipContext.Fluid.NONE, entity));
//        Vec3 targetPos = result.getLocation();
//        Vec3 pos = entity.getEyePosition(2)
//                .add(VectorHelper.getVectorForRotation(0.0f, entity.getViewYRot(0) + 90).scale(1));
//        Vec3 dir = targetPos.subtract(pos).normalize();
//        poseA(dir.x, dir.y, dir.z, 3.0f, 0.0f);
        super.setOwner(entity);
    }

    @Override
    public void tick() {
        entityData.set(TICK,  getTick()+1);
        // 不断向下延伸
        double minX = this.getX() - 0.5;
        int tickCount1 = getTick();
        double minY = this.getY() - tickCount1 * 5; // 根据tickCount不断向下扩展
        double minZ = this.getZ() - 0.5;
        double maxX = this.getX() + 0.5;
        double maxY = this.getY(); // 最高顶点保持不变
        double maxZ = this.getZ() + 0.5;
        entityData.set(YSclae, (float) (minY - this.getY()));

        // 检测地面高度
        double groundLevel = this.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG,(int) this.getX(), (int) this.getZ());
        if (minY < groundLevel) {
            minY = groundLevel;
            // 触发爆炸效果
            if (this.level() instanceof ServerLevel level) {
                var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), getOwner());
               // level.explode(this, this.getX(), groundLevel, this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
            }
//            discard(); // 销毁激光实体
        }else {
            this.setBoundingBox(new AABB(minX, minY, minZ, maxX, maxY, maxZ));

        }
        if (tickCount1 >80){
            {
                final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
                List<Entity> _entfound = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(minY*30), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator != this.getOwner()||entityiterator!=this) {
                        if (entityiterator instanceof LivingEntity) {
                            entityiterator.invulnerableTime = 0;
                            ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, (false), (false)));
                            entityiterator.hurt(new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), this,this.getOwner()), (float) (Math.pow((float) ((LivingEntity)this.getOwner()).getAttribute(Attributes.ATTACK_DAMAGE).getValue(),1.5)*4f));
                        }
                    }
                }
            }
        }
        if (tickCount1 > 100) discard();
        super.tick();
        setXRot(entityData.get(RX));
        setYRot(entityData.get(RY));
    }

    @Override
    protected void onHitBlock(BlockHitResult blockraytraceresult) {
        //爆炸
        if (this.level() instanceof ServerLevel level){
            var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),getOwner());
            //爆炸位置随机
            RandomSource random = level.getRandom();
            double x = blockraytraceresult.getLocation().x + (random.nextDouble() - 0.5) * 2.0;
            double y = blockraytraceresult.getLocation().y + (random.nextDouble() - 0.5) * 2.0;
            double z = blockraytraceresult.getLocation().z + (random.nextDouble() - 0.5) * 2.0;
           // level.explode(this, x, y, z, 3.0F, Level.ExplosionInteraction.MOB);
        }
        super.onHitBlock(blockraytraceresult);
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void rideTick() {
        super.rideTick();
    }
}

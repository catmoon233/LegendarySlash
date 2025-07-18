package net.exmo.rough_blade.entity;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.util.NBTHelper;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

public class FaZhenBase extends EntityAbstractSummonedSword {

    private static final EntityDataAccessor<Integer> COLOR;
    public static final EntityDataAccessor<Float> RX;
    public static final EntityDataAccessor<Float> RY;
    static {
        RX = SynchedEntityData.defineId(FaZhenBase.class, EntityDataSerializers.FLOAT);
        RY = SynchedEntityData.defineId(FaZhenBase.class, EntityDataSerializers.FLOAT);
        COLOR = SynchedEntityData.defineId(FaZhenBase.class, EntityDataSerializers.INT);
    }
    public int getColor() {
        return (Integer)this.getEntityData().get(COLOR);
    }

    public void setColor(int value) {
        this.getEntityData().set(COLOR, value);
    }
    private static final EntityDataAccessor<Integer> TICK = SynchedEntityData.defineId(FaZhenBase.class, EntityDataSerializers.INT); ;
    public FaZhenBase(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.noCulling=true;
    }
    public static FaZhenBase createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new FaZhenBase(RBEntityRegistry.FZB, worldIn);
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
        if (!(entity instanceof Player))return;
        Vec3 start = entity.getEyePosition(1.0f);
        Vec3 end = start.add(entity.getLookAngle().scale(40));
        HitResult result = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, entity));
        Vec3 targetPos = result.getLocation();
        Vec3 pos = entity.getEyePosition(2)
                .add(VectorHelper.getVectorForRotation(0.0f, entity.getViewYRot(0) + 90).scale(1));
        Vec3 dir = targetPos.subtract(pos).normalize();
        poseA(dir.x, dir.y, dir.z, 3.0f, 0.0f);
        super.setOwner(entity);
    }

    @Override
    public void tick() {
        if (this.tickCount >20){
            if (getOwner()==null)return;
            for (int i = 0; i < 2; i++){
                Level level = level();
                EntityAbstractSummonedSword ss = new EntityAbstractSummonedSword(SlashBlade.RegistryEvents.SummonedSword, level);


                    Vec3 start = this.getEyePosition(1.0f);
                    Vec3 end = start.add(this.getLookAngle().scale(40));
                    HitResult result = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE, this));
                Vec3 targetPos = result.getLocation();
                Vec3 pos = this.getEyePosition(2)
                        .add(VectorHelper.getVectorForRotation(0.0f, this.getViewYRot(0) + 90).scale(1));
                ss.setPos(pos.x, pos.y, pos.z);
                    ss.setIsCritical(false);
                    ss.setOwner(this);
                Vec3 dir = targetPos.subtract(pos).normalize();
                ss.shoot(dir.x, dir.y, dir.z, 3.0f, 0.0f);
                    ss.setColor(getColor());
                    ss.setRoll(0);

                    ss.setDamage(((LivingEntity) getOwner()).getAttributeValue(Attributes.ATTACK_DAMAGE)/50);
                    // force riding
                //    ss.startRiding(this, true);

                //    ss.setDelay(1);

//                        boolean isRight = ss.getDelay() % 2 == 0;
                RandomSource random = level.getRandom();

                double xOffset = random.nextDouble() * 2.5;
                double yOffset = random.nextFloat() *2.5;
                double zOffset = random.nextFloat() * 2.5;

                ss.setPos(pos.add(xOffset, yOffset-2, zOffset));


                    this.getOwner().playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                    level.addFreshEntity(ss);
                }
            }
        if (this.tickCount>140)discard();
        super.tick();
        setXRot(entityData.get(RX));
        setYRot(entityData.get(RY));
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

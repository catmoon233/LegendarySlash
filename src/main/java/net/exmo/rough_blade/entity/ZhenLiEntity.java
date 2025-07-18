package net.exmo.rough_blade.entity;

import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class ZhenLiEntity extends Projectile
{
    private static final EntityDataAccessor<Integer> AMP;
    private static final EntityDataAccessor<Integer> TICK;
    static {
        AMP = SynchedEntityData.defineId(ZhenLiEntity.class, EntityDataSerializers.INT);
        TICK = SynchedEntityData.defineId(ZhenLiEntity.class, EntityDataSerializers.INT);
    }
    public void setAmp(int value) {
        this.getEntityData().set(AMP, value);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AMP, 0);
        this.entityData.define(TICK, 0);

    }
    public void setTick(int value) {
        this.getEntityData().set(TICK, value);
    }
    public Integer getTick() {
        return this.getEntityData().get(TICK);
    }
    public Integer getAmp() {
        return this.getEntityData().get(AMP);
    }
    public ZhenLiEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.noCulling = true;
        this.noPhysics =true;
    }
    public static ZhenLiEntity createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new ZhenLiEntity(RBEntityRegistry.ZLENTITY, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        Integer tick = getTick();
        if (tick > 20){
            this.remove(RemovalReason.KILLED);
        }else setTick(tick + 1);
    }
}

package net.exmo.rough_blade.content.specialEffects;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.entity.ZhenLiEntity;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.network.ClearMingLiChangeMessage;
import net.exmo.rough_blade.network.MingLiChangeMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class MingLiEffectHandle {
    public static void addMingLi(LivingEntity livingEntity , int level){
        CompoundTag persistentData = livingEntity.getPersistentData();
        int mingLiLevel = persistentData.getInt("MingLiLevel");
        int p128407 = mingLiLevel + level;
        persistentData.putInt("MingLiLevel", p128407);
        persistentData.putInt("MingLiTime", 400);
        updatePlayerData(livingEntity, p128407);

    }
    public static void addZhenLi(LivingEntity livingEntity ){
        CompoundTag persistentData = livingEntity.getPersistentData();
        persistentData.putInt("ZhenLiTime", 400);
    }
    public static boolean isMingLi(LivingEntity livingEntity){
        CompoundTag persistentData = livingEntity.getPersistentData();
        return  (persistentData.contains("MingLiLevel")&& persistentData.getInt("MingLiLevel") > 0);
    }
    public static boolean isZhenLi(LivingEntity livingEntity){
        CompoundTag persistentData = livingEntity.getPersistentData();
        return  (persistentData.contains("ZhenLiTime")&& persistentData.getInt("ZhenLiTime") > 0);
    }

    public static void removeMingLi(LivingEntity livingEntity){
        CompoundTag persistentData = livingEntity.getPersistentData();
        persistentData.remove("MingLiLevel");
        persistentData.remove("MingLiTime");
        clearPlayerData(livingEntity);
    }
    public static void removeMingLi(LivingEntity livingEntity,int level){
        CompoundTag persistentData = livingEntity.getPersistentData();
        persistentData.putInt("MingLiLevel", persistentData.getInt("MingLiLevel") - level);
        if (persistentData.getInt("MingLiLevel") <= 0){
            persistentData.remove("MingLiLevel");
            persistentData.remove("MingLiTime");
        }
        clearPlayerData(livingEntity);
    }
    public static void applyEffectTick(LivingEntity livingEntity, int level) {

        livingEntity.setDeltaMovement(0,0.01,0);
        if (livingEntity.tickCount %2 == 0){

            if (MingLiEffectHandle.isMingLi(livingEntity)){

                int amplifier = MingLiEffectHandle.getMingLi(livingEntity);
                if (amplifier  <=0){
                    MingLiEffectHandle.removeMingLi(livingEntity);


                }else {
                    MingLiEffectHandle.addMingLi(livingEntity, -1);
                }
                if (livingEntity.level() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.SOUL, livingEntity.getX(), livingEntity.getY()+1, livingEntity.getZ(), 8, 0.8, 0.8, 0.8, 0.8);
                    ZhenLiEntity zhenLiEntity = new ZhenLiEntity(RBEntityRegistry.ZLENTITY, livingEntity.level());
                    RandomSource random = serverLevel.random;
                    double xOffset = random.nextDouble() * 0.3 ;
                    double yOffset = random.nextFloat() *0.3;
                    double zOffset = random.nextFloat() * 0.3;
                    zhenLiEntity.setPos(livingEntity.getX()+xOffset, livingEntity.getY()+1+yOffset, livingEntity.getZ()+zOffset);
                    zhenLiEntity.setAmp(amplifier %12 );
                    serverLevel.addFreshEntity(zhenLiEntity);
                    zhenLiEntity.invulnerableTime = 0;

                    String string = livingEntity.getPersistentData().getString("ZhenLiO");
                    if (string.length()<10)return;
                    LivingEntity owner = ((LivingEntity) serverLevel.getEntity(UUID.fromString(string)));
                    if (owner==null)return;
                    zhenLiEntity.setOwner( owner);
                    var damageSource = new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD),owner);
                    int invulnerableTime = livingEntity.invulnerableTime;
                    livingEntity.invulnerableTime = 0;
                    double attributeValue = owner.getPersistentData().getFloat("ZhenLiDamage"); //owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    livingEntity.hurt(damageSource, (float) ((level+1)*(attributeValue +12) *13f+livingEntity.getMaxHealth()*0.0005));
                    livingEntity.invulnerableTime = invulnerableTime;
                }}else livingEntity.getPersistentData().remove("ZhenLiTime");
        }
    }
    @SubscribeEvent
    public static void updateMingLi(LivingEvent.LivingTickEvent event){
        LivingEntity livingEntity = event.getEntity();
        CompoundTag persistentData1 = livingEntity.getPersistentData();
        if (persistentData1.contains("ZhenLiTime")) {
            int zhenLiTime = persistentData1.getInt("ZhenLiTime");
            if (zhenLiTime > 0) {
                persistentData1.putInt("ZhenLiTime", zhenLiTime - 1);
                applyEffectTick(livingEntity, 0);
            } else if (zhenLiTime == 0) {
                persistentData1.remove("ZhenLiTime");
            }
        }
        if (isMingLi(livingEntity)){
            persistentData1.putInt("MingLiTime", persistentData1.getInt("MingLiTime") - 1);
            if (persistentData1.getInt("MingLiTime") <= 0) {
                if (persistentData1.getInt("MingLiLevel") <= 1) {
                    removeMingLi(livingEntity);
                    clearPlayerData(livingEntity);
                }else{
                    if (persistentData1.getInt("MingLiLevel") > 10)
                        addMingLi(livingEntity, -10);
                    else removeMingLi(livingEntity);
                }
            }
        }
    }

    private static void clearPlayerData(LivingEntity livingEntity) {
        Level level = livingEntity.level();
        Vec3 _center = livingEntity.position();
        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (Entity entityiterator : _entfound) {
            if (entityiterator instanceof ServerPlayer le) {
                ClearMingLiChangeMessage clearMingLiChangeMessage = new ClearMingLiChangeMessage(livingEntity.getUUID());
                Rough_blade.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->le),clearMingLiChangeMessage);
            }
        }
    }
    private static void updatePlayerData(LivingEntity livingEntity, int level1) {
        Level level = livingEntity.level();
        Vec3 _center = livingEntity.position();
        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (Entity entityiterator : _entfound) {
            if (entityiterator instanceof ServerPlayer le) {
                MingLiChangeMessage mingLiChangeMessage = new MingLiChangeMessage(livingEntity.getUUID(),level1);
                Rough_blade.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->le),mingLiChangeMessage);
            }
        }
    }

    @SubscribeEvent
    public static void Death(LivingDeathEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (isMingLi(livingEntity)){
            removeMingLi(livingEntity);
        }
    }
    public static int getMingLi(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (isMingLi(livingEntity)) {
                CompoundTag persistentData = livingEntity.getPersistentData();
                int mingLiLevel = persistentData.getInt("MingLiLevel");
                if (mingLiLevel > 0) {
                    return mingLiLevel;
                }
            }

        }
        return 0;
    }
}

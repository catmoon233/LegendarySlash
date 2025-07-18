package net.exmo.rough_blade.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.awt.*;

public class LSZhenLiEffect extends MobEffect {
    public LSZhenLiEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.YELLOW.getRGB());

    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

//    @Override
//    public void applyEffectTick(LivingEntity livingEntity, int p_19468_) {
//        super.applyEffectTick(livingEntity, p_19468_);
//        livingEntity.setDeltaMovement(0,0.01,0);
//        if (livingEntity.tickCount %2 == 0){
//            if (MingLiEffectHandle.isMingLi(livingEntity)){
//
//                int amplifier = MingLiEffectHandle.getMingLi(livingEntity);
//                if (amplifier -1 <0){
//                    MingLiEffectHandle.removeMingLi(livingEntity);
//
//                }else {
//                    MingLiEffectHandle.addMingLi(livingEntity, -1);
//                }
//                if (livingEntity.level() instanceof ServerLevel serverLevel){
//                serverLevel.sendParticles(ParticleTypes.SOUL, livingEntity.getX(), livingEntity.getY()+1, livingEntity.getZ(), 8, 0.8, 0.8, 0.8, 0.8);
//                ZhenLiEntity zhenLiEntity = new ZhenLiEntity(LSEntityRegistry.ZLENTITY, livingEntity.level());
//                    RandomSource random = serverLevel.random;
//                    double xOffset = random.nextDouble() * 0.3 ;
//                    double yOffset = random.nextFloat() *0.3;
//                    double zOffset = random.nextFloat() * 0.3;
//                zhenLiEntity.setPos(livingEntity.getX()+xOffset, livingEntity.getY()+1+yOffset, livingEntity.getZ()+zOffset);
//                zhenLiEntity.setAmp(amplifier %12 );
//                serverLevel.addFreshEntity(zhenLiEntity);
//                zhenLiEntity.invulnerableTime = 0;
//
//                    String string = livingEntity.getPersistentData().getString("ZhenLiO");
//                    if (string.length()<10)return;
//                    LivingEntity owner = ((LivingEntity) serverLevel.getEntity(UUID.fromString(string)));
//                    if (owner==null)return;
//                zhenLiEntity.setOwner( owner);
//                    var damageSource = new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD),owner);
//                    int invulnerableTime = livingEntity.invulnerableTime;
//                    livingEntity.invulnerableTime = 0;
//                    double attributeValue = owner.getPersistentData().getFloat("ZhenLiDamage"); //owner.getAttributeValue(Attributes.ATTACK_DAMAGE);
//                    livingEntity.hurt(damageSource, (float) ((p_19468_+1)*(attributeValue +12) *13f+livingEntity.getMaxHealth()*0.0005));
//                    livingEntity.invulnerableTime = invulnerableTime;
//            }}
//        }
//    }

    //    @Override
//    public void applyEffectTick(LivingEntity sender, int p_19468_) {
//        super.applyEffectTick(sender, p_19468_);
//
//        final var worldIn = sender.level();
//        final Vec3 _center = new Vec3(sender.getX(), sender.getY(), sender.getZ());
//        List<Entity> _entfound = worldIn.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
//        boolean find  = false;
//        for (Entity entityiterator : _entfound) {
//            if (entityiterator instanceof LivingEntity le) {
//                if (entityiterator != sender) {
//                    if (le.hasEffect(Rough_blade.effectAbout.MingLiEffect.get())){
//                        if (sender.tickCount%2==1){
//                            Vec3 position = le.position();
//                            double radius = 6.5; double theta = Math.random() * 2 * Math.PI; double phi = Math.acos(2 * Math.random() - 1);
//                            double dx = radius * Math.sin(phi) * Math.cos(theta);
//                            double dy = radius * Math.sin(phi) * Math.sin(theta);
//                            double dz = radius * Math.cos(phi);
//                            var  position1 = position.add(dx, dy, dz);
//                            sender.teleportTo(position1.x(), position.y(), position1.z());
//                            return;
//                        }
//                        MobEffectInstance effect = le.getEffect(Rough_blade.effectAbout.MingLiEffect.get());
//                        if (effect.getAmplifier()-1 <0){
//                            le.removeEffect(Rough_blade.effectAbout.MingLiEffect.get());
//                        }else {
//                            int i = effect.getAmplifier() - 1;
//                            le.removeEffect(Rough_blade.effectAbout.MingLiEffect.get());
//                            le.addEffect(new MobEffectInstance(Rough_blade.effectAbout.MingLiEffect.get(), 200,i));
//                        }
//                        Vec3 position = le.position();
//                        Vec3 position1 = sender.position();
//                        // 新增：计算从sender到目标的方向向量
//                        double distance = 6.5; // 固定距离
//                        Vec3 direction = position.subtract(position1).normalize();
//
//                        // 创建新位置：沿方向向量移动固定距离
//                        Vec3 newPosition = position.add(direction.scale(distance));
//                        sender.teleportTo(newPosition.x(), position.y(), newPosition.z());
//                        find = true;
//                        sender.lookAt(EntityAnchorArgument.Anchor.EYES, position.add(0,1.2,0));
//                        AttackManager.doSlash(sender, 0);
//                        ComboStateRegistry.doBigDriveSlash3(sender, worldIn, 0,  3);
//
//                        PathGenerator.generatePath(position, newPosition).forEach(vec3 -> {
//                            if (sender.level() instanceof ServerLevel serverLevel){
//                                serverLevel.sendParticles(ParticleTypes.SOUL, vec3.x(), vec3.y()+1, vec3.z(), 8, 0.1, 0.1, 0.1, 0.01);
//                                {
//
//                                    List<Entity> _entfound1= serverLevel.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
//                                    for (Entity entityiterator1 : _entfound1) {
//                                        if (entityiterator1 != sender) {
//                                            if (entityiterator1 instanceof LivingEntity) {
//                                                sender.heal(2);
//                                                entityiterator1.invulnerableTime =0;
//                                                entityiterator1.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD), sender), (float) (sender.getAttributeValue(Attributes.ATTACK_DAMAGE)*10f));
//
//                                            }
//
//                                        }
//                                    }
//                                }
//                            }
//                        });
//                        break;
//                    }
//                }
//            }
//        }
//    }
}
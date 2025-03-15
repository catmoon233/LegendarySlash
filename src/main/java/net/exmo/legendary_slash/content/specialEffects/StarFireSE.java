package net.exmo.legendary_slash.content.specialEffects;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.Drive;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.content.SpecialEffectEx;
import net.exmo.legendary_slash.init.LSSpecialEffectRegistry;
import net.exmo.legendary_slash.network.LSVARB;
import net.exmo.legendary_slash.utils.ExUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StarFireSE extends SpecialEffectEx {
    public StarFireSE() {
        super(100);
    }
    @SubscribeEvent
    public static void hurt(LivingHurtEvent event){
    if (event.getSource().getEntity() instanceof Player player){
        if (hasSpecialEffect2(player.getMainHandItem(),LSSpecialEffectRegistry.STAR_FIRE.getId(),player)){
            CompoundTag persistentData = player.getPersistentData();
            persistentData.putDouble("totalDamage",persistentData.getDouble("totalDamage") + event.getAmount());
        }

    }
    if (event.getSource().getDirectEntity() instanceof EntityDrive entityDrive){
        CompoundTag persistentData = entityDrive.getPersistentData();
        if (persistentData.contains("Star_Fire_drive")&&persistentData.getBoolean("Star_Fire_drive")){
            entityDrive.remove(Entity.RemovalReason.DISCARDED);
        }
    }
    }
    @SubscribeEvent
    public static void OnHit(SlashBladeEvent.HitEvent event){
        LivingEntity user = event.getUser();

        if (user instanceof Player player){
            if (!SpecialEffectEx.hasSpecialEffect2(event.getBlade(), LSSpecialEffectRegistry.STAR_FIRE.getId(), player))return;
            LivingEntity target = event.getTarget();

            CompoundTag persistentData = target.getPersistentData();
            if (persistentData.contains("Star_tag")){
                if (persistentData.getInt("Star_tag") >= 10){
                    persistentData.remove("Star_tag");
                    target.setDeltaMovement(target.getDeltaMovement().add(0,0.6,0));
                    StunManager.setStun(target,30);
                    ISlashBladeState state = event.getSlashBladeState();
                    Level worldIn = user.level();
                    if (worldIn instanceof ServerLevel level){
                        level.sendParticles(ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 10, 0, 2, 0, 0);
                      //  var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),entity);

                        level.explode(user, target.getX(), target.getY()+2, target.getZ(), 1, Level.ExplosionInteraction.NONE);
                    }



                        int rank = user.getCapability(CapabilityConcentrationRank.RANK_POINT)
                                .map(r -> r.getRank(worldIn.getGameTime()).level).orElse(0);

                        int count = 6;

                        if (IConcentrationRank.ConcentrationRanks.S.level <= rank) {
                            count = 8;
                        }

                    int colorCode = state.getColorCode();
                    for (int i = 0; i < count; i++) {
                            EntityStormSwords ss = new EntityStormSwords(SlashBlade.RegistryEvents.StormSwords,
                                    worldIn);


                            ss.setShooter(user);
                            ss.setColor(colorCode);
                            ss.setRoll(0);
                            ss.setDamage((getPlayerDamage(user)+persistentData.getDouble("totalDamage")*0.1)*1.5f);
                            // force riding
                            ss.startRiding(target, true);

                            ss.setDelay(360/count*i );
                            worldIn.addFreshEntity(ss);
                            player.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F,
                                    1.45F);
                        }
                        if (target.hasEffect(MobEffects.WITHER) && target.getEffect(MobEffects.WITHER).getAmplifier() >=10){
                            target.removeEffect(MobEffects.WITHER);
                            for (int i = 0; i < 8; i++) {
                                // 计算角度，将圆周分为8份
                                double angle = 2 * Math.PI * i / 8;

                                // 圆的半径
                                double radius = 10;

                                // 计算圆周上的点坐标
                                double offsetX = radius * Math.cos(angle);
                                double offsetY = 1.0; // 可以调整高度
                                double offsetZ = radius * Math.sin(angle);

                                // 创建 EntityDrive 实例
                                EntityDrive entityDrive = new EntityDrive(SlashBlade.RegistryEvents.Drive, user.level());

                                // 设置 EntityDrive 的位置为目标周围的圆周点
                                entityDrive.setPos(target.getX() + offsetX, target.getY() + offsetY, target.getZ() + offsetZ);
                                entityDrive.setRank(rank);
                                entityDrive.setColor(colorCode);
                                entityDrive.setDamage(5f);
                                entityDrive.setShooter(user);
                                entityDrive.setSpeed(2f);
                                entityDrive.setLifetime(60);
                                entityDrive.setPierce((byte) 0);
                                entityDrive.getPersistentData().putBoolean("Star_Fire_drive",true);
                                Vec3 totalVec3 = target.position().subtract(entityDrive.position()).normalize();
                                entityDrive.shoot(totalVec3.x,totalVec3.y,totalVec3.z,entityDrive.getSpeed(),1.0f);
//                                // 设置 EntityDrive 的其他属性（如速度、方向等）
//                                Vec3 motion = new Vec3(offsetX, 0, offsetZ).normalize().scale(0.5); // 调整速度
//                                entityDrive.setDeltaMovement(motion);

                                // 将 EntityDrive 添加到世界中
                                user.level().addFreshEntity(entityDrive);
                            }
                            persistentData.remove("totalDamage");

                        }

                }else {
                    persistentData.putInt("Star_tag",
                            persistentData.getInt("Star_tag") + 1);
                }
            }else {
                persistentData.putInt("Star_tag",
                        1);
            }
        }
    }
    @SubscribeEvent
    public static void DoSlash(SlashBladeEvent.DoSlashEvent event) {
        LivingEntity user = event.getUser();
        if (user instanceof Player player) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (!SpecialEffectEx.hasSpecialEffect2(mainHandItem, LSSpecialEffectRegistry.STAR_FIRE.getId(), player))return;

            ISlashBladeState slashBladeState = event.getSlashBladeState();
            CompoundTag persistentData = player.getPersistentData();

            if (event.getRoll() != persistentData.getFloat("lastRoll")) {

                Legendary_slash.queueServerWork(2, () -> {
                    persistentData.putFloat("lastRoll", event.getRoll()+90);
                            AttackManager.doSlash(player, event.getRoll()+90, slashBladeState.getColorCode(), Vec3.ZERO, false, event.isCritical(), event.getDamage(), event.getKnockback());
                        });

            }else persistentData.putFloat("lastRoll", -1000);


            mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).map((state) -> {
                ResourceLocation comboSeq = state.getComboSeq();
                if (comboSeq == null) return false;
                LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                if (persistentData.getString("lastComboSeq").equals(comboSeq.toString())){
                //    persistentData.putString("lastComboSeq","");
                    return false;
                }

                    Legendary_slash.queueServerWork(2, () -> {
                        ComboState value = ComboStateRegistry.REGISTRY.get().getValue(comboSeq);


                        state.setLastActionTime(state.getLastActionTime()+1);
                        persistentData.putString("lastComboSeq", comboSeq.toString());
                        value.tickAction(user);

                    });
                    return true;
                });
        }
    }
}

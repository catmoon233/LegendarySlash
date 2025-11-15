package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CareerSkill;
import net.exmo.rough_blade.content.CareerSkillInstant;
import net.exmo.rough_blade.content.ExSkillHelper;
import net.exmo.rough_blade.content.SkillHandle;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.exmo.rough_blade.entity.FallenStarEntity;
import net.exmo.rough_blade.entity.TrackTailEntity;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.AutoInit;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static net.exmo.rough_blade.content.SpecialEffectEx.hasSpecialEffect;

@Mod.EventBusSubscriber
@AutoInit
public class StarDashSa extends CareerSkill {
    public StarDashSa() {
        super("StarDashSa");
        this.CoolDown = 20*28;
        this.Icon = Items.GLOW_INK_SAC ;
        this.LocalDescription = "StarDashSa_d";
    }
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            if (player.level() instanceof ServerLevel serverLevel){
                if (player.getPersistentData().contains("StarDash")){
                    if (player.getPersistentData().getInt("StarDash")>0){
                        player.getPersistentData().putInt("StarDash",player.getPersistentData().getInt("StarDash")-1);
                        if (player.getPersistentData().getInt("StarDash")==0){
                            player.getPersistentData().remove("StarDash");
                        }
                        serverLevel.sendParticles(
                                ParticleTypes.ELECTRIC_SPARK,

                                player.getX(),
                                player.getY()-1,
                                player.getZ(),
                                3,
                                0.2,
                                0.05,
                                0.2,
                                0.15
                        );
                    }

                }
            }
        }
    }

    @Override
    public boolean use(Player player) {
        if (player.level().isClientSide)return false;
        if (super.use(player)){
          //  changeCombo(player.getMainHandItem(), SlashBlade.prefix("none"));

            player.getPersistentData().putInt("StarDash",20);
                //Level level = player.level();
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 0, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 4, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 3, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 5, false, false, false));
            if (player.getAbsorptionAmount() < 20){
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 10);
            }

                StarDashSa.doSlash(player, 5f);
                SkillHandle.sendDashMessage(player, 1, -0.85);

            return true;
        }return false;
    }

    public static void doSlash(LivingEntity livingEntity, float damage){
       // final Vec3 _center = new Vec3(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        Level level = livingEntity.level();

        Random random = new Random();
        int starCount = 5 + random.nextInt(6); // 5-10之间
        
        // 获取玩家视线方向
        Vec3 lookVec = livingEntity.getLookAngle();
        // 计算目标点（玩家视线方向的一定距离）
        Vec3 targetPos = livingEntity.position().add(lookVec.scale(20D));

        Rough_blade.queueServerWork(5,()->{
            ISlashBladeState bladeState = ExUtils.getSlashBlade(livingEntity.getMainHandItem());
            if (bladeState==null)return;

        for (int i = 0; i < starCount; i++) {
            double v = (random.nextDouble() - 0.5) * 4.0D;
            double x = livingEntity.getX() + v;
            double v1 = random.nextDouble() * 1.25D;
            double y = livingEntity.getY() + 2.0D + v1; // 在玩家上方20-30格
            double v2 = (random.nextDouble() - 0.5) * 4.0D;
            double z = livingEntity.getZ() + v2;

            TrackTailEntity trackTailEntity = new TrackTailEntity(RBEntityRegistry.TRACK_TAIL_ENTITY, level);
            trackTailEntity.setPos(x, y, z);
            trackTailEntity.setOffset(new Vec3(v, v1, v2));
            trackTailEntity.setOwner(livingEntity);
            trackTailEntity.setDamage((double) bladeState.getRefine() * 0.07f * (bladeState.getProudSoulCount() * 0.0001 ));
            trackTailEntity.setRoll(0);
            trackTailEntity.setDelay(2);
            trackTailEntity.setIsCritical(false);
            trackTailEntity.setParticle(false);
            trackTailEntity.startRiding(livingEntity,true);
            // 计算朝向目标点的方向
            Vec3 direction = targetPos.subtract(x, y, z).normalize();
            // 给予一定的随机性
            direction = direction.add(
                    (random.nextDouble() - 0.5) * 0.3,
                    (random.nextDouble() - 0.5) * 0.3,
                    (random.nextDouble() - 0.5) * 0.3
            ).normalize();

            // 设置速度
            float speed = (float) (1.0D + random.nextDouble() * 0.5D);
            //trackTailEntity.shoot(direction.x , direction.y, direction.z , speed, 0.0F);
            trackTailEntity.setSpeed(speed);
                level.addFreshEntity(trackTailEntity);
            livingEntity.playSound(SoundEvents.ALLAY_ITEM_GIVEN, 0.5F, 1.45F);

        }
            changeCombo( livingEntity.getMainHandItem(), ComboStateRegistry.COMBO_A3.getId(),livingEntity);
        });
    }
}
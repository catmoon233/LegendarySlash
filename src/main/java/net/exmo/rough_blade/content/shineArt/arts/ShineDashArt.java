package net.exmo.rough_blade.content.shineArt.arts;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.content.shineArt.ShineArt;
import net.exmo.rough_blade.events.OnCharge;
import net.exmo.rough_blade.events.OnCharge2;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;
@Mod.EventBusSubscriber
public class ShineDashArt extends ShineArt {

    public ShineDashArt() {
        super();
    }

    public void applyEffect(LivingEntity entity) {
        super.applyEffect(entity);
        entity.addEffect(new MobEffectInstance(Rough_blade.effectAbout.ArmorStart.get(), 200, 0, false, false, false));
        if (entity instanceof ServerPlayer player){
            player.playNotifySound(SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS,1f,1f);
        }
    }
    @SubscribeEvent
    public static void hit(OnCharge charge){
        if (charge.artsType == SlashArts.ArtsType.Fail) return;
        LivingEntity entity = charge.getEntity();
        if (entity.hasEffect(Rough_blade.effectAbout.ArmorStart.get())) {
            entity.addEffect(new MobEffectInstance(Rough_blade.effectAbout.SHINE_DANCE.get(), 12, 1, false, false, false));
            charge.setCanceled(true);

            if (entity.level() instanceof ServerLevel serverLevel) {
                // 生成圆形粒子
                double radius = 2.0; // 粒子扩散的半径
                int particleCount = 60; // 粒子数量
                for (int i = 0; i < particleCount; i++) {
                    double angle = Math.toRadians(i * (360.0 / particleCount)); // 计算角度
                    double xOffset = Math.cos(angle) * radius; // x 轴偏移
                    double yOffset = Math.sin(angle) * radius; // y 轴偏移

                    // 获取玩家的视角方向
                    if (entity instanceof Player player) {
                        var lookAngle = player.getLookAngle();
                        // 根据视角方向调整粒子位置
                        double adjustedX = entity.getX() + xOffset * lookAngle.x;
                        double adjustedY = entity.getY() + yOffset * lookAngle.y;
                        double adjustedZ = entity.getZ() + xOffset * lookAngle.z;

                        serverLevel.sendParticles(
                                ParticleTypes.END_ROD, // 粒子类型
                                adjustedX, // 粒子的 x 坐标
                                adjustedY, // 粒子的 y 坐标
                                adjustedZ, // 粒子的 z 坐标
                                1, // 粒子数量
                                0, 0, 0, // 粒子的速度（x, y, z）
                                0 // 粒子的速度缩放
                        );
                    } else {
                        // 如果不是玩家，按原逻辑生成粒子
                        serverLevel.sendParticles(
                                ParticleTypes.END_ROD, // 粒子类型
                                entity.getX() + xOffset, // 粒子的 x 坐标
                                entity.getY() + yOffset, // 粒子的 y 坐标
                                entity.getZ(), // 粒子的 z 坐标
                                1, // 粒子数量
                                0, 0, 0, // 粒子的速度（x, y, z）
                                0 // 粒子的速度缩放
                        );
                    }
                }
            }

            if (entity instanceof Player player) {
                player.playNotifySound(SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
                SlashAttackHandle.sendDashMessage(player, player.getLookAngle().y() , 3.5);
            } else {
                SlashAttackHandle.vmove(entity, entity.getLookAngle().y() * 2, 4);
            }
        }
    }

}

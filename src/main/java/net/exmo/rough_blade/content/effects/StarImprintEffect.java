package net.exmo.rough_blade.content.effects;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.UUID;
@Mod.EventBusSubscriber
public class StarImprintEffect extends MobEffect {
    public StarImprintEffect() {
        super(MobEffectCategory.HARMFUL, new Color(138, 43, 226).getRGB()); // 紫色
    }
    @SubscribeEvent
    public static void Hurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(Rough_blade.effectAbout.StarImprintEffect.get())){
            event.setAmount((float) (event.getAmount()*(1+(0.1*entity.getEffect(Rough_blade.effectAbout.StarImprintEffect.get()).getAmplifier()))));
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 持续时间检查，确保效果正常工作
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        
        if (amplifier >= 4) {
            entity.removeEffect(this); // 移除效果
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        
        // 当效果被移除时检查是否因为叠加到5层而触发
        if (amplifier >= 4) {
            explode(entity);
        }
    }

    private void explode(LivingEntity entity) {

        if (entity.level() instanceof ServerLevel serverLevel) {
            // 创建粒子效果
            serverLevel.sendParticles(
                    ParticleTypes.GLOW_SQUID_INK,
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(),
                    20,
                    1.5, 1.5, 1.5,
                    0.3
            );

            // 定义影响范围（3格半径）
            AABB explosionArea = new AABB(
                    entity.getX() - 3, entity.getY() - 3, entity.getZ() - 3,
                    entity.getX() + 3, entity.getY() + 3, entity.getZ() + 3
            );

            String string = entity.getPersistentData().getString("star_imprint_uuid");
            if (string.isEmpty()) {
                return;
            }
            Player player = entity.level().getPlayerByUUID(UUID.fromString(string));
            if (player == null) {
                return;
            }
            if (player instanceof ServerPlayer serverPlayer){
                serverPlayer.playNotifySound(SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 0.8F, 1.45F);
            }
            ItemStack mainHandItem = player.getMainHandItem();
            ISlashBladeState slashBlade = ExUtils.getSlashBlade(mainHandItem);
            if (slashBlade ==null){
                return;
            }
            // 获取范围内所有实体
            int radius = 3;
            var entities = serverLevel.getEntities(player, AABB.ofSize(entity.position(), radius * 2, radius * 2, radius * 2));
            entities.forEach(target -> {
                if (target instanceof LivingEntity livingTarget && target != player) {
                    // 计算距离衰减
                    double distance = target.distanceTo(entity);
                    if (distance <= 3.0) {
                        // 应用失明效果（2秒）
                        livingTarget.addEffect(
                                new net.minecraft.world.effect.MobEffectInstance(
                                        MobEffects.BLINDNESS, 40, 0, false, true, true
                                )
                        );


                        // 如果使用者是玩家，计算基于攻击力的伤害
                            double attackDamage = player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
                            float magicDamage = (float) (attackDamage * 5.2 * slashBlade.getRefine()*0.07*slashBlade.getProudSoulCount()*0.00025); // 520%攻击力的魔法伤害

                            // 使用魔法伤害类型
                            net.minecraft.world.damagesource.DamageSource magicSource = new net.minecraft.world.damagesource.DamageSource(
                                    serverLevel.registryAccess()
                                            .registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE)
                                            .getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC),
                                    player
                            );

                            // 对目标造成魔法伤害
                            livingTarget.hurt(magicSource, magicDamage);

                    }
                }
            });
        }
    }
}
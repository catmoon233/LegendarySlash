package net.exmo.rough_blade.content.specialEffects;

import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CareerSkillInstant;
import net.exmo.rough_blade.content.ExSkillHelper;
import net.exmo.rough_blade.content.SkillHandle;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.entity.EntityDrivePlus;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.exmo.rough_blade.utils.PathGenerator;
import net.minecraft.client.Minecraft;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static net.exmo.rough_blade.content.SlashAttackHandle.sendDashMessage;
import static net.exmo.rough_blade.content.SlashAttackHandle.vmove;

@Mod.EventBusSubscriber
public class MingLiSe extends SpecialEffectEx {
    public MingLiSe() {
        super(0);
    }
    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent event){
        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD))return;
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer){
            ItemStack mainHandItem = serverPlayer.getMainHandItem();
            if (SpecialEffectEx.hasSpecialEffect2(mainHandItem, RBSpecialEffectRegistry.MingLi.getId(),serverPlayer)){
                 LivingEntity entity = event.getEntity();
                ISlashBladeState iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                iSlashBladeState.setMaxDamage(60);

                if (iSlashBladeState.getComboSeq().equals(ComboStateRegistry.COMBO_A4_Plus.getId()) && event.getSource().getDirectEntity() instanceof EntityDrivePlus){
                     if (Math.random() <0.5f)   iSlashBladeState.setMaxDamage(iSlashBladeState.getMaxDamage()-1);
                    else iSlashBladeState.setMaxDamage(Math.min(60,iSlashBladeState.getMaxDamage()+1));
                    StunManager.setStun(entity,20);
                    serverPlayer.addEffect(new MobEffectInstance(Rough_blade.effectAbout.GuardEffectSuc.get(), 20,5));
                    Vec3 position = entity.getOnPos().getCenter();
                    Vec3 playerPosition = serverPlayer.getOnPos().getCenter();
                    List<Vec3> vl = PathGenerator.generatePath(position, playerPosition);
                    //serverPlayer.teleportTo(position.x(), position.y()+0.75, position.z());
                    if (entity instanceof Player player ) {
                        sendDashMessage(player,0.3,-0.35);
                    }else {
                        vmove(entity,0.3,-0.35);
                    }
                    for (Vec3 v : vl){

                        if (serverPlayer.level() instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.SOUL, v.x(), v.y()+1, v.z(), 2, 0.3, 0.3, 0.3, 0.2);
                            {
                                final Vec3 _center = v;
                                List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                for (Entity entityiterator : _entfound) {
                                    if (entityiterator != serverPlayer) {
                                            if (entityiterator instanceof LivingEntity) {
                                                serverPlayer.heal(1);
                                                entityiterator.invulnerableTime =0;
                                                entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD), serverPlayer), (float) (serverPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE)*2.5f));

                                            }

                                    }
                                }
                            }
                        }
                    }
                }
                mainHandItem.setDamageValue(mainHandItem.getDamageValue()+1);
                if (mainHandItem.getDamageValue()>60) mainHandItem.setDamageValue(60);
                if (MingLiEffectHandle.isZhenLi( entity))return;

                if (MingLiEffectHandle.isMingLi( entity)){
                     int mingLi = MingLiEffectHandle.getMingLi(entity);
                     if (mingLi<99){
                         MingLiEffectHandle.addMingLi(entity, 1);
                     }


                 }else MingLiEffectHandle.addMingLi(entity, 1);
             }
        }
    }
    public static final UUID MING_LI_UUID = UUID.fromString("f0c0c3c0-c0c0-c0c0-c0c1-c2c0c0c0c0c0");
    @SubscribeEvent
    public static void MulitAttribute(ItemAttributeModifierEvent event){
        if (event.getItemStack().getItem() instanceof ItemSlashBlade slashBlade){
            if (hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.MingLi.getId())){
                ExSkillHelper exSkillHelper = ExSkillHelper.of(event.getItemStack());
                if (exSkillHelper.getSkillsSize()==0) {
                 //   exSkillHelper.addSkill(CareerSkillInstant.of(SkillHandle.getSkill("ZhenLiV"),1),true);
                    exSkillHelper.addSkill(CareerSkillInstant.of(SkillHandle.getSkill("ZhenLiSa"),1),true);
                }

                if (event.getSlotType()==  EquipmentSlot.MAINHAND){
                    event.addModifier(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(MING_LI_UUID, "MingLi", 10, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }

}

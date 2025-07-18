package net.exmo.rough_blade.content.effects;

import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.content.client.ClientPacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraftforge.common.ForgeMod;

import java.awt.*;

public class LSArmorStart extends MobEffect {
    public LSArmorStart() {
        super(MobEffectCategory.BENEFICIAL, Color.red.getRGB());
        addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "9D0D0D0D-5143-4D69-B5F9-EFE32EB66D0F", -0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "9D0D1D0D-5143-4D69-B5F9-EFE32EB66D0F", 2.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap p_19479_, int p_19480_) {

        super.addAttributeModifiers(livingEntity, p_19479_, p_19480_);
        if (livingEntity instanceof ServerPlayer player){
          //  player.noPhysics = true;
        }
    }
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap p_19470_, int p_19471_) {
        super.removeAttributeModifiers(entity, p_19470_, p_19471_);
        if (entity instanceof ServerPlayer player){
            if (!player.isSpectator()){
               // player.noPhysics = false;
            }
        }
    }


    @Override
    public void applyEffectTick(LivingEntity livingEntity, int p_19468_) {
        super.applyEffectTick(livingEntity, p_19468_);
        if (livingEntity.isFallFlying()){
            if (livingEntity instanceof Player player){
                SlashAttackHandle.sendDashMessage(player,livingEntity.getLookAngle().y *0.1,0.01);
            }else {
                SlashAttackHandle.vmove(livingEntity,livingEntity.getLookAngle().y *0.1,0.01);
            }
        }

    }
}

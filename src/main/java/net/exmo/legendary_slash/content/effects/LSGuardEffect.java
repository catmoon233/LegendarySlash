package net.exmo.legendary_slash.content.effects;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.exmo.legendary_slash.Legendary_slash;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LSGuardEffect extends MobEffect {

    public LSGuardEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.red.getRGB());
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int p_19468_) {
        super.applyEffectTick(livingEntity, p_19468_);
        if (!livingEntity.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())){
            if (livingEntity instanceof ServerPlayer player){
                if (!player.onGround()){

                }
                Vec3 motion = new Vec3(0, 0, 0);
                player.setDeltaMovement(0,0,0);
                player.connection
                        .send(new ClientboundSetEntityMotionPacket(player.getId(), motion.scale(0.75f)));
                ItemStack mainHandItem = player.getMainHandItem();
                if (mainHandItem.getItem() instanceof ItemSlashBlade itemSlashBlade){
                    ISlashBladeState iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                    iSlashBladeState.updateComboSeq(livingEntity,new ResourceLocation(SlashBlade.MODID, "none"));
                }
            }else {
                livingEntity.setDeltaMovement(0, 0, 0);
            }


        }
    }
    public static void doTeleport(Entity entityIn, Entity target) {
        entityIn.getPersistentData().putInt("sb.airtrick.counter", 1);
        entityIn.getPersistentData().putInt("sb.airtrick.target", target.getId());
        if (entityIn instanceof ServerPlayer) {
            Vec3 motion = target.getPosition(1.0f).subtract(entityIn.getPosition(1.0f)).scale(0.5f);
            ((ServerPlayer) entityIn).connection.send(new ClientboundSetEntityMotionPacket(entityIn.getId(), motion));
        }
    }
}

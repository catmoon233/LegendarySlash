package net.exmo.rough_blade.content.effects;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.exmo.rough_blade.Rough_blade;
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
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class LSGuardEffect extends MobEffect {

    public LSGuardEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.red.getRGB());
    }
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    //4-> 来自命理
    @Override
    public void applyInstantenousEffect(@Nullable Entity p_19462_, @Nullable Entity p_19463_, LivingEntity p_19464_, int p_19465_, double p_19466_) {
        super.applyInstantenousEffect(p_19462_, p_19463_, p_19464_, p_19465_, p_19466_);
        if (p_19464_.getMainHandItem().getItem() instanceof ItemSlashBlade){
            UserPoseOverrider.resetRot(p_19464_);
        }
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int p_19468_) {
        super.applyEffectTick(livingEntity, p_19468_);
        if (!livingEntity.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())){
            if (livingEntity instanceof ServerPlayer player){
                if (p_19468_ == 4) return;
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

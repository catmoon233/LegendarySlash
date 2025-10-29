package net.exmo.rough_blade.mixin.guard;

import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(AttackManager.class)
public class AttackManger2 {
    @Inject(at = @At(value = "RETURN"), method = "doSlash(Lnet/minecraft/world/entity/LivingEntity;FILnet/minecraft/world/phys/Vec3;ZZDLmods/flammpfeil/slashblade/util/KnockBacks;)Lmods/flammpfeil/slashblade/entity/EntitySlashEffect;",remap = false
    )
    private static void doSlash$LS1(LivingEntity playerIn, float roll, int colorCode, Vec3 centerOffset, boolean mute, boolean critical, double comboRatio, KnockBacks knockback, CallbackInfoReturnable<EntitySlashEffect> cir) {
        EntitySlashEffect jc = cir.getReturnValue();
        if (jc != null) {
            if (playerIn instanceof ServerPlayer player) {
                ItemStack mainHandItem = player.getMainHandItem();
                if (playerIn.hasEffect(Rough_blade.effectAbout.GuardEffectSuc.get())) {
                    if (SpecialEffectEx.hasSpecialEffect2(mainHandItem, RBSpecialEffectRegistry.STAR_FIRE.getId(), player)) {
                        jc.setBaseSize(3);
                        jc.setColor(Color.red.getRGB());
                        jc.setDamage(jc.getDamage() * 5);
                        jc.setLifetime(jc.getLifetime() * 2);
                    }
                }
                if (SpecialEffectEx.hasSpecialEffect2(mainHandItem, RBSpecialEffectRegistry.MingLi.getId(), player)){
                    ComboStateRegistry.doBigDriveSlash3(player, player.level(), roll,  3);
                  //  ISlashBladeState iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));

                }
            }
        }
    }
}

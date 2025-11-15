package net.exmo.rough_blade.mixin.mingli;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.exmo.rough_blade.init.ComboStateRegistry.summonSword;

@Mixin(ItemSlashBlade.class)
public abstract class OnBladeLeftClick {
     @Shadow public abstract int getDamage(ItemStack stack);

    @Shadow public abstract void setDamage(ItemStack stack, int damage);

    @Inject(at = @At("HEAD"), method = "onLeftClickEntity", remap = false, cancellable = true)
    public void onLeftClickEntity(ItemStack stack, Player player, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (SpecialEffectEx.hasSpecialEffect(stack, RBSpecialEffectRegistry.MingLi.getId())){
            stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                if (state.isBroken()) {
                    if (!state.getComboSeq().equals(ComboStateRegistry.MLZhan.getId()) ) state.updateComboSeq(player,ComboStateRegistry.MLZhan.getId());
                    state.setBroken(false);
                }
                if (state.getComboSeq().equals(SlashBlade.prefix("void_slash_sheath"))|| state.getComboSeq().equals(Rough_blade.prefix("zhen_li"))){
                    cir.setReturnValue(false);
                    return;
                }
                if (getDamage(stack)>=60){
                    if (!state.getComboSeq().equals(ComboStateRegistry.MLZhan.getId()) ) state.updateComboSeq(player,ComboStateRegistry.MLZhan.getId());
                }else {
//                Vec3 movePos = entity.position().add(entity.position().subtract(player.position()).scale(0.05f));
//                Vec3 normalize = player.getEyePosition(2).subtract(player.getEyePosition(0));
//                movePos = movePos.add(normalize);
                    player.addEffect(new MobEffectInstance(Rough_blade.effectAbout.GuardEffect.get(), 5, 4, false, false, false));
                    player.heal(0.1F);
//                player.setPos(movePos.x,movePos.y,movePos.z);
                    if (player instanceof ServerPlayer serverPlayer) {
                        SlashAttackHandle.sendDashMessage(serverPlayer, 0, -0.02);
//                    serverPlayer.addDeltaMovement(normalize);
//                    serverPlayer.connection
//                            .send(new ClientboundSetEntityMotionPacket(player.getId(), normalize.scale(0.75f)));

                    }
                }
            });


        }
    }

    @Inject(at = @At("HEAD"), method = "mineBlock", cancellable = true)
    public void onLeftClickBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving, CallbackInfoReturnable<Boolean> cir) {
        if (SpecialEffectEx.hasSpecialEffect(stack, RBSpecialEffectRegistry.MingLi.getId())){

             cir.setReturnValue(false);

        }
    }
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void ise(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (handIn==InteractionHand.OFF_HAND)return;

        ItemStack stack = playerIn.getItemInHand(handIn);
        if (SpecialEffectEx.hasSpecialEffect(stack, RBSpecialEffectRegistry.MingLi.getId())){
            var iSlashBladeState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(stack));
            if (iSlashBladeState.isBroken()) {
                if (!iSlashBladeState.getComboSeq().equals(ComboStateRegistry.MLZhan.getId()) ) iSlashBladeState.updateComboSeq(playerIn,ComboStateRegistry.MLZhan.getId());
                iSlashBladeState.setBroken(false);
                iSlashBladeState.setDamage(0);
            }
            if ( iSlashBladeState.getComboSeq().equals(Rough_blade.prefix("zhen_li"))){
                cir.setReturnValue(InteractionResultHolder.fail(stack));
                return;
            }
            if (getDamage(stack)<60){


                if (  !iSlashBladeState.getComboSeq().equals(ComboStateRegistry.MLZhan.getId())) {
//                    if (!playerIn.hasEffect(Rough_blade.effectAbout.ZhenLiEffect.get())) {
                        if (!iSlashBladeState.getComboSeq().equals(ComboStateRegistry.COMBO_A4_Plus.getId()) &&!iSlashBladeState.getComboSeq().equals(mods.flammpfeil.slashblade.registry.ComboStateRegistry.AERIAL_CLEAVE.getId()) && !iSlashBladeState.getComboSeq().equals(ComboStateRegistry.MLZhan.getId()))
                            iSlashBladeState.updateComboSeq(playerIn,ComboStateRegistry.COMBO_A4_Plus.getId());

//                    }
//                    iSlashBladeState.setComboSeq(SlashBlade.prefix("none"));
//                    playerIn.addEffect(new MobEffectInstance(Rough_blade.effectAbout.GuardEffect.get(), 5, 4, false, false, false));
                }
                cir.setReturnValue(InteractionResultHolder.fail(stack));
            }else if (getDamage(stack)>=60){
                iSlashBladeState.setDamage(0);
                if (!iSlashBladeState.getComboSeq().equals(ComboStateRegistry.MLZhan.getId()) ) iSlashBladeState.updateComboSeq(playerIn,ComboStateRegistry.MLZhan.getId());

            }
        }
    }
}

package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.ability.SummonedSwordArts;

import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.exmo.rough_blade.init.ComboStateRegistry.summonSword;

@Mixin(SummonedSwordArts.class)
public abstract class SummonSwordArtMixin {


    @Inject(remap = false,at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;" ,ordinal = 1), method = "onInputChange")
    public void onInputChange(InputCommandEvent event, CallbackInfo ci) {
//        ServerPlayer sender = event.getEntity();
//
//        ItemStack blade = sender.getMainHandItem();
//        if (!SpecialEffectEx.hasSpecialEffect2(blade, LSSpecialEffectRegistry.MingLi.getId(), sender))return;
//
////        int powerLevel = blade
////                .getEnchantmentLevel(Enchantments.POWER_ARROWS);
//        blade.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
//
//            summonSword(state, sender, blade.getEnchantmentLevel(Enchantments.POWER_ARROWS),true);
//            //summonSword(state, sender, powerLevel);
//
//        });
    }


}

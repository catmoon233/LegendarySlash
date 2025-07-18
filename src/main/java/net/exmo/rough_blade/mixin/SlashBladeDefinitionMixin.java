package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.exmo.rough_blade.content.shineArt.ShineArt;
import net.exmo.rough_blade.network.ChargePowerState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlashBladeDefinition.class)
public class SlashBladeDefinitionMixin {
    @Inject(at = @At("RETURN"), method = "getBlade(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/ItemStack;",remap = false)
    public void getBlade(Item bladeItem, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnValue = cir.getReturnValue();
        returnValue.getCapability(ShineArt.BLADESTATE_PLUST).orElse(new ChargePowerState(returnValue));
    }

}

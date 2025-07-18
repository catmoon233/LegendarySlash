package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static mods.flammpfeil.slashblade.item.ItemSlashBlade.BLADESTATE;
import static mods.flammpfeil.slashblade.item.ItemSlashBlade.INPUT_STATE;

@Mixin(ItemSlashBlade.class)
public class VibratingToolFunctionMixin {
//    @Inject(at = @At("HEAD"), method = "use", remap = false)
//    public void use(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
//    {
//
//        ItemStack itemstack = playerIn.getItemInHand(handIn);
//
//        Optional<Boolean> result = itemstack.getCapability(BLADESTATE).map((state) -> {
//            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().add(InputCommand.R_CLICK));
//            ResourceLocation combo = state.progressCombo(playerIn);
//            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().remove(InputCommand.R_CLICK));
//            if (!combo.equals(ComboStateRegistry.NONE.getId())) {
//                playerIn.swing(handIn);
//            }
//            return true;
//        });
//    }
}

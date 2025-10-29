package net.exmo.rough_blade.mixin.shineart;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.shineArt.ShineArt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSlashBlade.class)
public class ItemBladeMixin {
    @Inject(at = @At("HEAD"), method = "inventoryTick")
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci)
    {

        if (!(entityIn instanceof Player))return;
        stack.getCapability(ShineArt.BLADESTATE_PLUST).map((state) -> {
            if(isSelected)
                state.sendChanges(entityIn);
            return true;
        });

    }
    @Inject(at = @At("HEAD"), method = "use")
    public void use(Level worldIn, Player playerIn, InteractionHand handIn, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        itemstack.getCapability(ShineArt.BLADESTATE_PLUST).map((state) -> {
            state.setHasChangedActiveState(true);
            return true;
        });
    }
}

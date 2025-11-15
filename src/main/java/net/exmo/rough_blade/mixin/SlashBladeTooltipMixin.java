package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.ExHelper;
import net.exmo.rough_blade.content.specialEffects.tooltip.SlashTypeTooltipHandle;
import net.exmo.rough_blade.content.specialEffects.tooltip.SpecialEffectsSeTooltipHandle;
import net.exmo.rough_blade.utils.ExUtils;
import net.exmo.rough_blade.utils.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemSlashBlade.class)
public abstract class SlashBladeTooltipMixin {

    @Shadow public abstract ResourceLocation getBladeId(ItemStack stack);

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",shift = At.Shift.AFTER), method = "lambda$appendSpecialEffects$26",remap = false)
    private static void addTooltip(Player player, List<Component> tooltip, ResourceLocation se, CallbackInfo ci) {
        if (SpecialEffectsSeTooltipHandle.hasTranslation( se)){
           tooltip.addAll(SpecialEffectsSeTooltipHandle.getTooltip( se));
        }
    }
    @Inject(at = @At(value = "HEAD"), method = "appendSwordType", cancellable = true,remap = false)
    private void addTooltip(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        ResourceLocation bladeId = this.getBladeId(stack);
        if (SlashTypeTooltipHandle.hasTranslation(bladeId)){
           tooltip.addAll(SlashTypeTooltipHandle.getTooltip( bladeId));
           ci.cancel();
        }
    }
}

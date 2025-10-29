package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.ExHelper;
import net.exmo.rough_blade.content.specialEffects.tooltip.SpecialEffectsSeTooltipHandle;
import net.exmo.rough_blade.utils.ExUtils;
import net.exmo.rough_blade.utils.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemSlashBlade.class)
public class SlashBladeTooltipMixin {

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), method = "lambda$appendSpecialEffects$30")
    private static void addTooltip(List<Component> tooltip, Player player, ResourceLocation se, CallbackInfo ci) {
        if (SpecialEffectsSeTooltipHandle.hasTranslation( se)){
           tooltip.addAll(SpecialEffectsSeTooltipHandle.getTooltip( se));
        }
    }
}

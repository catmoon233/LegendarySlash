package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.ability.ArrowReflector;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowReflector.class)
public class ComboStateFix2 {
    @Inject(at = @At("HEAD"), method = "doTicks", cancellable = true,remap = false)
    private static void doTicks(LivingEntity attacker, CallbackInfo ci) {
        attacker.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((s) -> {

            ResourceLocation current = s.resolvCurrentComboState(attacker);
        ComboState oldCS = (ComboState)((IForgeRegistry) ComboStateRegistry.REGISTRY.get()).getValue(current);
        if (oldCS==null)ci.cancel();
    });
    }
}

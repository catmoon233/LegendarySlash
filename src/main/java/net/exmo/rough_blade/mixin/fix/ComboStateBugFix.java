package net.exmo.rough_blade.mixin.fix;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SlashBladeState.class)
public abstract class ComboStateBugFix implements ISlashBladeState {
    @Override
    public void updateComboSeq(LivingEntity entity, ResourceLocation loc) {
        if ((ComboState)((IForgeRegistry) ComboStateRegistry.REGISTRY.get()).getValue(loc)==null)loc = new ResourceLocation(SlashBlade.MODID,"none");
        ISlashBladeState.super.updateComboSeq(entity, loc);
    }

    //    @Inject(method = "updateComboSeq", at = @At(value = "INVOKE",
//            target = "Lmods/flammpfeil/slashblade/registry/combo/ComboState;clickAction(Lnet/minecraft/world/entity/LivingEntity;)V",
//            shift = At.Shift.BEFORE),cancellable = true,
//            locals = LocalCapture.CAPTURE_FAILHARD,
//            remap = false
//    )
//    public void updateComboSeq(LivingEntity entity, ResourceLocation loc, CallbackInfo ci, ComboState cs){
//        if (cs==null)ci.cancel();
//
//
//    }
}

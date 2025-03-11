package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(EntityAbstractSummonedSword.class)
public class SummonedSwordEntityColorMixin {
    @ModifyVariable(at = @At("HEAD"), method = "setColor", remap = false, index = 1, argsOnly = true)
    public int getColor(int value) {
        var o = (EntityAbstractSummonedSword)(Object)this;
        if (o.getPersistentData().contains("boost3") && o.getPersistentData().getBoolean("boost3")){
            return (Color.RED.getRGB());
        }
        if (o.getPersistentData().contains("boost2") && o.getPersistentData().getBoolean("boost2")){
            return (Color.YELLOW.getRGB());
        }
        return value;
    }

}

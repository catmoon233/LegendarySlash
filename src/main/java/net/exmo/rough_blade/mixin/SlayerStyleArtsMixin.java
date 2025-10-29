package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SlayerStyleArts.class)
public class SlayerStyleArtsMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;", ordinal = 0), method = "handleSprintMove")
    public Vec3 onInputChange(Vec3 instance, double p_82491_) {

        return instance.scale(0.3);
    }
}

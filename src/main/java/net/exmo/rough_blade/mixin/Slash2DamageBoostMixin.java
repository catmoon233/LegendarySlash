package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.slasharts.Drive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class Slash2DamageBoostMixin extends net.minecraft.world.entity.projectile.Projectile {


    protected Slash2DamageBoostMixin(EntityType<? extends net.minecraft.world.entity.projectile.Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Inject(at = @At("HEAD"), method = "setOwner")
    public void setOwner(Entity owner, CallbackInfo ci) {
        if (owner instanceof Player player) {
            CompoundTag persistentData = player.getPersistentData();

            if (persistentData.contains("boost3") && persistentData.getInt("boost3")>0){
                getPersistentData().putBoolean("boost3", true);
            }
            if (persistentData.contains("boost2") && persistentData.getInt("boost2")>0){
                getPersistentData().putBoolean("boost2", true);
            }

        }
    }
}

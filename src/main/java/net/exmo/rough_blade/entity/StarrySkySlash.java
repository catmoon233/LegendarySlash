package net.exmo.rough_blade.entity;

import it.hurts.octostudios.octolib.modules.particles.OctoRenderManager;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.Projectile;
import mods.flammpfeil.slashblade.slasharts.Drive;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class StarrySkySlash extends EntityDrive {
    public StarrySkySlash(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);

    }
    public static StarrySkySlash createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new StarrySkySlash(RBEntityRegistry.STARRY_SKY_SLASH, worldIn);
    }
}

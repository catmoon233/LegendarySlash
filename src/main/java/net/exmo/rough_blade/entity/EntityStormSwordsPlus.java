package net.exmo.rough_blade.entity;

import mods.flammpfeil.slashblade.entity.EntityStormSwords;
import mods.flammpfeil.slashblade.entity.Projectile;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class EntityStormSwordsPlus extends EntityStormSwords {
    public EntityStormSwordsPlus(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }
    public static EntityStormSwordsPlus createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new EntityStormSwordsPlus(RBEntityRegistry.STORMSOWRDPLUS, worldIn);
    }

}

package net.exmo.rough_blade.content;


import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.Rough_blade;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Rough_blade.MODID)
public class DimensionTeleportHandler {

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (Config.CANT_OTHER_ENTITY_TELEPORT_TO_DIMENSION.get() )return;
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            event.setCanceled(true);
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

}
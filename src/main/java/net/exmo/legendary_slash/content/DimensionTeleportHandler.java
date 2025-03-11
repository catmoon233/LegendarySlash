package net.exmo.legendary_slash.content;


import net.exmo.legendary_slash.Legendary_slash;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Legendary_slash.MODID)
public class DimensionTeleportHandler {

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            event.setCanceled(true);
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

}
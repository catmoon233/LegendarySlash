package net.exmo.legendary_slash.content.client;


import net.exmo.legendary_slash.network.DashMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;


import static net.exmo.legendary_slash.network.DashMessage.vmove;

public class ClientPacketHandler {
    public static void handledash(DashMessage msg){

        LivingEntity entity = Minecraft.getInstance().player;
        vmove(entity, msg.dy(), msg.dashDistance());
    }
}

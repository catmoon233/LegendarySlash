package net.exmo.rough_blade.init;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.client.LSClientData;
import net.exmo.rough_blade.network.GuardMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RBKeyMappings {
        public static final KeyMapping GUARD = new KeyMapping("key.rough_blade.guard", GLFW.GLFW_KEY_G, "key.category.slashblade") {
            private boolean isDownOld = false;

            @Override
            public void setDown(boolean isDown) {
                super.setDown(isDown);
                if (isDownOld != isDown && isDown) {
                    Rough_blade.PACKET_HANDLER.sendToServer(new GuardMessage(0, 0));
                    GuardMessage.pressAction(Minecraft.getInstance().player, 0, 0);
                }
                isDownOld = isDown;
            }
        };
        public static final KeyMapping HIDE_MODEL = new KeyMapping("key.rough_blade.hide_model", GLFW.GLFW_KEY_EQUAL, "key.category.slashblade") {
            private boolean isDownOld = false;

            @Override
            public void setDown(boolean isDown) {
                super.setDown(isDown);
                if (isDownOld != isDown && isDown) {
                    LSClientData.setHideModel(!LSClientData.isHideModel());
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.translatable("message.rough_blade.hide_model", LSClientData.isHideModel())
                    );
                }
                isDownOld = isDown;

            }
        };
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(GUARD);
        event.register(HIDE_MODEL);

    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                GUARD.consumeClick();
                HIDE_MODEL.consumeClick();

            }
        }
    }
}

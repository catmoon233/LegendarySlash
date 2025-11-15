package net.exmo.rough_blade.content.client;

import net.exmo.rough_blade.Rough_blade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class MoonLightOverlay {
    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        final var player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.hasEffect(Rough_blade.effectAbout.TheInfinityMoonEffect.get())){
                //黑白滤镜
                //淡灰色透明
                event.getGuiGraphics().fill(0, 0, event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(),
                        0x80000000);

            }
        }
    }
}

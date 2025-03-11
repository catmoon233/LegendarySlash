package net.exmo.legendary_slash;

import net.exmo.legendary_slash.content.screen.PowerBarOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)

public class OverlayRegistry {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {


        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "power_overlay", PowerBarOverlay.instance);

    }
}

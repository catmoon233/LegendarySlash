package net.exmo.legendary_slash.content;

import mods.flammpfeil.slashblade.init.SBItems;
import net.exmo.legendary_slash.content.client.SlashBladeIItemDecorator;
import net.exmo.legendary_slash.init.LSEntityRegistry;
import net.exmo.legendary_slash.render.entity.EntityDrivePlusRenderer;
import net.exmo.legendary_slash.render.entity.SummonedSwordPlusRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.exmo.legendary_slash.Legendary_slash.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LSClientEvent {
        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {

            event.registerEntityRenderer(LSEntityRegistry.DRIVEPLUS, EntityDrivePlusRenderer::new);
            event.registerEntityRenderer(LSEntityRegistry.SUMMONEDSWORDPLUS, SummonedSwordPlusRenderer::new);
            event.registerEntityRenderer(LSEntityRegistry.SUMMONEDSWORDPPROLUS, SummonedSwordPlusRenderer::new);


        }
        @SubscribeEvent
        public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
            event.register(SBItems.slashblade, new SlashBladeIItemDecorator());
            event.register(SBItems.slashblade_bamboo, new SlashBladeIItemDecorator());
            event.register(SBItems.slashblade_silverbamboo, new SlashBladeIItemDecorator());
            event.register(SBItems.slashblade_white, new SlashBladeIItemDecorator());
            event.register(SBItems.slashblade_wood, new SlashBladeIItemDecorator());
        }

}

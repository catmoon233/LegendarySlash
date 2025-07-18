package net.exmo.rough_blade.content.specialEffects;

import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

import static net.exmo.rough_blade.content.SpecialEffectEx.hasSpecialEffect;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class MingLiSeClient {
    @SubscribeEvent
    public static void RenderTooltip(RenderTooltipEvent.Pre event){
        if (!hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.MingLi.getId()))return;
        event.setCanceled(true);
        GuiGraphics graphics = event.getGraphics();
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 800);
        graphics.fill(event.getX(), event.getY(), event.getX() + 160, event.getY() + 60, Color.black.getRGB());
        //边框
        graphics.fill(event.getX(), event.getY(), event.getX() + 160, event.getY() + 1, Color.red.getRGB());
        graphics.fill(event.getX(), event.getY() + 59, event.getX() + 160, event.getY() + 60, Color.red.getRGB());
        graphics.fill(event.getX(), event.getY(), event.getX() + 1, event.getY() + 60, Color.red.getRGB());
        graphics.fill(event.getX() + 159, event.getY(), event.getX() + 160, event.getY() + 60, Color.red.getRGB());
        graphics.pose().popPose();
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 1000);
        graphics.pose().scale(2f, 2f, 2f);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable("tooltip.rough_blade.mingli"), ( (event.getX() + 20)/2), ( (event.getY() + 20)/2), Color.red.getRGB());
    }

}

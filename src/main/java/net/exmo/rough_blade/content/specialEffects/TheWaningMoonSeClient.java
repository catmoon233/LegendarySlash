package net.exmo.rough_blade.content.specialEffects;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TheWaningMoonSeClient extends SpecialEffectEx {

    public TheWaningMoonSeClient(int requestLevel) {
        super(requestLevel);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void tooltipRender(RenderTooltipEvent.Color event){
        if (event.getItemStack().getItem() instanceof ItemSlashBlade){
            if (SpecialEffectEx.hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.The_Waning_Moon.getId())) {
                event.setBackground(Color.WHITE.getRGB());
            }
        }
    }
}

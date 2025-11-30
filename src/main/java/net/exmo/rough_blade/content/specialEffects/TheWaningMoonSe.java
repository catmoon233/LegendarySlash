package net.exmo.rough_blade.content.specialEffects;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber
public class TheWaningMoonSe extends SpecialEffectEx {

    public TheWaningMoonSe(int requestLevel) {
        super(requestLevel);
    }


}

package net.exmo.rough_blade.content;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.init.RBBuiltInRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class SlashTooltipAffix {
    public static String toTransKey(ResourceLocation location){
        return "item.rough_blade."+location.getPath();
    }
    public static final List<String> slash_Slash = List.of(toTransKey(RBBuiltInRegistry.LiuYing.location()));
    @SubscribeEvent
    public static void TooltipFix(ItemTooltipEvent event){
       event.getItemStack().getCapability(ItemSlashBlade.BLADESTATE).map(e -> {
        if (slash_Slash.contains(e.getTranslationKey())){
            event.getToolTip().set(1, Component.translatable("tooltip.rough_blade.recall"));
        }
           return true;
       });

    }
}

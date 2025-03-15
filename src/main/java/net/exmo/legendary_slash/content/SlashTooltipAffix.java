package net.exmo.legendary_slash.content;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.legendary_slash.init.LSBuiltInRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class SlashTooltipAffix {
    public static String toTransKey(ResourceLocation location){
        return "item.legendary_slash."+location.getPath();
    }
    public static final List<String> slash_Slash = List.of(toTransKey(LSBuiltInRegistry.LiuYing.location()));
    @SubscribeEvent
    public static void TooltipFix(ItemTooltipEvent event){
       event.getItemStack().getCapability(ItemSlashBlade.BLADESTATE).map(e -> {
        if (slash_Slash.contains(e.getTranslationKey())){
            event.getToolTip().set(1, Component.translatable("tooltip.legendary_slash.recall"));
        }
           return true;
       });

    }
}

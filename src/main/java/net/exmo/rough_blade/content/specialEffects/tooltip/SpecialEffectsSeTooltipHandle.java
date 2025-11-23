package net.exmo.rough_blade.content.specialEffects.tooltip;

import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.exmo.rough_blade.utils.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mod.EventBusSubscriber
public class SpecialEffectsSeTooltipHandle {
    public static Map<ResourceLocation, String> specialEffectsSeTooltip = new HashMap<>();

    @SubscribeEvent
    public static void reload(AddReloadListenerEvent event){
        specialEffectsSeTooltip.clear();
        specialEffectsSeTooltip.put(
                RBSpecialEffectRegistry.StarrySky.getId(),
                "rough_blade_se_tooltip_starry_sky"
        );
        specialEffectsSeTooltip.put(
                RBSpecialEffectRegistry.The_Waning_Moon.getId(),
                "rough_blade_se_tooltip_the_waning_moon"
        );
        add(RBSpecialEffectRegistry.The_Weak_Power.getId());
        add(RBSpecialEffectRegistry.The_Star_Power.getId());
    }
    public static boolean hasTranslation(ResourceLocation se){
        return specialEffectsSeTooltip.containsKey(se);
    }
    public static String getTranslation(ResourceLocation se){
        return specialEffectsSeTooltip.get(se);
    }
    public static List<Component> getTooltip(ResourceLocation se){
       return TooltipUtil.sprit(Component.translatable(SpecialEffectsSeTooltipHandle.getTranslation(se)));
    }
    public static void add(ResourceLocation se){
        specialEffectsSeTooltip.put(se, "rough_blade_se_tooltip_"+se.getPath());
    }
}

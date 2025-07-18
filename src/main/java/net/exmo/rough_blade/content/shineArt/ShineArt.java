package net.exmo.rough_blade.content.shineArt;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.exmo.rough_blade.network.IChargePowerState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.function.Function;
@Mod.EventBusSubscriber
public class ShineArt  {
    public int power_max;
    public static final Capability<IChargePowerState> BLADESTATE_PLUST = CapabilityManager.get(new CapabilityToken<IChargePowerState>() {
    });


    public ShineArt() {
        super();
    }


    public void applyEffect(LivingEntity entity){



    }
    public Object build (ResourceLocation resourceLocation){
        ShineArtHandle.shineArts.put(resourceLocation,this);
        return this;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void tooltip(ItemTooltipEvent event){
        LazyOptional<IChargePowerState> capability = event.getItemStack().getCapability(ShineArt.BLADESTATE_PLUST);
        if (capability.isPresent()){
            IChargePowerState chargePowerState = capability.orElse(null);
            event.getToolTip().add(Component.translatable("tooltip.rough_blade.shine_art.",chargePowerState.getShineArt().toString()));
        }
    }
}

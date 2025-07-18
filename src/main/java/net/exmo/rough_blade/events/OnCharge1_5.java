package net.exmo.rough_blade.events;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
@Cancelable
public class OnCharge1_5  extends LivingEvent {

    public ISlashBladeState slashBladeState ;
    public SlashArts slashArts ;
    public int chargeTime ;

    public OnCharge1_5(LivingEntity entity, ISlashBladeState slashBladeState, int chargeTime, SlashArts slashArts) {
        super(entity);
        this.slashArts = slashArts;
        this.slashBladeState = slashBladeState;
        this.chargeTime = chargeTime;

    }
}

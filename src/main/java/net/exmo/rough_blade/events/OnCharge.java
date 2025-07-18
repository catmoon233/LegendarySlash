package net.exmo.rough_blade.events;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class OnCharge extends LivingEvent {
    public ISlashBladeState slashBladeState ;
    public SlashArts slashArts ;
    public int chargeTime ;
    public SlashArts.ArtsType artsType;

    public OnCharge(LivingEntity entity, ISlashBladeState slashBladeState, int chargeTime, SlashArts slashArts, SlashArts.ArtsType artsType) {
        super(entity);
        this.slashArts = slashArts;
        this.slashBladeState = slashBladeState;
        this.chargeTime = chargeTime;
        this.artsType = artsType;

    }
}

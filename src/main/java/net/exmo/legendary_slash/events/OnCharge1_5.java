package net.exmo.legendary_slash.events;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
@Cancelable
public class OnCharge1_5  extends PlayerEvent {
    public Player player ;
    public ISlashBladeState slashBladeState ;
    public SlashArts slashArts ;
    public int chargeTime ;

    public OnCharge1_5(Player player, ISlashBladeState slashBladeState, int chargeTime,SlashArts slashArts) {
        super(player);
        this.player = player;
        this.slashArts = slashArts;
        this.slashBladeState = slashBladeState;
        this.chargeTime = chargeTime;

    }
}

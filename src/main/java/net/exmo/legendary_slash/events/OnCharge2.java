package net.exmo.legendary_slash.events;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class OnCharge2 extends PlayerEvent {
    public Player player ;
    public ISlashBladeState slashBladeState ;
    public int chargeTime ;
    public SlashArts slashArts ;

    public OnCharge2(Player player, ISlashBladeState slashBladeState, int chargeTime, SlashArts slashArts) {
        super(player);
        this.player = player;
        this.slashBladeState = slashBladeState;
        this.slashArts = slashArts;
        this.chargeTime = chargeTime;
    }
}

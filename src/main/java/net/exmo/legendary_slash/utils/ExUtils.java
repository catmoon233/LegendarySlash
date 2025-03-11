package net.exmo.legendary_slash.utils;

import net.exmo.legendary_slash.network.LSVARB;
import net.minecraft.world.entity.player.Player;

public class ExUtils {
    public static LSVARB.PlayerVariables getPlayerVariables(Player player) {
        return player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LSVARB.PlayerVariables());
    }
}

package net.exmo.legendary_slash.content.client;

//import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
//import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

public class LSClientData {
    public static boolean charge2 = false;
    public static Player Rdplayer;
    private static boolean hideModel = true;

    public static boolean isHideModel() {
//        if (ModList.get().isLoaded("ftbquests")) {
//            if (ClientUtils.getCurrentGuiAs(QuestScreen.class)!=null){
//                return true;
//            }
//        }
        return hideModel;
    }

    public static void setHideModel(boolean hideModel) {
        LSClientData.hideModel = hideModel;
    }
}

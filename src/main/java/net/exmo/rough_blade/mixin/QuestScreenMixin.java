package net.exmo.rough_blade.mixin;

//import dev.ftb.mods.ftblibrary.ui.BaseScreen;
//import dev.ftb.mods.ftbquests.client.ClientQuestFile;
//import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import net.exmo.rough_blade.content.client.LSClientData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseScreen.class)
public class QuestScreenMixin {
    @Inject( at = @At("HEAD"), method = "onClosed",remap = false)
    public void onClosed(CallbackInfo ci) {
        if (LSClientData.changeHideModel)
        {
            LSClientData.setHideModel(true);
            LSClientData.changeHideModel = false;
        }
    }
    @Inject(at = @At("HEAD"), method = "initGui",remap = false)
    private void openGui(CallbackInfo ci) {
        if (LSClientData.isHideModel()) {
            LSClientData.changeHideModel = true;
            LSClientData.setHideModel(false);
        }
    }
}

package net.exmo.legendary_slash.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.legendary_slash.content.client.LSClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRenderMixin
{
    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean p_115146_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_115149_, int p_115150_, BakedModel p_115151_, CallbackInfo ci
){
        if (!LSClientData.hideModel)return;
        if (ModList.get().isLoaded("ftbquests")){
            if (ClientUtils.getCurrentGuiAs(QuestScreen.class) != null) {
                return;
            }
//                if (ClientQuestFile.exists()){
//                if(ClientQuestFile.INSTANCE.getQuestScreen().isPresent()){
//              //      QuestScreen questScreen = ClientQuestFile.INSTANCE.getQuestScreen().get();
//                }
//            }
        }
        if (itemStack.getItem() instanceof ItemSlashBlade slashBlade) {

            if (itemDisplayContext == ItemDisplayContext.GUI) {
                if (!Screen.hasAltDown()) {
                    ci.cancel();
                }
            }
        }
    }
}

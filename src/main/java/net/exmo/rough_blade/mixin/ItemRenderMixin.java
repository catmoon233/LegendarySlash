package net.exmo.rough_blade.mixin;

import com.mojang.blaze3d.vertex.PoseStack;


import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.content.client.LSClientData;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(ItemRenderer.class)
public abstract class ItemRenderMixin
{
    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean p_115146_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_115149_, int p_115150_, BakedModel p_115151_, CallbackInfo ci
) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (!LSClientData.isHideModel())return;

        if (itemStack.getItem() instanceof ItemSlashBlade slashBlade) {

            if (itemDisplayContext == ItemDisplayContext.GUI) {
                if (!Screen.hasAltDown() && !Config.CLOSE_TEXT_RENDER.get()) {
                    ci.cancel();
                }
            }
        }
    }

}

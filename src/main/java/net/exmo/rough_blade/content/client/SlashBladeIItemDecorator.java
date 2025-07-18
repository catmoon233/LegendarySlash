package net.exmo.rough_blade.content.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

import static net.exmo.rough_blade.Config.*;

public class SlashBladeIItemDecorator implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        if (CLOSE_TEXT_RENDER.get())return false;

        if (Screen.hasShiftDown() || OLD_TEXT_RENDER_ALAWAYS.get() && !NEW_TEXT_RENDER_ALAWAYS.get())return false;
        if (!LSClientData.isHideModel())return false;
        if (!Screen.hasAltDown()){
            // 添加：在物品图标周围绘制耐久边框，颜色根据耐久度从绿到红变化


            Component displayName = itemStack.getDisplayName();
            
            // 去掉displayName的第一个字符和最后一个字符
            String displayNameString = displayName.getString();
            if (displayNameString.startsWith("[") && displayNameString.endsWith("]")) {
                displayNameString = displayNameString.substring(1, displayNameString.length() - 1);
                displayName = Component.literal(displayNameString);
            }

            TextColor color = displayName.getStyle().getColor();
            int value;
            if (color == null) value = 0xFFFFFF;
            else value = color.getValue();

            // 计算文字的原始宽度和高度
            int textWidth = font.width(displayName);
            int textHeight = font.lineHeight;

            // 计算缩放比例，确保文字不超过16x16的框
            float scale = Math.min(16f / textWidth, 16f / textHeight)*1.3f;

            // 应用缩放并渲染文字
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(xOffset -1, yOffset + 7, 200);
            guiGraphics.pose().scale(scale, scale, 1); // 等比例缩放
            guiGraphics.drawString(font, displayName, 0,0 , value);
            guiGraphics.pose().popPose();

            return true;
        }
        return false;
    }
}

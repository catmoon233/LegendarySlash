package net.exmo.rough_blade.content.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

import static net.exmo.rough_blade.Config.*;

public class SlashBladeIItemDamageDecorator implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        if (CLOSE_TEXT_RENDER.get()) return false;

        if (Screen.hasShiftDown()) return false;
        if (!LSClientData.isHideModel()) return false;
        if (!Screen.hasAltDown()) {

            int maxDamage = itemStack.getMaxDamage();
            int damage = itemStack.getDamageValue();
            float durability = (float) (maxDamage - damage) / maxDamage;
            int color = itemStack.getBarColor(); // 获取与原版耐久条一致的渐变色

            guiGraphics.pose().pushPose();
            // 调整Z轴层级，确保边框显示在物品图标之上
            guiGraphics.pose().translate(0, 0, 100);
            
            // 根据耐久度动态计算边框长度（16px基础尺寸乘以耐久比例）
            int length = (int) (16 * durability);
            
            // 绘制动态长度的边框四条边
            guiGraphics.fill(xOffset, yOffset, xOffset + length, yOffset + 1, color); // 上边
            guiGraphics.fill(xOffset, yOffset + 15, xOffset + length, yOffset + 16, color); // 下边
            guiGraphics.fill(xOffset, yOffset, xOffset + 1, yOffset + length, color); // 左边
            guiGraphics.fill(xOffset + 15, yOffset, xOffset + 16, yOffset + length, color); // 右边

            guiGraphics.pose().popPose();
            return true;
        }
        return false;
    }
}

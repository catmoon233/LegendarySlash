
package net.exmo.rough_blade.content.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.network.LSVARB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ExSkillOverlay {
	static int screenHeight;
    static int screenWidth;
    public static Map<Long,Component> lastUpdate = new HashMap<>();
	public static float renderH = 0;
	public static String old_r = "";
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
        Map<Long,Component> toRemove = new HashMap<>();
        lastUpdate.forEach((k,v) -> {
            if (System.currentTimeMillis() - k > 3200) {
                toRemove.put(k,v);
            }
        });
        toRemove.forEach((k,v) -> lastUpdate.remove(k));
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		LocalPlayer entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
        screenWidth = w;
        screenHeight = h;
		Font font = Minecraft.getInstance().font;
        int	centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
        int centerY = screenHeight - Math.max(55, screenHeight / 8);
        LSVARB.PlayerVariables v = entity.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LSVARB.PlayerVariables());

        GuiGraphics gg = event.getGuiGraphics();
		ItemStack mainHandItem = entity.getMainHandItem();
		if (!mainHandItem.isEmpty()){
			LazyOptional<ISlashBladeState> capability = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE, null);
			ISlashBladeState iSlashBladeState = capability.orElse(null);
			String s = "";

			if (capability.isPresent()){
				gg.drawString(font, iSlashBladeState.getProudSoulCount()+" | "+ iSlashBladeState.getKillCount()+s , centerX/2, centerY+40, 0xFFFFFF, false );

			}

		}
        String s = ((String) v.playerSimpleVars.get(4).getValue());
		if (!old_r.equals(s)){
			old_r = s;
			if (!s.isEmpty()){
				renderH = 0;
			}
		}

        if (s.length() > 1){

//			if (renderH> screenHeight*0.15f) renderH = (float) (screenWidth*0.15f);
//			else
				renderH = (float) Math.min(renderH+=0.5f,screenHeight*0.15f);
            String text = I18n.get(s);
			gg.fill(0, 0, screenWidth, (int) renderH, Color.BLACK.getRGB());
			gg.fill(0, screenHeight, screenWidth, (int) (screenHeight-renderH), Color.BLACK.getRGB());
			float progress = renderH / (screenHeight *0.15f) ;
			int width = font.width(text);
			String toRender = font.plainSubstrByWidth(text, (int) (width*progress));
			//centerAndZoom(gg,(int)(screenWidth*0.15),(int)( screenHeight*0.15),width,font.lineHeight,2f,()->{
			int newY = (int) (screenHeight*0.8);
            gg.fill(screenWidth /2 - font.width(toRender)/2-4, newY,  screenWidth /2 + font.width(toRender)/2 , newY+font.lineHeight+2,0x80000000);
			gg.drawString(font, toRender,
							screenWidth /2 - font.width(toRender)/2-2,
                        (int) (newY)+1,
							0xFFFFFF, false);
			// render fill

		//	});
		//	Minecraft.getInstance().cameraEntity.setPos(x,y+2,z);


        }else renderH = Math.max(0,renderH-=0.1f);

		if (Config.COMBO_SCREEN.get()) {
			AtomicInteger line = new AtomicInteger();
// 将 lastUpdate 转换为按时间排序的列表

			lastUpdate.entrySet().stream()
					.sorted(Map.Entry.comparingByKey()) // 按时间戳排序
					.forEach(entry -> {
						//long k = entry.getKey();

						String text = entry.getValue().getString();
						Pattern pattern = Pattern.compile("-(.*?)-");
						Matcher matcher = pattern.matcher(text);


						String toWrite;
						if (matcher.find()) {
							toWrite = matcher.group(1);
						} else {
							toWrite = text;
						}
						gg.drawString(font, toWrite,
								screenWidth - font.width(toWrite),
								(int) (screenHeight * 0.5 + 40 - (font.lineHeight + 2) * line.get()),
								0xFFFFFF, false);
						line.getAndIncrement();
					});
		}
        RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
	public static void centerAndZoom(GuiGraphics graphics, int x, int y, int width, int height, float scale, Runnable drawAction) {
		PoseStack poseStack = graphics.pose();

		// 保存当前状态
		poseStack.pushPose();

		// 平移到中心
		poseStack.translate(x + width / 2.0F, y + height / 2.0F, 0);

		// 缩放
		poseStack.scale(scale, scale, scale);

		// 反向平移到中心
		poseStack.translate(-width / 2.0F / scale, -height / 2.0F / scale, 0);

		// 执行绘制动作
		graphics.pose();
		drawAction.run();

		// 恢复状态
		poseStack.popPose();
	}
}

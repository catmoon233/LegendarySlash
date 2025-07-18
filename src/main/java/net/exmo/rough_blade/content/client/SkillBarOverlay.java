
package net.exmo.rough_blade.content.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.exmo.rough_blade.content.CareerSkill;
import net.exmo.rough_blade.content.CareerSkillInstant;
import net.exmo.rough_blade.content.ExSkillHelper;
import net.exmo.rough_blade.network.CareerWarModVariables;
import net.exmo.rough_blade.utils.UiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class SkillBarOverlay {
	static int screenHeight;
    static int screenWidth;
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
        Player entity = Minecraft.getInstance().player;
        ItemStack mainHandItem = entity.getMainHandItem();
        if (!mainHandItem.hasTag())return;
        ExSkillHelper exSkillHelper = ExSkillHelper.of(mainHandItem);
        List<CareerSkillInstant> skills = exSkillHelper.getSkills();
        if (skills.isEmpty())return;

        int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
//		Level world = null;
//		double x = 0;
//		double y = 0;
//		double z = 0;
//
//		if (entity != null) {
//			world = entity.level();
//			x = entity.getX();
//			y = entity.getY();
//			z = entity.getZ();
//		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
        screenWidth = w;
        screenHeight = h;
//        int centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
//        int centerY = screenHeight - Math.max(55, screenHeight / 8);
        CareerWarModVariables.PlayerVariables v = entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());

        GuiGraphics gg = event.getGuiGraphics();
        //	CareerWarMod.LOGGER.debug(career.LocalName + " rendered" + career.Skills.size());
        var map = new HashMap<Integer,String>();
        for (int i = 0; i < skills.size(); i++) {
            int yOffset = ((int)(i/7))*20;
            event.getGuiGraphics().blit(new ResourceLocation("minecraft:textures/gui/widgets.png"), w  + -189 + i * 20 - yOffset*7, h - 48 + yOffset,  1, 1, 20, 20, 256, 256);
            var careerSkillInstant = skills.get(i);
            var careerSkill = careerSkillInstant.careerSkill;
            if (careerSkill ==null)break;
            {
                if (careerSkill.IconTexture!=null){
                    event.getGuiGraphics().blit(careerSkill.IconTexture, w  + -189 +2+ i * 20, h - 48+2, 1, 1, 16, 16,16,16);
                }else if (careerSkill.Icon!=null) event.getGuiGraphics().renderItem(careerSkill.Icon.getDefaultInstance(), w  + -189 +2+ i * 20, h - 48+2);
            }
            String localName = careerSkill.LocalName;
            if (v.playercooldown.containsKey(localName)){
                if ((v.playercooldown.get(localName)) > 0) {
                //    gg.pose().pushPose();
                //	gg.setColor(1, 1, 1, 0.5f);
                    double v1 = Double.valueOf(v.playercooldown.get(localName)) *0.5;
                    double coolDown =  (careerSkill.CoolDown *0.5);
                    gg.blit(new ResourceLocation("rough_blade:textures/screens/white.png"), w  + -189 + i * 20+2, h - 48+2,  1, 1, 16, (int)((v1 / coolDown) * 16), 16, 16);
               //     gg.pose().popPose();
                //	gg.setColor(1, 1, 1, 1f);
                    double formattedValue = Math.round(((v1 * 0.5) * 0.1) * 10) / 10.0;

                    map.put(i,String.valueOf(formattedValue));
                    //gg.drawString(Minecraft.getInstance().font,, w / 2 + -189 + i * 20+4, h - 48+9, 0x000000, false);

                }
            }
        }

        map.forEach((i,s)->{
            UiUtil.renderLineStr(gg, List.of(String.valueOf(s)), Minecraft.getInstance().font, w + -189 +(i) * 20 + 10, h - 48 + 8, 5123887, 0, UiUtil.displaystyle.center);

        });


        RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}

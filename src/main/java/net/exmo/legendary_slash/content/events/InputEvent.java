package net.exmo.legendary_slash.content.events;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.exmo.legendary_slash.Config;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.init.LSSlashArtRegistry;
import net.exmo.legendary_slash.network.LSVARB;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class InputEvent {
    public static final int need = 15;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInputChange(InputCommandEvent event) {
        if (!Config.POWER.get())return;
        ServerPlayer entity1 = event.getEntity();
        if (event.getCurrent().contains(InputCommand.M_DOWN)) {
            ItemStack mainHandItem = entity1.getMainHandItem();
            if (entity1.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())) {
                if (mainHandItem.getItem() instanceof ItemSlashBlade) {
                    ISlashBladeState iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                iSlashBladeState.setLastActionTime(iSlashBladeState.getLastActionTime()-10);
                    iSlashBladeState.updateComboSeq(entity1, new ResourceLocation(SlashBlade.MODID, "none"));
                    LSSlashArtRegistry.ZJ.get().doArts(SlashArts.ArtsType.Success, entity1);
                }
            }
        }
        for (InputCommand command : InputCommand.move) {
            if (event.getCurrent().contains(InputCommand.SPRINT) && event.getCurrent().contains(command)) {            // 代码逻辑
                LSVARB.PlayerVariables p = entity1.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY).orElse(null);
                if (((Integer) p.playerSimpleVars.get(0).getValue()) >= need) {
                    p.playerSimpleVars.get(0).setValue(Math.max((Integer) p.playerSimpleVars.get(0).getValue() - need, 0));
                } else {
                    event.getCurrent().remove(InputCommand.SPRINT);
                    entity1.sendSystemMessage(Component.translatable("message.legendary_slash.sprint_no_power", need), true);
                }
                break;
            }
        }
    }
}

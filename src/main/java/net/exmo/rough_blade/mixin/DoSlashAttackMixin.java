package net.exmo.rough_blade.mixin;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CustomConfig;
import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.events.OnCharge;
import net.exmo.rough_blade.events.OnCharge1_5;
import net.exmo.rough_blade.events.OnCharge2;
import net.exmo.rough_blade.network.LSVARB;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(SlashArts.class)
public abstract class DoSlashAttackMixin {
    @Shadow(remap = false) public abstract ResourceLocation doArts(SlashArts.ArtsType type, LivingEntity user);


    @Shadow(remap = false) public abstract ResourceLocation getComboState(LivingEntity user);

    @Shadow(remap = false) public abstract Function<LivingEntity, ResourceLocation> getComboStateSuper();

    @Inject(at = @At("HEAD"), method = "doArts", cancellable = true,remap = false)
    public void doArts$LS(SlashArts.ArtsType type, LivingEntity user, CallbackInfoReturnable<ResourceLocation> cir){
        ItemStack mainHandItem = user.getMainHandItem();
        var iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
        var art = ((SlashArts) (Object) this);
        LSVARB.PlayerVariables playerVariables1 = user.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
        if (playerVariables1 == null) return;
        OnCharge event = new OnCharge(user, iSlashBladeState, ((int) playerVariables1.playerSimpleVars.get(1).getValue()),art,type);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()){
            cir.setReturnValue( ComboStateRegistry.NONE.getId());
            cir.cancel();

            return;
        }
        if (!Config.POWER.get()){
            if (type!= SlashArts.ArtsType.Fail) {
                if (user instanceof ServerPlayer player) {
                    MutableComponent mutableComponent = art.getDescription().copy().withStyle(ChatFormatting.WHITE);

                    SlashAttackHandle.sendSkillInfoMessage(player, mutableComponent);
                }
            }
            return;
        };

        if (type==SlashArts.ArtsType.Fail)return;
        if (user instanceof ServerPlayer player){
            LSVARB.PlayerVariables playerVariables = player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);

            int cost = CustomConfig.SlashArtsCostMap.getOrDefault( SlashArts.getRegistryKey(art).toString(), 50);
            int value1 = (int) playerVariables.playerSimpleVars.get(0).getValue();
            if ((!player.getPersistentData().contains("boost2") || !player.getPersistentData().getBoolean("boost2")) && value1 <cost){

             player.sendSystemMessage(Component.translatable("message.rough_blade.slash_arts_no_power", cost,art.getDescription()),true);
                player.playNotifySound(SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS,1,1);
                cir.setReturnValue( ComboStateRegistry.NONE.getId());
                cir.cancel();
            }else {
                int elapsed = ((int) playerVariables1.playerSimpleVars.get(1).getValue());

                if (elapsed >= 22 && elapsed < 25) {
                    int v = (int) (cost * 1.75f);
                    int max = (int) Math.max(0, value1 - v);
                    MutableComponent mutableComponent = art.getDescription().copy().append(" 1.5").withStyle(ChatFormatting.WHITE);
                    MutableComponent append = Component.translatable("message.rough_blade.slash_arts_no_power", v, mutableComponent.withStyle(ChatFormatting.YELLOW));

                    if (value1 <v){
                        player.sendSystemMessage(append,true);
                        player.playNotifySound(SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS,1,1);
                        cir.setReturnValue( ComboStateRegistry.NONE.getId());
                        cir.cancel();
                        return;
                    }
                    SlashAttackHandle.sendSkillInfoMessage(player, mutableComponent);
                    playerVariables.playerSimpleVars.get(0).setValue((int) max);
                    playerVariables.syncPlayerVariables(player);

                    long lastActionTime = iSlashBladeState.getLastActionTime();
                    Runnable runnable = () -> {
                            player.getPersistentData().putInt("boost2", 12);
                        player.getPersistentData().putInt("boost3", 16);
                        ComboState value = ComboStateRegistry.REGISTRY.get().getValue(this.getComboState(user));
                        if (value != null) {
                            AttackManager.playQuickSheathSoundAction(user);
                            if (mainHandItem.getItem() instanceof ItemSlashBlade itemSlashBlade) {
                                Level level = player.level();
                                itemSlashBlade.onUseTick(level, player, mainHandItem, (int) (10));

                                iSlashBladeState.setLastActionTime((long) (lastActionTime + (elapsed - 10)));
                            }
                            iSlashBladeState.updateComboSeq(user, new ResourceLocation(SlashBlade.MODID, "none"));
                            iSlashBladeState.doChargeAction(user, elapsed);
                        }
                    };
                    OnCharge1_5 event1 = new OnCharge1_5(player, iSlashBladeState, elapsed,art);
                   MinecraftForge.EVENT_BUS.post(event1);
                        if (!event1.isCanceled()){
                            Rough_blade.queueServerWork(4, runnable);
                        }

                }
               else if (elapsed >= 25) {
                    float v = cost * 1.5f;
                    int max2 = (int) Math.max(0, value1 - v);
                    MutableComponent mutableComponent = art.getDescription().copy().append(" 2.0").withStyle(ChatFormatting.WHITE);
                    MutableComponent translatable = Component.translatable("message.rough_blade.slash_arts_no_power", v, mutableComponent.withStyle(ChatFormatting.YELLOW));
                    if (value1 <v){
                        player.sendSystemMessage(translatable,true);
                        player.playNotifySound(SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS,1,1);
                        cir.setReturnValue( ComboStateRegistry.NONE.getId());
                        cir.cancel();
                        return;
                    }
                    SlashAttackHandle.sendSkillInfoMessage(player, mutableComponent);
                    playerVariables.playerSimpleVars.get(0).setValue((int) max2);
                    playerVariables.syncPlayerVariables(player);
//                    ItemStack mainHandItem = player.getMainHandItem();
//                    var iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                    OnCharge2 event2 = new OnCharge2(player, iSlashBladeState, elapsed,art);
                    MinecraftForge.EVENT_BUS.post(event2);
                    if (event2.isCanceled())return;
                    long lastActionTime = iSlashBladeState.getLastActionTime();
                    Rough_blade.queueServerWork(5, () -> {
                        player.getPersistentData().putInt("boost2",12);
                        ComboState value = ComboStateRegistry.REGISTRY.get().getValue(this.getComboState(user));
                        if (value!=null){
                            AttackManager.playQuickSheathSoundAction(user);
//                            ComboState.releaseActionQuickCharge(user,elapsed);
//                            Drive.doSlash(player, 0.0F, 10, Vec3.ZERO, false, (double)1.5F, 2.0F);
//                            doArts(type, user);
                  //          AttackManager.doSlash(user, 0.0F, Vec3.ZERO, false, false, 2.0F);

                            if (mainHandItem.getItem() instanceof ItemSlashBlade itemSlashBlade){
                                Level level = player.level();

                                itemSlashBlade.onUseTick(level, player, mainHandItem, (int) (10));

                                iSlashBladeState.setLastActionTime((long) (lastActionTime + (elapsed-10)));
                                //itemSlashBlade.releaseUsing(mainHandItem, level,player, (int) (72000-10));
                            }
                            iSlashBladeState.updateComboSeq(user, new ResourceLocation(SlashBlade.MODID, "none"));
                            iSlashBladeState.doChargeAction(user,elapsed);
                        }
                    });
                }else if (!player.getPersistentData().contains("boost2") || player.getPersistentData().getInt("boost2")<=0){
                   SlashAttackHandle.sendSkillInfoMessage(player,art.getDescription());
                   playerVariables.playerSimpleVars.get(0).setValue(Math.max(0, value1 - cost));
                   playerVariables.syncPlayerVariables(player);
               }
               ;



            }
        }
    }
}

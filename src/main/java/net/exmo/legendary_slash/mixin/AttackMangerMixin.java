package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.init.ComboStateRegistry;
import net.exmo.legendary_slash.init.LSSlashArtRegistry;
import net.exmo.legendary_slash.network.LSVARB;
import net.exmo.legendary_slash.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AttackManager.class)
public class AttackMangerMixin {
    @Inject(at = @At("HEAD"), method = "doVoidSlashAttack",remap = false)
    private static void doVoidSlashAttack(LivingEntity living, CallbackInfo ci) {
        LazyOptional<ISlashBladeState> capability = living.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE);
        if (capability.isPresent()){
            ISlashBladeState iSlashBladeState = capability.orElse(null);
            if (iSlashBladeState.getSlashArts()== LSSlashArtRegistry.ZJ.get()){
                ComboStateRegistry.doBigDriveSlash(living,living.level(),0,18);
                if (living instanceof ServerPlayer player1){
                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player1);
                    List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                    playerSimpleVars.get(4).setValue("slash_art.zj_x");
                    playerVariables.syncPlayerVariables(player1);
                    player1.playNotifySound(SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.PLAYERS,1f,1f);
                    player1.playNotifySound(SoundEvents.LAVA_EXTINGUISH,SoundSource.PLAYERS,1f,1f);
                    player1.playNotifySound(SoundEvents.BUCKET_FILL_LAVA,SoundSource.PLAYERS,1f,1f);
                }
                Legendary_slash.queueServerWork(46,()->{
                    ComboStateRegistry.doBigDriveSlash(living,living.level(),30,20);
                    if (living instanceof ServerPlayer player1){
                        LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player1);
                        List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                        playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                        playerVariables.syncPlayerVariables(player1);
                        player1.playNotifySound(SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.PLAYERS,1f,1f);
                        player1.playNotifySound(SoundEvents.LAVA_EXTINGUISH,SoundSource.PLAYERS,1f,1f);
                        player1.playNotifySound(SoundEvents.BUCKET_FILL_LAVA,SoundSource.PLAYERS,1f,1f);
                    }
                });
            }
        }
    }
}

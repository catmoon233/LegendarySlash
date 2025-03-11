package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.network.SkillInfoMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(AdvancementHelper.class)
public class SkillNameMixin {
    @Inject(at = @At("HEAD"), method = "grantCriterion(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceLocation;)V",remap = false)
    private static void grantCriterion(ServerPlayer player, ResourceLocation resourcelocation, CallbackInfo ci) {
        String[] split = resourcelocation.toString().split("/");
        if (split.length < 2) return;
        SkillInfoMessage msg = new SkillInfoMessage(Component.translatable("adv.slashblade."+split[1]));
        Legendary_slash.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }
}

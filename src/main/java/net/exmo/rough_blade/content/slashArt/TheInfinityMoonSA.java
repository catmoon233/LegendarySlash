package net.exmo.rough_blade.content.slashArt;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.effects.TheInfinityMoonEffect;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class TheInfinityMoonSA {
    public static void use(LivingEntity user) {
        if (user instanceof ServerPlayer serverPlayer) {
            {
                LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(serverPlayer);
                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                playerSimpleVars.get(4).setValue("slash_art.the_infinity_moon");
                playerVariables.syncPlayerVariables(user);
            }

            user.addEffect(new MobEffectInstance(Rough_blade.effectAbout.TheInfinityMoonEffect.get(), 200, 0, false, false));
        }
    }
}

package net.exmo.rough_blade.content.effects;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

import java.awt.*;
import java.util.List;

public class TheInfinityMoonEffect extends MobEffect {
    public TheInfinityMoonEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.WHITE.getRGB());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
        if (p_19469_ instanceof ServerPlayer serverPlayer) {
            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(serverPlayer);
                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.the_infinity_moon")) {
                    playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                    playerVariables.syncPlayerVariables(serverPlayer);
                }
            }

    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
        if (p_19467_ instanceof Player player){
            final var slashBlade = ExUtils.getSlashBlade(player.getMainHandItem());
            if (slashBlade!=null){
                slashBlade.setComboSeq(ComboStateRegistry.VOID_SLASH_SHEATH.getId());
            }
        }

    }
}

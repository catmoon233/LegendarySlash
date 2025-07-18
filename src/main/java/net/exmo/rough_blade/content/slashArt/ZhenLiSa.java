package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.SlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CareerSkill;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.AutoInit;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

@AutoInit
public class ZhenLiSa extends CareerSkill {
    public ZhenLiSa() {
        super("ZhenLiSa");
        this.CoolDown = 20*60;
        this.Icon = Items.GLOWSTONE ;
        this.LocalDescription = "KaiSkill_d";
    }

    @Override
    public boolean use(Player player) {
        if (player.level().isClientSide)return false;
        if (super.use(player)){
          //  changeCombo(player.getMainHandItem(), SlashBlade.prefix("none"));
           changeCombo( player.getMainHandItem(), ComboStateRegistry.Zhen_Li.getId(),player);
                {
                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                    List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                    playerSimpleVars.get(4).setValue("slash_art.zhenli");
                    playerVariables.syncPlayerVariables(player);
                }
                Rough_blade.queueServerWork(200, () -> {
                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                            if (player instanceof ServerPlayer) {
                                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.zhenli")) {
                                    playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                    playerVariables.syncPlayerVariables(player);
                                }
                            }

                        }
                );
                Level level = player.level();

                ZhenLiSa.doSlash(player, 5f);

            return true;
        }return false;
    }

    public static void doSlash(LivingEntity livingEntity, float damage){
        final Vec3 _center = new Vec3(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        Level level = livingEntity.level();
        livingEntity.getPersistentData().putFloat("ZhenLiDamage", (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE));
        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (Entity entityiterator : _entfound) {
            if (entityiterator instanceof LivingEntity le) {
                if (entityiterator != livingEntity){
                    MingLiEffectHandle.addZhenLi( le);

                le.getPersistentData().putString("ZhenLiO", livingEntity.getUUID().toString());
                }}
            }
    }
}

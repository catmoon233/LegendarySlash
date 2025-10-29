package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CareerSkill;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.exmo.rough_blade.init.ComboStateRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.AutoInit;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

import static net.exmo.rough_blade.init.ComboStateRegistry.summonSword;

@AutoInit
public class ZhenLiV extends CareerSkill {
    public ZhenLiV() {
        super("ZhenLiV");
        this.CoolDown = 20;
        this.Icon = Items.IRON_SWORD ;
        this.LocalDescription = "ZhenLiV_d";
    }

    @Override
    public boolean use(Player player) {
        if (player.level().isClientSide)return false;
        if (super.use(player)){


            ItemStack blade = player.getMainHandItem();
            if (!SpecialEffectEx.hasSpecialEffect2(blade, RBSpecialEffectRegistry.MingLi.getId(), player)) {
                return false;
            }

//        int powerLevel = blade
//                .getEnchantmentLevel(Enchantments.POWER_ARROWS);
            blade.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {

                summonSword(state, player, blade.getEnchantmentLevel(Enchantments.POWER_ARROWS),true);
                //summonSword(state, sender, powerLevel);

            });

            return true;
        }return false;
    }


}

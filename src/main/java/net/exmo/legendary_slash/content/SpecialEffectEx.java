package net.exmo.legendary_slash.content;

import com.dinzeer.legendblade.regsitry.slashblade.LBSpecialEffectsRegistry;
import com.exfantasy.mclib.Utils.TriConsumer;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

///
/// 超好用的封装类·se
///
///
public abstract class SpecialEffectEx extends SpecialEffect {
    public SpecialEffectEx(int requestLevel) {
        super(requestLevel);
    }
    static Random random =new Random();
    public static boolean hasSpecialEffect(ItemStack stack, ResourceLocation resourceLocation) {
        if (stack.getItem() != SBItems.slashblade)return false;
        AtomicBoolean _setval = new AtomicBoolean(false);
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
            if(  state.hasSpecialEffect(resourceLocation)) _setval.set(true);
        });

        return _setval.get();
    }
    public static boolean hasSpecialEffect2(ItemStack stack, ResourceLocation resourceLocation,Player player) {
        if (stack.getItem() != SBItems.slashblade)return false;
        AtomicBoolean _setval = new AtomicBoolean(false);
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
            if(  state.hasSpecialEffect(resourceLocation)) _setval.set(true);
        });
        int level=player.experienceLevel;
        if (!SpecialEffect.isEffective(resourceLocation, level)) {
            return false;
        }
        return _setval.get();
    }
    public static double getPlayerDamage(LivingEntity player) {
        if (player.getAttribute(Attributes.ATTACK_DAMAGE)!=null) return  player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        return 0f;
    }

    public static int randomInt(int a){

        return random.nextInt(a);
    }
    public static ResourceLocation getEnchantmentID(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }
    public static void hurtEventHandle(LivingHurtEvent event, BiConsumer<Player,LivingEntity> consumer){
        if (event.getSource().getEntity() instanceof Player player){
            consumer.accept(player,event.getEntity());
        }
    }
    public static void hurtEventHandleSe(LivingHurtEvent event, TriConsumer<Player,LivingEntity, ISlashBladeState> consumer, ResourceLocation resourceLocation){
        if (event.getSource().getEntity() instanceof Player player){
            ItemStack mainHandItem = player.getMainHandItem();
            if (hasSpecialEffect2(mainHandItem,resourceLocation,player)) {
                consumer.accept(player, event.getEntity(),mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(null));
            }
        }
    }
    public static void hurtEventAmountBase(LivingHurtEvent event,float amount){

        event.setAmount(event.getAmount() *( 1+amount));
    }

}

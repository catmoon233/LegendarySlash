package net.exmo.rough_blade.content.item;

import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.content.SlashTooltipAffix;
import net.exmo.rough_blade.content.shineArt.ShineArt;
import net.exmo.rough_blade.entity.ShineCoreItemEntity;
import net.exmo.rough_blade.init.RBBuiltInRegistry;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
@Mod.EventBusSubscriber
public class ShineCoreItem extends Item {
    public ShineCoreItem(Properties p_41383_) {
        super(p_41383_);
    }
    @javax.annotation.Nullable
    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        ShineCoreItemEntity e = new ShineCoreItemEntity(RBEntityRegistry.ShineCoreEntity, world);
        e.restoreFrom(location);
        return e;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        components.add(Component.translatable("tooltip.rough_blade.shine_core"));
        String string = stack.getOrCreateTag().getString("blade_id");
        MutableComponent bladeId ;
        if (string.isEmpty()){
           bladeId = Component.translatable("tooltip.rough_blade.empty");
        }else {
          bladeId =  Component.translatable(string);
        }

        components.add(Component.translatable("tooltip.rough_blade.shine_core.1").append(bladeId));
        super.appendHoverText(stack, p_41422_, components, p_41424_);
    }
    @SubscribeEvent
    public static void hitBlade(SlashBladeEvent.BladeStandAttackEvent event){
        if (event.getDamageSource().getEntity() instanceof Player player){
            ItemStack mainHandItem = player.getMainHandItem();
            Item item = mainHandItem.getItem();
            if (item instanceof ShineCoreItem){
                String string = mainHandItem.getOrCreateTag().getString("blade_id");
                if (string.equals(
                        event.getBlade().getCapability(ItemSlashBlade.BLADESTATE).orElse(null).getTranslationKey()
                )){
                    if (string.equals(SlashTooltipAffix.toTransKey(RBBuiltInRegistry.LiuYing.location()))){
                        event.getBlade().getCapability(ShineArt.BLADESTATE_PLUST).map(state -> {
                            state.setShineArt(new ResourceLocation("rough_blade:shine_art.liuying"));
                            return true;
                        });
                    }
                    event.getBlade().getCapability(ShineArt.BLADESTATE_PLUST).map(state -> {
                        state.setShineArt(new ResourceLocation("rough_blade:shine_dash_art"));
                        return true;
                    });
                    BladeStandEntity bladeStand = event.getBladeStand();
                    mainHandItem.shrink(1);
                    event.setCanceled(true);
                    RandomSource random = player.getRandom();
                    player.level().playSound(bladeStand, bladeStand.getPos(),
                            SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1f, 1f);
                    for(int i = 0; i < 32; ++i) {
                        if(player.level().isClientSide())
                            break;
                        double xDist = (random.nextFloat() * 2.0F - 1.0F);
                        double yDist = (random.nextFloat() * 2.0F - 1.0F);
                        double zDist = (random.nextFloat() * 2.0F - 1.0F);
                        if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
                            double x = bladeStand.getX(xDist / 4.0D);
                            double y = bladeStand.getY(0.5D + yDist / 4.0D);
                            double z = bladeStand.getZ(zDist / 4.0D);
                            ((ServerLevel)player.level()).sendParticles(ParticleTypes.END_ROD, x, y, z,0, xDist, yDist + 0.2D, zDist,1);
                        }
                    }
                }
            }
        }
    }
}

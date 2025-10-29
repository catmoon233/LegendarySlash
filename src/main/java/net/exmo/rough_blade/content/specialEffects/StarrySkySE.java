package net.exmo.rough_blade.content.specialEffects;

import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.exmo.rough_blade.content.SpecialEffectEx;
import net.exmo.rough_blade.entity.EntityDrivePlus;
import net.exmo.rough_blade.entity.StarrySkySlash;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

import static net.exmo.rough_blade.Rough_blade.MODID;
import static net.exmo.rough_blade.init.ComboStateRegistry.findTarget;


@Mod.EventBusSubscriber
public class StarrySkySE extends SpecialEffectEx {
    public StarrySkySE(int requestLevel) {
        super(requestLevel);
    }
    @SubscribeEvent
    public static void slash(SlashBladeEvent.DoSlashEvent event){
        if (SpecialEffectEx.hasSpecialEffect(event.getBlade(), RBSpecialEffectRegistry.StarrySky.getId())) {
            LivingEntity sender = event.getUser();
            Level level = sender.level();
            ISlashBladeState slashBladeState = event.getSlashBladeState();

            Optional<Entity> foundTarget = findTarget(sender, slashBladeState.getTargetEntity(sender.level()));
            Level worldIn = sender.level();
            Vec3 targetPos = (Vec3) foundTarget.map((e) -> new Vec3(e.getX(), e.getY() + (double) e.getEyeHeight() * (double) 0.5F, e.getZ())).orElseGet(() -> {
                Vec3 start = sender.getEyePosition(1.0F);
                Vec3 end = start.add(sender.getLookAngle().scale((double) 40.0F));
                HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, sender));
                return result.getLocation();
            });
            StarrySkySlash ss = new StarrySkySlash(RBEntityRegistry.STARRY_SKY_SLASH, level);
            Vec3 pos = sender.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, sender.getViewYRot(0.0F) + 90.0F));
            ss.setPos(pos.x, pos.y, pos.z);

            ss.setDamage((double) slashBladeState.getRefine() * 0.1f * (slashBladeState.getProudSoulCount() * 0.01 + sender.getAttributeValue(Attributes.ATTACK_DAMAGE)));
            Vec3 dir = targetPos.subtract(pos).normalize();
            ss.shoot(dir.x, dir.y, dir.z, 1.5F, 0.0F);
            ss.setBaseSize(0.03f);
            ss.setOwner(sender);
            ss.setRoll(event.getRoll());
            ss.setColor(1644912);
            ss.setLifetime(200);
            worldIn.addFreshEntity(ss);

            if (sender instanceof ServerPlayer serverPlayer) {
                serverPlayer.playNotifySound(SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 0.2F, 1.45F);
                serverPlayer.playNotifySound(SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 0.2F, 1.45F);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void tooltipRender(RenderTooltipEvent.Color event){
        if (event.getItemStack().getItem() instanceof ItemSlashBlade ){
            if (SpecialEffectEx.hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.StarrySky.getId())) {
                event.setBackground(1644912);
            }
        }
    }
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void tooltipRender2(RenderTooltipEvent.Pre event){
//        if (event.getItemStack().getItem() instanceof ItemSlashBlade ){
//            if (SpecialEffectEx.hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.StarrySky.getId())) {
//
//                int rex = event.getScreenWidth()  - 1;
//                int rey = event.getScreenHeight()  - 1;
//                event.getGraphics().blit(ResourceLocation.tryBuild(MODID,"textures/screen/sky_tooltip.png"), event.getX()+1, event.getY()+1,0,0, rex, rey,rex,rey);
//            }
//        }
//    }
}

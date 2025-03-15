package net.exmo.legendary_slash.content;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.JudgementCut;
import net.exmo.legendary_slash.Config;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.entity.EntityDrivePlus;
import net.exmo.legendary_slash.events.OnCharge1_5;
import net.exmo.legendary_slash.events.OnCharge2;
import net.exmo.legendary_slash.init.LSAttribute;
import net.exmo.legendary_slash.init.LSSlashArtRegistry;
import net.exmo.legendary_slash.network.DashMessage;
import net.exmo.legendary_slash.network.LSVARB;
import net.exmo.legendary_slash.network.SkillInfoMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static net.exmo.legendary_slash.init.ComboStateRegistry.doBigDriveSlash;

@Mod.EventBusSubscriber
public class SlashAttackHandle {
    private static int tick = 0;
    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        tick++;
        if (tick >= 10) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {

                LSVARB.PlayerVariables playerVariables = player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
                if (playerVariables != null) {
                    playerVariables.playerSimpleVars.get(0).setValue((int)(Math.min((int) (playerVariables.playerSimpleVars.get(0).getValue()) + 1, player.getAttributeValue(LSAttribute.Max_Slash_Power.get()))));

                    playerVariables.syncPlayerVariables(player);
                }
            }
            tick = 0;
        }


    }
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;

        if (!(event.player instanceof ServerPlayer)) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (player==null)return;
        if ((stack.getItem() instanceof ItemSlashBlade)) {
            LSVARB.PlayerVariables playerVariables1 = player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
            if (playerVariables1 != null) {

                playerVariables1.playerSimpleVars.get(3).setValue((int) (Math.max((int) (playerVariables1.playerSimpleVars.get(3).getValue()) - 1, 0)));
                if (Config.POWER.get()) {
                    int elapsedTime = player.getTicksUsingItem();
                    playerVariables1.playerSimpleVars.get(1).setValue(elapsedTime);

                    playerVariables1.syncPlayerVariables(player);
                    if (elapsedTime == 23) {
                        player.playNotifySound(SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        (((ServerLevel) player.level())).sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.2, player.getZ(), 1, 0, 0, 0, 0);
                    }
                    if (elapsedTime >= 23 && elapsedTime < 25) {
                        stack.getOrCreateTag().putBoolean("Charge3", true);
                    } else if (stack.getOrCreateTag().contains("Charge3") && stack.getOrCreateTag().getBoolean("Charge3")) {
                        stack.getOrCreateTag().putBoolean("Charge3", false);
                    }
                    if (elapsedTime >= 25) {
                        stack.getOrCreateTag().putBoolean("Charge2", true);
                    } else if (stack.getOrCreateTag().contains("Charge2") && stack.getOrCreateTag().getBoolean("Charge2")) {
                        stack.getOrCreateTag().putBoolean("Charge2", false);
                    }
                }
            }
        }
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains("boost2") && persistentData.getInt("boost2") > 0) {
            persistentData.putInt("boost2", persistentData.getInt("boost2") - 1);
        }
        if (persistentData.contains("boost3") && persistentData.getInt("boost3") > 0) {
            persistentData.putInt("boost3", persistentData.getInt("boost3") - 1);
        }
    }
    @SubscribeEvent
    public static void attackHealPower(SlashBladeEvent.HitEvent event) {
        if (event.getUser() instanceof Player player) {
            Level level = player.level();
            if (level.isClientSide) return;
            LSVARB.PlayerVariables playerVariables = player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
            if (playerVariables != null) {
                int rank = player.getCapability(CapabilityConcentrationRank.RANK_POINT).map((r) -> {
                    return r.getRank(level.getGameTime()).level;
                }).orElse(0);
                playerVariables.playerSimpleVars.get(0).setValue((int)(Math.min((int)( playerVariables.playerSimpleVars.get(0).getValue()) + rank * 0.4 *player.getAttributeValue(LSAttribute.Slash_Power_effect.get()), player.getAttributeValue(LSAttribute.Max_Slash_Power.get()))));
                playerVariables.syncPlayerVariables(player);

            }


        }
    }
@SubscribeEvent
public static void SAPlus(OnCharge1_5 onCharge1_5){
        if (onCharge1_5.slashArts == LSSlashArtRegistry.ZJ.get()){
            onCharge1_5.setCanceled(true);
            Legendary_slash.queueServerWork(2*27,()->{
                EntityDrivePlus drive1 = doBigDriveSlash(onCharge1_5.player, onCharge1_5.player.level(),120,50);
            });
        }
}
@SubscribeEvent
public static void SAPlus(OnCharge2 onCharge2){
    if (onCharge2.slashArts == LSSlashArtRegistry.ZJ.get()){
        onCharge2.setCanceled(true);
        Legendary_slash.queueServerWork(2*27,()->{
            EntityDrivePlus drive1 = doBigDriveSlash(onCharge2.player, onCharge2.player.level(),120,25);
        });
    }
}
    public static void vmove(LivingEntity livingEntity,double dy,double dashDistance){

        float yaw = livingEntity.getYRot();
        double dx = -Math.sin(Math.toRadians(yaw)) * dashDistance;
        double dz = Math.cos(Math.toRadians(yaw)) * dashDistance;
        livingEntity.setDeltaMovement(new Vec3(dx, dy, dz));
    }
    public static void sendDashMessage(Player player, double dy, double dashDistance) {
        DashMessage message = new DashMessage( dy, dashDistance);
        if (player.level().isClientSide)return;
        Legendary_slash.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
    public static void sendSkillInfoMessage(ServerPlayer player, Component s) {
        SkillInfoMessage message = new SkillInfoMessage(s);
        if (player.level().isClientSide)return;
        Legendary_slash.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }
    @SubscribeEvent
    public static void saFixed(OnCharge2 onCharge2){
        if (onCharge2.slashArts == LSSlashArtRegistry.RSA.get()){
            onCharge2.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void saFixed(OnCharge1_5 onCharge1_5){
        if (onCharge1_5.slashArts == LSSlashArtRegistry.RSA.get()){
            onCharge1_5.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void damageBoost2AndGuard(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        Entity entity2 = event.getSource().getEntity();
        if (entity.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())){
            event.setAmount(event.getAmount()*0.5f);
        }
        if (entity.hasEffect(Legendary_slash.effectAbout.GuardEffect.get())){

            if (entity2 instanceof LivingEntity entity1){
                entity1.addEffect(new MobEffectInstance( MobEffects.MOVEMENT_SLOWDOWN,30,4));
                entity1.addEffect(new MobEffectInstance( MobEffects.WEAKNESS,30,4));
                entity1.addEffect(new MobEffectInstance( MobEffects.DIG_SLOWDOWN,30,4));

                if (entity1 instanceof Player player ) {
                    sendDashMessage(player,0.5,-1);
                    player.playNotifySound(SoundEvents.ANVIL_PLACE, player.getSoundSource(), 2.0F, 1.0F);

                }else {
                    vmove(entity1,0.5,-1);
                }
                if (entity instanceof Player player ) {
                    player.getCapability(ItemSlashBlade.BLADESTATE).map((state)->{
                        state.setDamage(state.getDamage() - state.getMaxDamage()/10);
                        return true;
                    });
                    player.playNotifySound(SoundEvents.ANVIL_PLACE, player.getSoundSource(), 2.0F, 1.0F);
                    player.getCapability(CapabilityConcentrationRank.RANK_POINT).map((r)->{
                        r.addRankPoint(player,r.getUnitCapacity() );
                        return true;
                    });
                    player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).map((playerVariables) -> {
                        playerVariables.playerSimpleVars.get(0).setValue((int) (Math.min((int) (playerVariables.playerSimpleVars.get(0).getValue()) + 40, player.getAttributeValue(LSAttribute.Max_Slash_Power.get()))));
                        playerVariables.playerSimpleVars.get(3).setValue((int) 0);
                        return true;
                    });
                }

            }
            event.setCanceled(true);
            entity.addEffect(new MobEffectInstance(Legendary_slash.effectAbout.GuardEffectSuc.get(), 24,3));
        }
        if (entity2 instanceof  LivingEntity livingEntity){
            if (livingEntity.hasEffect(Legendary_slash.effectAbout.GuardEffectSuc.get())){
                int am = livingEntity.getEffect(Legendary_slash.effectAbout.GuardEffectSuc.get()).getAmplifier();
                event.setAmount(event.getAmount() * (1+(am+1)*0.5f) +entity.getMaxHealth()*0.01f);
            }
        }
        if (event.getSource().getDirectEntity() instanceof Projectile p){
            if (p.getPersistentData().contains("boost2") && p.getPersistentData().getBoolean("boost2")){
                event.setAmount(event.getAmount() *1.5f);
            }
            if (p.getPersistentData().contains("boost3") && p.getPersistentData().getBoolean("boost3")){
                event.setAmount(event.getAmount() *2f);
            }
            if (event.getSource().getEntity() instanceof Player){
                if (event.getSource().getDirectEntity() instanceof EntityJudgementCut entityJudgementCut){
                    event.setAmount(event.getAmount() *1.5f);
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void rankDamageBoost(LivingHurtEvent event){
        if (event.getSource().getEntity() instanceof Player player){
            if (player.getMainHandItem().getItem() instanceof ItemSlashBlade)return;
            Level level = player.level();
            if (level.isClientSide) return;
            int rank = player.getCapability(CapabilityConcentrationRank.RANK_POINT).map((r) -> {
                return r.getRank(level.getGameTime()).level;
            }).orElse(0);
            event.setAmount(event.getAmount()*(getDamageRankBoost(rank)));
        }
    }


    private static float getDamageRankBoost(int r){
        return switch (r) {
            case 0 -> 0.4f;
            case 1 -> 0.5f;
            case 2 -> 0.6f;
            case 3 -> 0.9f;
            case 4 -> 1.1f;
            case 5 -> 1.3f;
            case 6 -> 1.8f;
            case 7 -> 2.1f;
            default -> r * 0.4f + 0.1f;
        };
    }
}

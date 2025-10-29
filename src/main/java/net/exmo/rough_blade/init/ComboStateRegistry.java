package net.exmo.rough_blade.init;


import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;

import mods.flammpfeil.slashblade.entity.IShootable;

import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;

import mods.flammpfeil.slashblade.registry.combo.ComboState;

import mods.flammpfeil.slashblade.util.*;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.SlashAttackHandle;
import net.exmo.rough_blade.content.effects.LSGuardEffect;
import net.exmo.rough_blade.content.shineArt.arts.TheBrokenSword;
import net.exmo.rough_blade.content.slashArt.FullFireSa;
import net.exmo.rough_blade.entity.EntityDrivePlus;
import net.exmo.rough_blade.entity.SummonedSwordPlus;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.ExUtils;
import net.exmo.rough_blade.utils.PathGenerator;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;


import static mods.flammpfeil.slashblade.ability.SummonedSwordArts.ADVANCEMENT_SUMMONEDSWORDS;
import static mods.flammpfeil.slashblade.registry.combo.ComboState.REGISTRY_KEY;
import static net.exmo.rough_blade.Rough_blade.MODID;

public class ComboStateRegistry {

    public static final DeferredRegister<ComboState> COMBO_STATE ;

    public static final RegistryObject<ComboState> ZD ;
    public static final RegistryObject<ComboState> RANDOM_SA ;
    public static final RegistryObject<ComboState> SUPER_SOWRD ;
    public static final RegistryObject<ComboState> ZJ ;
    public static final RegistryObject<ComboState> FULL_FIRE ;
    public static final RegistryObject<ComboState> BREAK_SKY ;
    public static final RegistryObject<ComboState> FZZ ;
    public static final RegistryObject<ComboState> FZU;

    public static final RegistryObject<ComboState> GuardML ;

    public static final RegistryObject<ComboState> MLZhan ;
    public static final RegistryObject<ComboState> COMBO_A4_Plus ;
    public static final RegistryObject<ComboState> Zhen_Li ;
    static {

        COMBO_STATE = DeferredRegister.create(REGISTRY_KEY, MODID);
        COMBO_A4_Plus = COMBO_STATE.register("combo_a4_plus",
                ComboState.Builder.newInstance().startAndEnd(900, 1013).priority(150)
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .next(ComboState.TimeoutNext.buildFromFrame(20, entity -> SlashBlade.prefix("none")))
                        .nextOfTimeout(entity -> SlashBlade.prefix("combo_a5ex_end"))

                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(13 + 0, (entityIn) -> UserPoseOverrider.setRot(entityIn, 72, true))
                                .put(13 + 1, (entityIn) -> {
                                    UserPoseOverrider.setRot(entityIn, 72, true);
                                    if (entityIn instanceof  ServerPlayer serverPlayer) {
                                        ItemStack mainHandItem = entityIn.getMainHandItem();
                                        summonSword(mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem)), serverPlayer, mainHandItem.getEnchantmentLevel(Enchantments.POWER_ARROWS));
                                        serverPlayer.playNotifySound(SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.0F, 1.0F);
                                        AttackManager.doSlash(serverPlayer, 72 );
                                    }
                                })
                                .put(13 + 2, (entityIn) ->{
                                    UserPoseOverrider.setRot(entityIn, 72, true);
                                    AttackManager.doSlash(entityIn, 72 );
                                })
                                .put(13 + 3, (entityIn) -> {
                                    UserPoseOverrider.setRot(entityIn, 72, true);
                                    AttackManager.doSlash(entityIn, 72 );
                                })
                                .put(13 + 4, (entityIn) -> {
                                    UserPoseOverrider.setRot(entityIn, 72, true);
                                    AttackManager.doSlash(entityIn, 72 );
                                })
                                .put(13 + 5, (entityIn) -> UserPoseOverrider.resetRot(entityIn)).build())
                        .clickAction(a -> AdvancementHelper.grantCriterion(a, AdvancementHelper.ADVANCEMENT_COMBO_A_EX))
                        .addHitEffect(StunManager::setStun)::build);
        RANDOM_SA = COMBO_STATE.register("random_sa",
                ComboState.Builder.newInstance()
                        .startAndEnd(459, 488)
                        .priority(100)
                        .next((entity) -> SlashBlade.prefix("none"))
                        .nextOfTimeout((entity) -> SlashBlade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(1, (entityIn) ->{
                                    if (entityIn instanceof Player player){
                                      UserPoseOverrider.setRot(entityIn, -12, true);
                                        var  entries = new ArrayList<>(SlashArtsRegistry.SLASH_ARTS.getEntries().stream().toList());

                                        var random = entries.get((int) (Math.random() * entries.size()));
                                        ItemStack mainHandItem = player.getMainHandItem();

                                        var iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                                        iSlashBladeState.doChargeAction(player, 1);
                                        long lastActionTime = iSlashBladeState.getLastActionTime();
                                        Rough_blade.queueServerWork(3, () -> {
                                            AttackManager.playQuickSheathSoundAction(player);
                                            if (mainHandItem.getItem() instanceof ItemSlashBlade itemSlashBlade) {
                                                Level level = player.level();
                                                itemSlashBlade.onUseTick(level, player, mainHandItem, (int) (10));

                                                iSlashBladeState.setLastActionTime((long) (lastActionTime + ( - 10)));
                                            }

                                            iSlashBladeState.updateComboSeq(entityIn, new ResourceLocation(SlashBlade.MODID, "none"));
                                            iSlashBladeState.setSlashArtsKey(random.getId());
                                            iSlashBladeState.doChargeAction(player, 10);
                                            iSlashBladeState.setSlashArtsKey(RBSlashArtRegistry.RSA.getId());
                                        });

                                        }


                                })
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        SUPER_SOWRD = COMBO_STATE.register("super_sword",
                ComboState.Builder.newInstance()
                        .startAndEnd(459, 488)
                        .priority(100)
                        .next((entity) -> SlashBlade.prefix("none"))
                        .nextOfTimeout((entity) -> SlashBlade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(1, (entityIn) ->{
                                    if (entityIn instanceof Player player) {
                                        {
                                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                            List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                            playerSimpleVars.get(4).setValue("slash_art.super_sword");
                                            playerVariables.syncPlayerVariables(player);
                                        }
                                        Rough_blade.queueServerWork(45, () -> {
                                                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                    if (player instanceof ServerPlayer) {
                                                        List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                        if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.super_sword")) {
                                                            playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                                            playerVariables.syncPlayerVariables(player);
                                                        }
                                                    }

                                                }
                                        );
                                        Level level = player.level();
                                        player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                                            Vec3 basePos;
                                            Entity target = state.getTargetEntity(level);
                                            if (target != null) {
                                                basePos = target.position();
                                            } else {
                                                Vec3 forwardDir = calculateViewVector(0, player.getYRot());
                                                basePos = player.getPosition(0).add(forwardDir.scale(5));
                                            }
                                                    SummonedSwordPlus ss = new SummonedSwordPlus(
                                                            RBEntityRegistry.SUMMONEDSWORDPLUS, level);


                                                    ss.noPhysics = true;
                                                    ss.setOwner(player);
                                                    ss.setColor(state.getColorCode());
                                                    ss.setRoll(0);
                                                    ss.setDamage(50);
                                                    ss.setBaseSize(0.09f);
                                                    // force riding
                                                    ss.startRiding(player, true);

                                                    ss.setDelay(25);
                                                    ss.setPierce((byte) 100);

                                                    ss.setPos(basePos.add(0, 50, 0));

                                                    ss.setXRot(-90);

                                                    player.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F,
                                                            1.45F);
                                                    level.addFreshEntity(ss);
                                                }
                                        );
                                    }

                                })
                                .put(0 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, -12, true))
                                .put(0 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, -12, true))
                                .put(0 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, -12, true))
                                .put(0 + 5, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true))
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        Zhen_Li = COMBO_STATE.register("zhen_li",
                ComboState.Builder.newInstance()
                        .startAndEnd(2200, 2277)
                        .priority(100)
                        .speed(1.0F)

                        .next((entity) -> Rough_blade.prefix("zhen_li"))
                        .nextOfTimeout((entity) -> SlashBlade.prefix("void_slash_sheath"))
                        .addTickAction(entity -> entity.setDeltaMovement(Vec3.ZERO))

                        .motionLoc(DefaultResources.ExMotionLocation)
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(16 + 0, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 1, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 5, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 6, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 7, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 8, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 9, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 10, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 11, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 12, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true))
                                .put(16 + 13, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true))
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        FULL_FIRE = COMBO_STATE.register("full_fire",
                ComboState.Builder.newInstance()
                        .startAndEnd(2200, 2288)
                        .priority(50)
                        .speed(1.0F)
                        .next((entity) -> SlashBlade.prefix("none"))
                        .nextOfTimeout((entity) -> SlashBlade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(1, (entityIn) -> {
                                    if (entityIn instanceof Player player) {
                                        {
                                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                            List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                            playerSimpleVars.get(4).setValue("slash_art.full_fire");
                                            playerVariables.syncPlayerVariables(player);
                                        }
                                        Rough_blade.queueServerWork(45, () -> {
                                                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                    if (player instanceof ServerPlayer) {
                                                        List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                        if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.full_fire")) {
                                                            playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                                            playerVariables.syncPlayerVariables(player);
                                                        }
                                                    }

                                                }
                                        );
                                        FullFireSa.doSlash(player, 12f, 1.6f);
                                    }


                                })
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        BREAK_SKY = COMBO_STATE.register("break_sky",
                ComboState.Builder.newInstance()
                        .startAndEnd(2200, 2288)
                        .priority(50)
                        .speed(1.0F)
                        .next((entity) -> SlashBlade.prefix("none"))
                        .nextOfTimeout((entity) -> SlashBlade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(1, (entityIn) -> {
                                    if (entityIn instanceof Player player) {
                                        {
                                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                            List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                            playerSimpleVars.get(4).setValue("slash_art.break_sky");
                                            playerVariables.syncPlayerVariables(player);
                                        }
                                        Rough_blade.queueServerWork(45, () -> {
                                                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                    if (player instanceof ServerPlayer) {
                                                        List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                        if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.break_sky")) {
                                                            playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                                            playerVariables.syncPlayerVariables(player);
                                                        }
                                                    }

                                                });
                                        TheBrokenSword.doSlash(player, 12f, 1.6f);
                                    }


                                })
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        ZD = COMBO_STATE.register("zd",
                ComboState.Builder.newInstance()
                        .startAndEnd(1, 10)
                        .priority(100)
                        .next(ComboState.TimeoutNext.buildFromFrame(5, (entity) -> SlashBlade.prefix("none")))
                        .nextOfTimeout((entity) -> Rough_blade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .clickAction((entity) -> {})
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(0, (entityIn) ->{
                                    if (entityIn instanceof Player player){
                                        sdFunction(entityIn, player);
                                        //     }
                                    }


                                }).build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        FZZ = COMBO_STATE.register("fzz",
                ComboState.Builder.newInstance()
                        .startAndEnd(1, 10)
                        .priority(100)
                        .next(ComboState.TimeoutNext.buildFromFrame(5, (entity) -> SlashBlade.prefix("none")))
                        .nextOfTimeout((entity) -> Rough_blade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .clickAction((entity) -> {})
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(0, (entityIn) ->{
                                    if (entityIn instanceof Player player){
                                            {
                                                LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                playerSimpleVars.get(4).setValue("slash_art.fzz");
                                                playerVariables.syncPlayerVariables(player);
                                            }
                                        Rough_blade.queueServerWork(45, () -> {
                                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                            if (player instanceof ServerPlayer) {
                                                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.fzz")) {
                                                    playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                                    playerVariables.syncPlayerVariables(player);
                                                }
                                            }

                                        });
                                            TheBrokenSword.doSlash2(player, 12f, 1.6f);

                                    }


                                }).build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        FZU = COMBO_STATE.register("fzu",
                ComboState.Builder.newInstance()
                        .startAndEnd(1, 10)
                        .priority(50)
                        .next(ComboState.TimeoutNext.buildFromFrame(5, (entity) -> SlashBlade.prefix("none")))
                        .nextOfTimeout((entity) -> Rough_blade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .clickAction((entity) -> {})
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(0, (entityIn) ->{
                                    if (entityIn instanceof Player player){
                                            {
                                                LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                playerSimpleVars.get(4).setValue("slash_art.fzu");
                                                playerVariables.syncPlayerVariables(player);
                                            }
                                            Rough_blade.queueServerWork(
                                                    50,()->{
                                                                {
                                                                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                                    List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                                    playerSimpleVars.get(4).setValue("slash_art.fzu1");
                                                                    playerVariables.syncPlayerVariables(player);
                                                                }
                                                    }
                                            );
                                        Rough_blade.queueServerWork(
                                                    100,()->{
                                                                {
                                                                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                                                    List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                                                    playerSimpleVars.get(4).setValue("slash_art.fzu2");
                                                                    playerVariables.syncPlayerVariables(player);
                                                                }
                                                    }
                                            );

                                            TheBrokenSword.doSlash3(player, 12f, 1.6f);

                                    }


                                })



                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        GuardML = COMBO_STATE.register("guard_ml",
                ComboState.Builder.newInstance()
                        .startAndEnd(1, 10)
                        .priority(50)
                        .next(ComboState.TimeoutNext.buildFromFrame(5, (entity) -> SlashBlade.prefix("none")))
                        .nextOfTimeout((entity) -> Rough_blade.prefix("none"))
                        .motionLoc(DefaultResources.ExMotionLocation)
                        .clickAction((entity) -> {})
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(0, (entityIn) ->{
                                    if (entityIn instanceof Player player){
                                        UserPoseOverrider.setRot(entityIn, -50, true);
                                        sdFunction(player, player);

                                    }


                                })



                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
        ZJ = COMBO_STATE.register("zj",
                ComboState.Builder
                        .newInstance().startAndEnd(2200, 2288).priority(50).speed(1.0F)
                        .next(entity -> SlashBlade.prefix("void_slash"))
                        .nextOfTimeout(entity -> SlashBlade.prefix("void_slash_sheath"))
                        .clickAction((entity) -> {})
                        .addTickAction(entity -> entity.setDeltaMovement(Vec3.ZERO))
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(16 + 0, (entityIn) -> {

                                    UserPoseOverrider.setRot(entityIn, -12, true);
                                    if (entityIn instanceof Player player){
                                        LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                        List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                        playerSimpleVars.get(4).setValue("slash_art.zj");
                                        playerVariables.syncPlayerVariables(player);
                                    }
                                    Rough_blade.queueServerWork(40,()->{
                                    if (entityIn instanceof Player player){
                                        LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                        if (player instanceof ServerPlayer) {
                                            List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                            if (((String) playerSimpleVars.get(4).getValue()).equals("slash_art.zj")) {
                                                playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                                playerVariables.syncPlayerVariables(player);
                                            }
                                        }
                                        Vec3 forward = player.getForward();
                                        int radius = 8;
                                        Vec3 hitLocation = player.position().add(0, player.getBbHeight() * .3f, 0).add(forward.scale(5));
                                        Level level = player.level();
                                        var entities = level.getEntities(player, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));
                                        for (Entity entity : entities) {
                                            if (entity instanceof LivingEntity livingEntity) {
                                                if (livingEntity!= entityIn){
                                                    livingEntity.knockback(1, 1,1);
                                                    StunManager.setStun(livingEntity, 10);
                                                    if (level instanceof ServerLevel level1) {
                                                        for (int i = 0; i < 10; i++) {
                                                            int x = (int) (Math.random() * 2) - 1;
                                                            int y = (int) (Math.random() * 2) - 1;
                                                            int z = (int) (Math.random() * 2) - 1;
                                                            level1.sendParticles(ParticleTypes.CLOUD, livingEntity.getX() + x, livingEntity.getY()+1 + y, livingEntity.getZ() + z, 5, 0.1, 0.1, 0.1, 0.1);

                                                        }
                                                    }
                                                    var damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),player);
                                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
                                                    livingEntity.hurt(damageSource, (float) (entityIn.getAttributeValue(Attributes.ATTACK_DAMAGE)*25f+5f+livingEntity.getHealth()*0.15f));

                                                }
                                            }

                                        }

                                        EntityDrivePlus drive = doBigDriveSlash(entityIn, level,60,32);


                                        if (entityIn != null)
                                            entityIn.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT)
                                                    .ifPresent(rank -> drive.setRank(rank.getRankLevel(entityIn.level().getGameTime())));
                                        if (entityIn instanceof ServerPlayer player1){
                                            CommandSourceStack commandSourceStack;
                                            if (player1.level() instanceof  ServerLevel serverLevel){
                                                commandSourceStack =  new CommandSourceStack(
                                                        CommandSource.NULL,
                                                        player.position(),
                                                        player.getRotationVector(),
                                                        serverLevel,
                                                        4,
                                                        player.getName().getString(),
                                                        player.getDisplayName(),
                                                        serverLevel.getServer(),
                                                        player
                                                );
                                            }else commandSourceStack = null;
                                            player.getServer().getCommands().performPrefixedCommand(commandSourceStack, "title @s title \"\\u00a74斬\"");
                                            Vec3 delta = player1.getDeltaMovement();
                                            Vec3 motion = new Vec3(delta.x, +0.8, delta.z);
                                            player1.playNotifySound(SoundEvents.ENDER_DRAGON_SHOOT,SoundSource.PLAYERS,1f,1f);
                                            player.playNotifySound(SoundEvents.LAVA_EXTINGUISH,SoundSource.PLAYERS,1f,1f);
                                            player.playNotifySound(SoundEvents.BUCKET_FILL_LAVA,SoundSource.PLAYERS,1f,1f);
                                            player1.move(MoverType.SELF, motion);

                                            player1.connection.send(new ClientboundSetEntityMotionPacket(player1.getId(), motion.scale(0.75f)));
                                        }
                                        //     }
                                    }
                                    });

                                })
                                .put(16 + 1, (entityIn) ->
                                        rollAndSound(entityIn, -12, true))
                                .put(16 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, -12, true
                                ))
                                .put(16 + 3, (entityIn) -> rollAndSound(entityIn, -12, true
                                ))
                                .put(16 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true
                                ))
                                .put(57 + 0, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 1, (entityIn) -> rollAndSound(entityIn, 18, true
                                ))
                                .put(57 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 4, (entityIn) -> rollAndSound(entityIn, 18, true
                                ))
                                .put(57 + 5, (entityIn) -> {
                                    UserPoseOverrider.setRot(entityIn, 0, true);

                                })
                                .put(57+6, (entityIn) ->{


                                })
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);

        MLZhan = COMBO_STATE.register("ml_zhan",
                ComboState.Builder
                        .newInstance().startAndEnd(2200, 2288).priority(50).speed(1.0F)
                        .next(entity -> Rough_blade.prefix("ml_zhan"))
                        .nextOfTimeout(entity -> SlashBlade.prefix("void_slash_sheath"))
                        .clickAction((entity) -> {})
                        .addTickAction(entity -> entity.setDeltaMovement(Vec3.ZERO))
                        .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                                .put(3, (entityIn) -> {

                                    UserPoseOverrider.setRot(entityIn, -12, true);
                                    entityIn.getMainHandItem().setDamageValue(0);
                                    Rough_blade.queueServerWork(40,()->{
                                    if (entityIn instanceof Player player){

                                        Vec3 forward = player.getForward();
                                        int radius = 8;
                                        Vec3 hitLocation = player.position().add(0, player.getBbHeight() * .3f, 0).add(forward.scale(5));
                                        Level level = player.level();
                                        var entities = level.getEntities(player, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));
                                        EntityDrivePlus drive = doBigDriveSlash2(entityIn, level,60,12f);



                                        if (entityIn != null)
                                            entityIn.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT)
                                                    .ifPresent(rank -> drive.setRank(rank.getRankLevel(entityIn.level().getGameTime())));
                                        double attributeValue = entityIn.getAttributeValue(Attributes.ATTACK_DAMAGE)+7;

                                        for (Entity entity : entities) {
                                            if (entity instanceof LivingEntity livingEntity) {
                                                if (livingEntity!= entityIn){
                                                    livingEntity.knockback(1, 1,1);
                                                    StunManager.setStun(livingEntity, 10);
                                                    if (level instanceof ServerLevel level1) {
                                                        for (int i = 0; i < 10; i++) {
                                                            int x = (int) (Math.random() * 2) - 1;
                                                            int y = (int) (Math.random() * 2) - 1;
                                                            int z = (int) (Math.random() * 2) - 1;
                                                            level1.sendParticles(ParticleTypes.CLOUD, livingEntity.getX() + x, livingEntity.getY()+1 + y, livingEntity.getZ() + z, 5, 0.1, 0.1, 0.1, 0.1);

                                                        }
                                                    }
                                                    var damageSource = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),player);
                                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 10));
                                                    livingEntity.hurt(damageSource, (float) (attributeValue *20+livingEntity.getHealth()*0.01f));

                                                    player.heal(3);
                                                    Vec3 pv = player.blockPosition().getCenter();
                                                    Vec3  kv = livingEntity.blockPosition().getCenter();
                                                    player.teleportTo(kv.x(), kv.y(), kv.z());
                                                    livingEntity.teleportTo(pv.x(), pv.y(), pv.z());
                                                    List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), Vec3.atCenterOf(livingEntity.blockPosition()));
                                                    for (Vec3 v : vl){

                                                        if (player.level() instanceof ServerLevel serverLevel){
                                                            serverLevel.sendParticles(ParticleTypes.SOUL, v.x(), v.y(), v.z(), 8, 0.3, 0.3, 0.3, 0.1);
                                                            {
                                                                final Vec3 _center = v;
                                                                List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                                                for (Entity entityiterator : _entfound) {
                                                                    if (entityiterator != player) {
                                                                        if (entityiterator!=livingEntity) {
                                                                            if (entityiterator instanceof LivingEntity) {
                                                                                player.heal(1);
                                                                                entityiterator.invulnerableTime = 0;
                                                                                entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FELL_OUT_OF_WORLD), player), (float) (attributeValue*9f));
                                                                                entityiterator.setDeltaMovement(new Vec3(0, 0.3, 0));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                        if (entityIn instanceof ServerPlayer player1){
                                            Vec3 delta = player1.getDeltaMovement();
                                            Vec3 motion = new Vec3(delta.x, +0.8, delta.z);
                                            player1.playNotifySound(SoundEvents.ENDER_DRAGON_SHOOT,SoundSource.PLAYERS,1f,1f);
                                            player.playNotifySound(SoundEvents.LAVA_EXTINGUISH,SoundSource.PLAYERS,1f,1f);
                                            player.playNotifySound(SoundEvents.BUCKET_FILL_LAVA,SoundSource.PLAYERS,1f,1f);
                                            player1.move(MoverType.SELF, motion);

                                            player1.connection.send(new ClientboundSetEntityMotionPacket(player1.getId(), motion.scale(0.75f)));
                                        }

                                        //     }
                                    }
                                    });

                                })
                                .put(16 + 1, (entityIn) ->
                                        rollAndSound(entityIn, -12, true))
                                .put(16 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, -12, true
                                ))
                                .put(16 + 3, (entityIn) -> rollAndSound(entityIn, -12, true
                                ))
                                .put(16 + 4, (entityIn) -> UserPoseOverrider.setRot(entityIn, 0, true
                                ))
                                .put(57 + 0, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 1, (entityIn) -> rollAndSound(entityIn, 18, true
                                ))
                                .put(57 + 2, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 3, (entityIn) -> UserPoseOverrider.setRot(entityIn, 18, true
                                ))
                                .put(57 + 4, (entityIn) -> rollAndSound(entityIn, 18, true
                                ))
                                .put(57 + 5, (entityIn) -> {
                                    UserPoseOverrider.setRot(entityIn, 0, true);

                                })
                                .put(57+6, (entityIn) ->{


                                })
                                .build())
                        .addTickAction(FallHandler::fallDecrease)
                        .addHitEffect(StunManager::setStun)
                        ::build);
    }
    public static Vec3 calculateViewVector(float x, float y) {
        float f = x * ((float) Math.PI / 180F);
        float f1 = -y * ((float) Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
    }
    public static void rollAndSound(LivingEntity entityIn, int r, boolean b){
        UserPoseOverrider.setRot(entityIn, r, b);
        if (entityIn instanceof ServerPlayer player){
            player.playNotifySound(SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS,1f,1f);
            player.playNotifySound(SoundEvents.BUCKET_EMPTY_LAVA,SoundSource.PLAYERS,1f,1f);
        }
    }
    public static Optional<Entity> findTarget(LivingEntity sender, Entity lockedT) {
        Optional<Entity> foundTarget = Stream.of(Optional.ofNullable(lockedT),
                        RayTraceHelper
                                .rayTrace(sender.level(), sender, sender.getEyePosition(1.0f), sender.getLookAngle(),
                                        12, 12, (e) -> true)
                                .filter(r -> r.getType() == HitResult.Type.ENTITY).filter(r -> {
                                    EntityHitResult er = (EntityHitResult) r;
                                    Entity target = er.getEntity();

                                    boolean isMatch = true;
                                    if (target instanceof LivingEntity)
                                        isMatch = TargetSelector.lockon.test(sender, (LivingEntity) target);

                                    if (target instanceof IShootable)
                                        isMatch = ((IShootable) target).getShooter() != sender;

                                    return isMatch;
                                }).map(r -> ((EntityHitResult) r).getEntity()))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
        return foundTarget;
    }
    public static void summonSword(ISlashBladeState state, ServerPlayer sender, int powerLevel) {

        AdvancementHelper.grantCriterion(sender, ADVANCEMENT_SUMMONEDSWORDS);
        Optional<Entity> foundTarget = findTarget(sender, state.getTargetEntity(sender.level()));
        Level worldIn = sender.level();
        Vec3 targetPos = (Vec3) foundTarget.map((e) -> new Vec3(e.getX(), e.getY() + (double) e.getEyeHeight() * (double) 0.5F, e.getZ())).orElseGet(() -> {
            Vec3 start = sender.getEyePosition(1.0F);
            Vec3 end = start.add(sender.getLookAngle().scale((double) 40.0F));
            HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, sender));
            return result.getLocation();
        });
        for (int i = 0; i < 1; ++i) {
            //int counter = StatHelper.increase(sender, SlashBlade.RegistryEvents.SWORD_SUMMONED, 1);
            boolean sided = i % 2 == 0;
            EntityDrivePlus ss = new EntityDrivePlus(RBEntityRegistry.DRIVEPLUS, worldIn);
            Vec3 pos = sender.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, sender.getViewYRot(0.0F) + 90.0F));
            ss.setPos(pos.x, pos.y, pos.z);
            ss.setParticle(false);
            ss.setDamage((double) state.getRefine() * 0.1f + powerLevel+8);
            Vec3 dir = targetPos.subtract(pos).normalize();
            ss.shoot(dir.x, dir.y, dir.z, 3.0F, 0.0F);
            ss.setBaseSize(0.03f);
            ss.setOwner(sender);
            ss.setColor(state.getColorCode());
            ss.setRotationRoll(sided ? 72 : 72+180);
            ss.setLifetime(100);
            worldIn.addFreshEntity(ss);

            sender.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F, 1.45F);
        }
    }

    public static void summonSword(ISlashBladeState state, Player sender, int powerLevel,boolean doubleC) {

        AdvancementHelper.grantCriterion(sender, ADVANCEMENT_SUMMONEDSWORDS);
//        Optional<Entity> foundTarget = findTarget(sender, state.getTargetEntity(sender.level()));
        Level worldIn = sender.level();
//        Vec3 targetPos = (Vec3) foundTarget.map((e) -> new Vec3(e.getX(), e.getY() + (double) e.getEyeHeight() * (double) 0.5F, e.getZ())).orElseGet(() -> {
//
//            Vec3 start = sender.getEyePosition(1.0F);
//            Vec3 end = start.add(sender.getLookAngle().scale((double) 40.0F));
//            HitResult result = worldIn.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, sender));
//            return result.getLocation();
//        });
        {
            final Vec3 _center = new Vec3(sender.getX(), sender.getY(), sender.getZ());
            List<Entity> _entfound = worldIn.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (Entity entityiterator : _entfound) {
                if (entityiterator instanceof LivingEntity le) {

                    if (entityiterator != sender) {
                        le.addEffect(new MobEffectInstance(MobEffects.GLOWING, 3, 0));
                        le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3));
                            Vec3 basePos = le.position();

                            SummonedSwordPlus ss = new SummonedSwordPlus(
                                    RBEntityRegistry.SUMMONEDSWORDPLUS, worldIn);
                            ss.getPersistentData().putFloat("redius",4f);
                            ss.getPersistentData().putBoolean("mingli",true);
                            ss.noPhysics = true;
                            ss.setOwner(sender);
                            ss.setColor(state.getColorCode());
                            ss.setRoll(0);
                            ss.setParticle(false);
                            ss.setDamage(0.5f);
                            ss.setBaseSize(0.01f);
                            // force riding
                            ss.startRiding(sender, true);

                            ss.setDelay(0);
                            ss.setPierce((byte) 100);
                        RandomSource random = worldIn.getRandom();

                        double xOffset = random.nextDouble() * 0.1 ;
                        double yOffset = random.nextFloat() *0.1;
                        double zOffset = random.nextFloat() * 0.1 ;
                        Vec3 add = basePos.add(xOffset, 5 + yOffset, zOffset);
                        if (worldIn instanceof ServerLevel serverLevel)
                        ExUtils.sendParticleCircle(serverLevel, add, ParticleTypes.END_ROD, 1.5f, 8);
                        ss.setPos(add);

                            ss.setXRot(-90);

                            sender.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F,
                                    1.45F);
                            worldIn.addFreshEntity(ss);

                    }
                }
            }
        }
//        for (int i = 0; i < (doubleC ? 2 : 1); ++i) {
//            //int counter = StatHelper.increase(sender, SlashBlade.RegistryEvents.SWORD_SUMMONED, 1);
//            boolean sided = i % 2 == 0;
//            EntityDrive ss = new EntityDrive(SlashBlade.RegistryEvents.Drive, worldIn);
//            Vec3 pos = sender.getEyePosition(1);
//            ss.setPos(pos.x, pos.y, pos.z);
//            double damageIn = (double) state.getRefine() * 0.1f + powerLevel;
//            if (doubleC) damageIn /=4;
//            ss.setDamage(damageIn);
//            Vec3 dir = targetPos.subtract(pos).normalize();
//            ss.shoot(dir.x, dir.y, dir.z, 3.0F, 0.0F);
//
//            ss.setOwner(sender);
//            ss.setColor(state.getColorCode());
//            ss.setRotationRoll(sided ? 60 : 120);
//            ss.setLifetime(100);
//            worldIn.addFreshEntity(ss);
//
//            sender.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 0.2F, 1.45F);
//        }
    }
    public static @NotNull EntityDrivePlus doBigDriveSlash(LivingEntity entityIn, Level level,int roll,float damage) {
        EntityDrivePlus drive = new EntityDrivePlus(RBEntityRegistry.DRIVEPLUS, level);
        drive.setBaseSize(0.35f);

        float speed = Mth.randomBetween(level.getRandom(), 0.7f, 1.4f);
        drive.setPierce((byte) 1000);
        drive.setPos(entityIn.position());
        drive.setDamage(damage);
        drive.setSpeed(speed);
        drive.shoot(entityIn.getLookAngle().x, entityIn.getLookAngle().y, entityIn.getLookAngle().z,
                drive.getSpeed(), 0);
        int colorCode = entityIn.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                .map(state -> state.getColorCode()).orElse(0xFF3333FF);

        drive.setOwner(entityIn);
        drive.setRotationRoll(roll);
        drive.setColor(colorCode);
        drive.setIsCritical(false);
        drive.setLifetime(100);
        level.addFreshEntity(drive);

        return drive;
    }
    public static @NotNull EntityDrivePlus doBigDriveSlash2(LivingEntity entityIn, Level level,int roll,float damage) {
        EntityDrivePlus drive = new EntityDrivePlus(RBEntityRegistry.DRIVEPLUS, level);
        drive.setBaseSize(0.095f);

        float speed = Mth.randomBetween(level.getRandom(), 0.7f, 1.4f);
        drive.setPierce((byte) 1000);
        drive.setPos(entityIn.position());
        drive.setDamage(damage);
        drive.setSpeed(speed);
        drive.shoot(entityIn.getLookAngle().x, entityIn.getLookAngle().y, entityIn.getLookAngle().z,
                drive.getSpeed(), 0);
        int colorCode = entityIn.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                .map(state -> state.getColorCode()).orElse(0xFF3333FF);

        drive.setOwner(entityIn);
        drive.setRotationRoll(roll);
        drive.setColor(colorCode);
        drive.setIsCritical(false);
        drive.setLifetime(100);
        level.addFreshEntity(drive);

        return drive;
    }
    public static @NotNull EntityDrivePlus doBigDriveSlash3(LivingEntity entityIn, Level level,float roll,float damage) {
        EntityDrivePlus drive = new EntityDrivePlus(RBEntityRegistry.DRIVEPLUS, level);
        drive.setBaseSize(0.035f);


        float speed = Mth.randomBetween(level.getRandom(), 0.7f, 1.4f);
        drive.setPierce((byte) 1000);
        drive.setPos(entityIn.position().add(0,1.5,0));
        drive.setDamage(damage);
        drive.setSpeed(speed);
        drive.shoot(entityIn.getLookAngle().x, 0, entityIn.getLookAngle().z,
                drive.getSpeed(), 0);
        int colorCode = entityIn.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                .map(state -> state.getColorCode()).orElse(0xFF3333FF);

        drive.setOwner(entityIn);
        drive.setParticle( false);
        drive.setRotationRoll(roll);
        drive.setColor(colorCode);
        drive.setIsCritical(false);
        drive.setLifetime(100);
        level.addFreshEntity(drive);
        return drive;
    }
    public static void sdFunction(LivingEntity entityIn, Player player) {
        LSVARB.PlayerVariables playerVariables = entityIn.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
        Optional<Boolean> map = player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).map(e -> e.isBroken());
        if (map.isPresent() && map.get()){
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.translatable("message.rough_blade.sword_broken"),true);
            }
            return;
        }
        if (((int) playerVariables.playerSimpleVars.get(3).getValue()) == 0 ) {
            ItemStack mainHandItem = entityIn.getMainHandItem();
            if (mainHandItem.getItem() instanceof ItemSlashBlade) {
                playerVariables.playerSimpleVars.get(3).setValue(26);
                playerVariables.syncPlayerVariables(player);
                player.setDeltaMovement(new Vec3(0, 0, 0));
                entityIn.addEffect(new MobEffectInstance(Rough_blade.effectAbout.GuardEffect.get(), 10, 0, false, false, false));
                entityIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 4, false, false, false));
                player.playNotifySound(SoundEvents.IRON_DOOR_OPEN, player.getSoundSource(), 2.0F, 1.0F);
               // AttackManager.doSlash(player,0);
                if (!player.onGround()) {
                    if (player instanceof ServerPlayer) SlashAttackHandle.sendSkillInfoMessage((ServerPlayer) player, Component.translatable("skill.rough_blade.guard_space"));

                    player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                        Entity targetEntity = state.getTargetEntity(player.level());
                        if (targetEntity !=null){
                            LSGuardEffect.doTeleport(player, targetEntity);
                        }
                    });
                }else {
                    if (player instanceof ServerPlayer) SlashAttackHandle.sendSkillInfoMessage((ServerPlayer) player, Component.translatable("skill.rough_blade.guard"));

                }
                mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(null).updateComboSeq(entityIn, new ResourceLocation(SlashBlade.MODID, "none"));
            }
        }
    }

}

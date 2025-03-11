package net.exmo.legendary_slash.init;

import com.dinzeer.legendblade.Legendblade;
import com.dinzeer.legendblade.regsitry.slashblade.LBslashArtRegsitry;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.entity.EntityHeavyRainSwords;
import mods.flammpfeil.slashblade.event.FallHandler;
import mods.flammpfeil.slashblade.event.client.UserPoseOverrider;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboCommands;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.slasharts.Drive;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.EnumSetConverter;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.exmo.legendary_slash.Legendary_slash;
import net.exmo.legendary_slash.content.SlashAttackHandle;
import net.exmo.legendary_slash.content.effects.LSGuardEffect;
import net.exmo.legendary_slash.entity.EntityDrivePlus;
import net.exmo.legendary_slash.entity.SummonedSwordPlus;
import net.exmo.legendary_slash.network.LSVARB;
import net.exmo.legendary_slash.utils.ExUtils;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.TitleCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import static mods.flammpfeil.slashblade.SlashBlade.RegistryEvents.DriveLoc;
import static mods.flammpfeil.slashblade.registry.combo.ComboState.REGISTRY_KEY;
import static net.exmo.legendary_slash.Legendary_slash.MODID;

public class ComboStateRegistry {

    public static final DeferredRegister<ComboState> COMBO_STATE ;

    public static final RegistryObject<ComboState> ZD ;
    public static final RegistryObject<ComboState> RANDOM_SA ;
    public static final RegistryObject<ComboState> SUPER_SOWRD ;
    public static final RegistryObject<ComboState> ZJ ;
    static {
        COMBO_STATE = DeferredRegister.create(REGISTRY_KEY, MODID);

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
                                        if (ModList.get().isLoaded(Legendblade.MODID)){
                                            entries.addAll(LBslashArtRegsitry.SLASH_ARTS.getEntries());
                                        }
                                        var random = entries.get((int) (Math.random() * entries.size()));
                                        ItemStack mainHandItem = player.getMainHandItem();

                                        var iSlashBladeState = mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(mainHandItem));
                                        iSlashBladeState.doChargeAction(player, 1);
                                        long lastActionTime = iSlashBladeState.getLastActionTime();
                                        Legendary_slash.queueServerWork(3, () -> {
                                            AttackManager.playQuickSheathSoundAction(player);
                                            if (mainHandItem.getItem() instanceof ItemSlashBlade itemSlashBlade) {
                                                Level level = player.level();
                                                itemSlashBlade.onUseTick(level, player, mainHandItem, (int) (10));

                                                iSlashBladeState.setLastActionTime((long) (lastActionTime + ( - 10)));
                                            }

                                            iSlashBladeState.updateComboSeq(entityIn, new ResourceLocation(SlashBlade.MODID, "none"));
                                            iSlashBladeState.setSlashArtsKey(random.getId());
                                            iSlashBladeState.doChargeAction(player, 10);
                                            iSlashBladeState.setSlashArtsKey(LSSlashArtRegistry.RSA.getId());
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
                                        Legendary_slash.queueServerWork(45, () -> {
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
                                                            LSEntityRegistry.SUMMONEDSWORDPLUS, level);


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
        ZD = COMBO_STATE.register("zd",
                ComboState.Builder.newInstance()
                        .startAndEnd(1, 10)
                        .priority(100)
                        .next(ComboState.TimeoutNext.buildFromFrame(5, (entity) -> SlashBlade.prefix("none")))
                        .nextOfTimeout((entity) -> Legendary_slash.prefix("none"))
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
                                    Legendary_slash.queueServerWork(40,()->{
                                    if (entityIn instanceof Player player){
                                        LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                                        if (player instanceof ServerPlayer) {
                                            List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                            if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.zj")) {
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
    public static @NotNull EntityDrivePlus doBigDriveSlash(LivingEntity entityIn, Level level,int roll,float damage) {
        EntityDrivePlus drive = new EntityDrivePlus(LSEntityRegistry.DRIVEPLUS, level);
        drive.setBaseSize(0.35f);

        level.addFreshEntity(drive);
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
        return drive;
    }

    public static void sdFunction(LivingEntity entityIn, Player player) {
        LSVARB.PlayerVariables playerVariables = entityIn.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
        if (((int) playerVariables.playerSimpleVars.get(3).getValue()) == 0) {
            ItemStack mainHandItem = entityIn.getMainHandItem();
            if (mainHandItem.getItem() instanceof ItemSlashBlade) {
                playerVariables.playerSimpleVars.get(3).setValue(26);
                playerVariables.syncPlayerVariables(player);
                player.setDeltaMovement(new Vec3(0, 0, 0));
                entityIn.addEffect(new MobEffectInstance(Legendary_slash.effectAbout.GuardEffect.get(), 10, 0, false, false, false));
                entityIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 4, false, false, false));
                player.playNotifySound(SoundEvents.IRON_DOOR_OPEN, player.getSoundSource(), 2.0F, 1.0F);
               // AttackManager.doSlash(player,0);
                if (!player.onGround()) {
                    if (player instanceof ServerPlayer) SlashAttackHandle.sendSkillInfoMessage((ServerPlayer) player, Component.translatable("skill.legendary_slash.guard_space"));

                    player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
                        Entity targetEntity = state.getTargetEntity(player.level());
                        if (targetEntity !=null){
                            LSGuardEffect.doTeleport(player, targetEntity);
                        }
                    });
                }else {
                    if (player instanceof ServerPlayer) SlashAttackHandle.sendSkillInfoMessage((ServerPlayer) player, Component.translatable("skill.legendary_slash.guard"));

                }
                mainHandItem.getCapability(ItemSlashBlade.BLADESTATE).orElse(null).updateComboSeq(entityIn, new ResourceLocation(SlashBlade.MODID, "none"));
            }
        }
    }

}

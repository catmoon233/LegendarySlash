package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.CareerSkill;
import net.exmo.rough_blade.content.CareerSkillInstant;
import net.exmo.rough_blade.content.ExSkillHelper;
import net.exmo.rough_blade.content.SkillHandle;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.exmo.rough_blade.entity.FallenStarEntity;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.init.RBSpecialEffectRegistry;
import net.exmo.rough_blade.network.LSVARB;
import net.exmo.rough_blade.utils.AutoInit;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static net.exmo.rough_blade.content.SpecialEffectEx.hasSpecialEffect;
import static net.exmo.rough_blade.content.specialEffects.MingLiSe.MING_LI_UUID;
@Mod.EventBusSubscriber
@AutoInit
public class FallenStarSa extends CareerSkill {
    public FallenStarSa() {
        super("FallenStarSa");
        this.CoolDown = 20*60;
        this.Icon = Items.NETHER_STAR ;
        this.LocalDescription = "KaiSkill_d";
    }
    @SubscribeEvent
    public static void MulitAttribute(ItemAttributeModifierEvent event){
        if (event.getItemStack().getItem() instanceof ItemSlashBlade slashBlade){
            if (hasSpecialEffect(event.getItemStack(), RBSpecialEffectRegistry.StarrySky.getId())){

                ExSkillHelper exSkillHelper = ExSkillHelper.of(event.getItemStack());
                if (exSkillHelper.getSkillsSize()==0) {
                    exSkillHelper.addSkill(CareerSkillInstant.of(SkillHandle.getSkill("StarDashSa"),1),true);
                    exSkillHelper.addSkill(CareerSkillInstant.of(SkillHandle.getSkill("FallenStarSa"),1),true);

                }

            }
        }
    }
    @Override
    public boolean use(Player player) {
        if (player.level().isClientSide)return false;
        if (super.use(player)){
          //  changeCombo(player.getMainHandItem(), SlashBlade.prefix("none"));
            changeCombo(player.getMainHandItem(), ComboStateRegistry.COMBO_A2.getId(),player);
                {
                    LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                    List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                    playerSimpleVars.get(4).setValue("slash_art.fallen_star");
                    playerVariables.syncPlayerVariables(player);
                }
                Rough_blade.queueServerWork(200, () -> {
                            LSVARB.PlayerVariables playerVariables = ExUtils.getPlayerVariables(player);
                            if (player instanceof ServerPlayer) {
                                List<LSVARB.playerSimpleVar<?>> playerSimpleVars = playerVariables.playerSimpleVars;
                                if (((String) playerSimpleVars.get(4).getValue()).contains("slash_art.fallen_star")) {
                                    playerSimpleVars.get(4).setValue(playerSimpleVars.get(4).getDefaultValue());
                                    playerVariables.syncPlayerVariables(player);
                                }
                            }

                        }
                );
                Level level = player.level();

                FallenStarSa.doSlash(player, 5f);

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
            
        // 生成30-50个FallenStar实体
        Random random = new Random();
        int starCount = 40 + random.nextInt(32); // 30-50之间
        
        // 获取玩家视线方向
        Vec3 lookVec = livingEntity.getLookAngle();
        // 计算目标点（玩家视线方向的一定距离）
        Vec3 targetPos = livingEntity.position().add(lookVec.scale(20D));
        
        for (int i = 0; i < starCount; i++) {
            // 延迟生成每颗星
            // 在玩家上方随机位置生成FallenStar
            double x = livingEntity.getX() + (random.nextDouble() - 0.5) * 30.0D;
            double y = livingEntity.getY() + 20.0D + random.nextDouble() * 10.0D; // 在玩家上方20-30格
            double z = livingEntity.getZ() + (random.nextDouble() - 0.5) * 30.0D;
            int finalI = i;

            FallenStarEntity fallenStar = new FallenStarEntity(RBEntityRegistry.FALLEN_STAR_ENTITY, level);
            fallenStar.setPos(x, y, z);
            fallenStar.setOwner(livingEntity);
            // 计算朝向目标点的方向
            Vec3 direction = targetPos.subtract(x, y, z).normalize();

            fallenStar.setLifetime(200);







            // 给予一定的随机性
            direction = direction.add(
                    (random.nextDouble() - 0.5) * 0.3,
                    (random.nextDouble() - 0.5) * 0.3,
                    (random.nextDouble() - 0.5) * 0.3
            ).normalize();

            // 设置速度
            float speed = (float) (1.0D + random.nextDouble() * 0.5D);
            fallenStar.shoot(direction.x , direction.y, direction.z , speed, 0.0F);

            Rough_blade.queueServerWork(finalI * 3, () -> {

                level.addFreshEntity(fallenStar);
            });
        }
    }
}
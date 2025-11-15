package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.exmo.rough_blade.entity.StarrySkySlash;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.utils.ExUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.exmo.rough_blade.content.CareerSkill.changeCombo;
import static net.exmo.rough_blade.init.ComboStateRegistry.findTarget;

public class StarTrackAttack {
    public static void doSlash(Player player,boolean ex){
      if(ex) {
          changeCombo(player.getMainHandItem(), ComboStateRegistry.COMBO_B7.getId(), player);
      }
        ISlashBladeState bladeState = ExUtils.getSlashBlade(player.getMainHandItem());
        if (bladeState!=null){
            Level level = player.level();
            Optional<Entity> foundTarget = findTarget(player, bladeState.getTargetEntity(level));
            foundTarget.ifPresent(
                    entity -> {

                        List<Vec3> circlePoints = createCirclePoints(entity,6,8);
                        circlePoints.forEach(
                                pos1->{
                                    Vec3 targetPos = new Vec3(entity.getX(), entity.getY() + (double) entity.getEyeHeight() * (double) 0.5F, entity.getZ());

                                    StarrySkySlash ss = new StarrySkySlash(RBEntityRegistry.STARRY_SKY_SLASH, level);



                                    ss.setPos(pos1.x, pos1.y+1 , pos1.z);

                                        ss.setDamage((double) bladeState.getRefine() * 0.05f * (bladeState.getProudSoulCount() * 0.0002 ));

                                    Vec3 dir = targetPos.subtract(pos1).normalize();
                                        ss.shoot(dir.x, dir.y, dir.z, 1.5f, 0.0F);

                                    ss.setBaseSize(0.03f);
                                    ss.setOwner(player);
                                    ss.setColor(Color.blue.getRGB());
                                    ss.setLifetime(200);
                                    //  ss.setPierce((byte) 10);
                                    level.addFreshEntity(ss);

                                    if (player instanceof ServerPlayer serverPlayer) {
                                        serverPlayer.playNotifySound(SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 0.2F, 1.45F);

                                        serverPlayer.playNotifySound(SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 0.2F, 1.45F);
                                    }
                                }
                        );
                    }

            );
            if (player instanceof ServerPlayer serverPlayer) {

                serverPlayer.playNotifySound(SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 0.2F, 1.45F);
            }
        }
    }
    
    /**
     * 在给定实体周围创建一个半径为5的圆圈，并返回圆周上均匀分布的8个点的坐标列表
     * @param entity 中心实体
     * @return 包含8个顶点坐标的列表
     */
    public static List<Vec3> createCirclePoints(Entity entity,int radius , int pointCount) {
        List<Vec3> points = new ArrayList<>();

        
        Vec3 center = entity.position().add(0, entity.getBbHeight() / 2, 0);
        
        for (int i = 0; i < pointCount; i++) {
            double angle = 2 * Math.PI * i / pointCount;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y;
            
            points.add(new Vec3(x, y, z));
        }
        
        return points;
    }
}
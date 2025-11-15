package net.exmo.rough_blade.content.shineArt.arts;

import net.exmo.rough_blade.entity.FaZhenBase;
import net.exmo.rough_blade.entity.FaZhenUnder;
import net.exmo.rough_blade.entity.TheBreakSwordPlus;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

public class TheBrokenSword {
    public static void doSlash(LivingEntity livingEntity, float damage, float speed) {
        Level world = livingEntity.level();

        Vec3 _center = livingEntity.position();
        List<TheBreakSwordPlus> entitiesOfClass = world.getEntitiesOfClass(TheBreakSwordPlus.class, new AABB(_center, _center).inflate(3));
        for (TheBreakSwordPlus entity : entitiesOfClass) {
            if (entity.getOwner() == livingEntity &&entity.tickCount>10) {
                entity.discard();
            }
        }
        for (int i = 0; i < 1; i++) {
            TheBreakSwordPlus sword = new TheBreakSwordPlus(RBEntityRegistry.TBS, world);
            sword.setFormationParams(i % 6); // 6个为一组
            sword.setOwner(livingEntity);
            sword.setDamage(damage);
           // sword.setNoGravity(true);
            sword.setInvulnerable(true);
            sword.setColor(Color.GREEN.getRGB());
            sword.startRiding(livingEntity, true);


            sword.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            world.addFreshEntity(sword);
        }
    }

    public static void doSlash2(LivingEntity livingEntity, float damage, float v1) {
        Level world = livingEntity.level();


        for (int i = 0; i < 1; i++) {
            FaZhenBase sword = new FaZhenBase(RBEntityRegistry.FZB, world);

         //   sword.setDamage(damage);
            // sword.setNoGravity(true);
            sword.setInvulnerable(true);
            sword.setColor(Color.GREEN.getRGB());
            sword.setPos(livingEntity.getX(), livingEntity.getY()+1, livingEntity.getZ());
            //sword.startRiding(livingEntity, true);

            sword.getEntityData().set(FaZhenBase.RX,livingEntity.getXRot());
            sword.getEntityData().set(FaZhenBase.RY,livingEntity.getYHeadRot());
            sword.setYBodyRot(livingEntity.getViewYRot(1));
            sword.setOwner(livingEntity);
            world.addFreshEntity(sword);



        }
    }
    public static void doSlash3(LivingEntity livingEntity, float damage, float v1) {
        Level world = livingEntity.level();


        for (int i = 0; i < 1; i++) {
            FaZhenUnder sword = new FaZhenUnder(RBEntityRegistry.FZU, world);

         //   sword.setDamage(damage);
            // sword.setNoGravity(true);
            sword.setInvulnerable(true);
            sword.setColor(Color.GREEN.getRGB());
            sword.setPos(livingEntity.getX(), livingEntity.getY()+25, livingEntity.getZ());
            //sword.startRiding(livingEntity, true);

            sword.setOwner(livingEntity);
            world.addFreshEntity(sword);



        }
    }
}

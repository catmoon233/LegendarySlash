package net.exmo.rough_blade.content.slashArt;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.entity.SummonSwordPROEntity;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FullFireSa {
    public static void doSlash(LivingEntity livingEntity,float damage, float speed){
        livingEntity.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {

            Level worldIn = livingEntity.level();

      //      int rank = livingEntity.getCapability(CapabilityConcentrationRank.RANK_POINT).map(r -> r.getRank(worldIn.getGameTime()).level).orElse(0);
            int count = 4;

            for (int ia = 0 ; ia < 3; ia++) {
                int finalIa = ia;
                Runnable runnable = () -> {
                    for (int i = 0; i < count; i++) {
                        SummonSwordPROEntity ss = new SummonSwordPROEntity(RBEntityRegistry.SUMMONEDSWORDPPROLUS, worldIn);

                        ss.setSpeed(speed);
                        ss.setIsCritical(false);
                        ss.setOwner(livingEntity);
                        ss.setColor(state.getColorCode());
                        ss.setRoll(0);
                        ss.setPso(i);
                        ss.setBaseSize(0.0035f);
                        ss.setParticle(false);
                        ss.setDamage(damage);
                        // force riding
                        ss.startRiding(livingEntity, true);

                        ss.setDelay(3 + finalIa *2);

//                        boolean isRight = ss.getDelay() % 2 == 0;
                        RandomSource random = worldIn.getRandom();

                        double xOffset = random.nextDouble() * 2.5 * (i %2 ==0 ? -1 : 1);
                        double yOffset = random.nextFloat() *2;
                        double zOffset = random.nextFloat() * 0.5;

                        ss.setPos(livingEntity.position().add(xOffset, yOffset, zOffset));
                        ss.setOffset(new Vec3(xOffset, yOffset+1.5, zOffset));

                        livingEntity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
                        worldIn.addFreshEntity(ss);
                    }

                };
                Rough_blade.queueServerWork(ia*2+1, runnable);

            }

        });

    }
}

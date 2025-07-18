package net.exmo.rough_blade.utils;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.capability.slashblade.SlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.network.LSVARB;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ExUtils {
    public static ISlashBladeState getSlashBlade(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSlashBlade itemSlashBlade) {
            return itemStack.getCapability(ItemSlashBlade.BLADESTATE).orElse(new SlashBladeState(itemStack));

        }
        return null;
    }
    public static LSVARB.PlayerVariables getPlayerVariables(Player player) {
        return player.getCapability(LSVARB.PLAYER_VARIABLES_CAPABILITY, null).orElse(new LSVARB.PlayerVariables());
    }
    public static void sendParticleCircle(ServerLevel level, Entity entity, SimpleParticleType particleType, float radius, int count) {
        double posX = entity.getX();
        double posY = entity.getY() + (double)entity.getBbHeight() * (double)0.5F;
        double posZ = entity.getZ();
        RandomSource random = RandomSource.create();

        for(int i = 0; i < count; ++i) {
            double angle = (double)i * ((Math.PI * 2D) / (double)count);
            double offsetX = (double)radius * Math.cos(angle);
            double offsetZ = (double)radius * Math.sin(angle);
            offsetX += random.nextDouble() * 0.1 - 0.05;
            offsetZ += random.nextDouble() * 0.1 - 0.05;
            level.sendParticles(particleType, posX + offsetX, posY, posZ + offsetZ, 1, (double)0.0F, (double)0.0F, (double)0.0F, 0.05);
        }

    }
    public static void sendParticleCircle(ServerLevel level, Vec3 vec3, SimpleParticleType particleType, float radius, int count) {
        double posX = vec3.x();
        double posY = vec3.y() ;
        double posZ = vec3.z();
        RandomSource random = RandomSource.create();

        for(int i = 0; i < count; ++i) {
            double angle = (double)i * ((Math.PI * 2D) / (double)count);
            double offsetX = (double)radius * Math.cos(angle);
            double offsetZ = (double)radius * Math.sin(angle);
            offsetX += random.nextDouble() * 0.1 - 0.05;
            offsetZ += random.nextDouble() * 0.1 - 0.05;
            level.sendParticles(particleType, posX + offsetX, posY, posZ + offsetZ, 1, (double)0.0F, (double)0.0F, (double)0.0F, 0.05);
        }

    }
}

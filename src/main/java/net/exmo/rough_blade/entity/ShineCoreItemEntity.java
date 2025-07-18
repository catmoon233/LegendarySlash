package net.exmo.rough_blade.entity;

import net.exmo.rough_blade.content.SlashTooltipAffix;
import net.exmo.rough_blade.init.RBBuiltInRegistry;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

public class ShineCoreItemEntity extends ItemEntity {
    public ShineCoreItemEntity(EntityType<? extends ItemEntity> p_31991_, Level p_31992_) {
        super(p_31991_, p_31992_);

    }
    public static ShineCoreItemEntity createInstanceFromPacket(PlayMessages.SpawnEntity packet, Level worldIn) {
        return new ShineCoreItemEntity(RBEntityRegistry.ShineCoreEntity, worldIn);
    }

    @Override
    public boolean isInLava() {
        ItemStack item2 = this.getItem();
        if (!item2.getOrCreateTag().getString("blade_id").isEmpty())return false;
        final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> _entfound = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true);
        for (Entity entity : _entfound) {
            if (entity instanceof ItemEntity item) {
                ItemStack item1 = item.getItem();
                if (item1.getItem() == Blocks.GLOWSTONE.asItem()){
                    if (item1.getCount() >= 64){
                        item1.shrink(64);
                        this.setGlowingTag(true);
                        this.setDeltaMovement(0,0.5,0);
                        this.setNoGravity(true);
                        item2.getOrCreateTag().putString("blade_id", SlashTooltipAffix.toTransKey(RBBuiltInRegistry.LiuYing.location()));
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.TRIDENT_RETURN, this.getSoundSource(), 1.0F, 1.0F, false);
                        if (item1.getCount() <=0){
                            item.remove(Entity.RemovalReason.DISCARDED);
                            for(int i = 0; i < 32; ++i) {
                                if(this.level().isClientSide())
                                    break;
                                double xDist = (random.nextFloat() * 2.0F - 1.0F);
                                double yDist = (random.nextFloat() * 2.0F - 1.0F);
                                double zDist = (random.nextFloat() * 2.0F - 1.0F);
                                if (!(xDist * xDist + yDist * yDist + zDist * zDist > 1.0D)) {
                                    double x = item.getX(xDist / 4.0D);
                                    double y = item.getY(0.5D + yDist / 4.0D);
                                    double z = item.getZ(zDist / 4.0D);
                                    ((ServerLevel)this.level()).sendParticles(ParticleTypes.CLOUD, x, y, z,0, xDist, yDist + 0.2D, zDist,1.4);
                                }
                            }
                        }
                        break;
                    }

                }
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel level){
            level.sendParticles(
                    ParticleTypes.END_ROD
                            ,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    1,
                    0.5,
                    0.5,
                    0.5,
                    0.2
            );
        }
    }
}

package net.exmo.rough_blade.mixin.trails.entity;

import it.hurts.octostudios.octolib.modules.particles.OctoRenderManager;

import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.TrailConfig;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.misc.ITrailConfigProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "addEntity", at = @At("TAIL"))
    public void addEntity(int i, Entity entity, CallbackInfo ci) {
        if (!entity.getCommandSenderWorld().isClientSide()
                || !(entity instanceof ITrailConfigProvider provider))
            return;

        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (key == null)
            return;
        var trail = TrailConfig.entityTrails.getOrDefault(key.toString(), null);
        if (trail == null)
            return;

        provider.setTrailConfigData(trail);

        OctoRenderManager.registerProvider(provider);
    }
}
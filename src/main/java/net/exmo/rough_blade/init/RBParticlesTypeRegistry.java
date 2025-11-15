package net.exmo.rough_blade.init;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.particles.StarParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RBParticlesTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE.key(), Rough_blade.MODID);
    public static final RegistryObject<SimpleParticleType> STAR_PARTICLE = PARTICLES.register("star_particle", () -> new SimpleParticleType(true));


}

package net.exmo.rough_blade.content.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nonnull;

public class StarParticle extends ExplodeParticle {

    public StarParticle(ClientLevel p_106576_, double p_106577_, double p_106578_, double p_106579_, double p_106580_, double p_106581_, double p_106582_, SpriteSet p_106583_) {
        super(p_106576_, p_106577_, p_106578_, p_106579_, p_106580_, p_106581_, p_106582_, p_106583_);
    }
    @Nonnull
    public static ParticleProvider<SimpleParticleType> factory(SpriteSet spriteSet) {

        return (data, level, x, y, z, dx, dy, dz) -> new StarParticle(level, x, y, z, dx, dy, dz, spriteSet);
    }
}

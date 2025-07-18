package net.exmo.rough_blade.render;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3f;

public class EnergyParticleOptions implements ParticleOptions {
    private final Vector3f color;
    private final float alpha;

    public EnergyParticleOptions(Vector3f color, float alpha) {
        this.color = color;
        this.alpha = alpha;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypes.END_ROD;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf p_123732_) {

    }

    @Override
    public String writeToString() {
        return "";
    }
}
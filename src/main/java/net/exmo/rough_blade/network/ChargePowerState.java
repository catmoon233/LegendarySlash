package net.exmo.rough_blade.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ChargePowerState implements IChargePowerState {
    protected int maxPower =100;
    protected int chargePower =0;
    protected double chargeEffective = 1;
    protected ResourceLocation shineArt = new ResourceLocation("rough_blade:shine_art/none");
    protected boolean hasChangedActiveState = false;
    protected UUID uniqueId = UUID.randomUUID();

    public ChargePowerState(ItemStack blade) {
        if (!blade.isEmpty() && blade.getOrCreateTag().contains("charge_power_state")) {
            this.deserializeNBT(blade.getTagElement("charge_power_state"));
        }
    }

    @Override
    public int getMaxPower() {
        return maxPower;
    }

    @Override
    public int getChargePower() {
        return chargePower;
    }

    @Override
    public double getChargeEffective() {
        return chargeEffective;
    }

    @Override
    public ResourceLocation getShineArt() {
        return shineArt;
    }

    @Override
    public void setChargePower(int chargePower) {
        this.chargePower = chargePower;
        setHasChangedActiveState(true);
    }

    @Override
    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
        setHasChangedActiveState(true);
    }

    @Override
    public void setChargeEffective(double chargeEffective) {
        this.chargeEffective = chargeEffective;
        setHasChangedActiveState(true);
    }

    @Override
    public void setShineArt(ResourceLocation shineArt) {
        this.shineArt = shineArt;
        setHasChangedActiveState(true);
    }

    @Override
    public boolean hasChangedActiveState() {
        return hasChangedActiveState;

    }

    @Override
    public void setHasChangedActiveState(boolean var1) {
        this.hasChangedActiveState = var1;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(UUID var1) {
        this.uniqueId = var1;
        setHasChangedActiveState(true);
    }
}

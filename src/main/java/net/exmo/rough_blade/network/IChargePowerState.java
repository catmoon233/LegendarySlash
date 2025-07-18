package net.exmo.rough_blade.network;


import mods.flammpfeil.slashblade.network.NetworkManager;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public interface IChargePowerState extends INBTSerializable<CompoundTag> {
    int getMaxPower();
    int getChargePower();
    double getChargeEffective();
    ResourceLocation getShineArt();
    void setChargePower(int chargePower);
    void setMaxPower(int maxPower);
    void setChargeEffective(double chargeEffective);
    void setShineArt(ResourceLocation shineArt);
    boolean hasChangedActiveState();
    void setHasChangedActiveState(boolean var1);
    UUID getUniqueId();
    void setUniqueId(UUID var1);
    @Override
    default CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("chargePower", getChargePower());
        tag.putInt("maxPower", getMaxPower());
        tag.putDouble("chargeEffective", getChargeEffective());
        tag.putString("shineArt", getShineArt().toString());
        UUID bladeId = this.getUniqueId();
        tag.putUUID("BladeUniqueId", bladeId);
        this.setUniqueId(tag.hasUUID("BladeUniqueId") ? tag.getUUID("BladeUniqueId") : UUID.randomUUID());
        tag.putBoolean("hasChangedActiveState", hasChangedActiveState());
        return tag;
    }

    @Override
    default   void deserializeNBT(CompoundTag compoundTag) {
        setChargePower(compoundTag.getInt("chargePower"));
        setMaxPower(compoundTag.getInt("maxPower"));
        setChargeEffective(compoundTag.getDouble("chargeEffective"));
        setShineArt(ResourceLocation.tryParse(compoundTag.getString("shineArt")));
        setHasChangedActiveState(compoundTag.getBoolean("hasChangedActiveState"));
    }
    default void sendChanges(Entity entityIn) {
        if (!entityIn.level().isClientSide() && this.hasChangedActiveState()) {
            ActiveState2SyncMessage msg = new ActiveState2SyncMessage();
            msg.activeTag = this.getActiveState();
            msg.id = entityIn.getId();
            NetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn), msg);
            this.setHasChangedActiveState(false);
        }

    }
    default CompoundTag getActiveState() {
        CompoundTag tag = new CompoundTag();
        NBTHelper.getNBTCoupler(tag).put("chargePower",getChargePower()).put("maxPower",getMaxPower()).put("chargeEffective",getChargeEffective()).put("shineArt",getShineArt());
        return tag;
    }

    default void setActiveState(CompoundTag tag) {
        NBTHelper.getNBTCoupler(tag).get("chargePower",this::setChargePower)
                .get("maxPower",this::setMaxPower)
                .get("chargeEffective",this::setChargeEffective)
                .get("shineArt",this::setShineArt);
        this.setHasChangedActiveState(false);
    }
}

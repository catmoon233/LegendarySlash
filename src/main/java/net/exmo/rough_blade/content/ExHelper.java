package net.exmo.rough_blade.content;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;


public class ExHelper {
    private CompoundTag nbt;
    public ItemStack itemStack;
    public static final String EXMO_NBT = "exmo_nbt";

    public ExHelper(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.nbt = itemStack.getTag();
    }
    public void createNbt(){
     nbt = itemStack.getOrCreateTag();
    }
    public void createMainNbt(){
        nbt.put(EXMO_NBT,new CompoundTag());
    }
    public boolean ValidMainNbt(){
        if (nbt==null)return false;
        return nbt.contains(EXMO_NBT);
    }
    public CompoundTag getMainNbt(){
        if (nbt==null)return new CompoundTag();
        return nbt.getCompound(EXMO_NBT);
    }

}
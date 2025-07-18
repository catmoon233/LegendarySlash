package net.exmo.rough_blade.network;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;

import net.exmo.rough_blade.content.shineArt.ShineArt;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActiveState2SyncMessage {
    public CompoundTag activeTag;
    public int id;

    public ActiveState2SyncMessage() {
    }

    public static ActiveState2SyncMessage decode(FriendlyByteBuf buf) {
        ActiveState2SyncMessage msg = new ActiveState2SyncMessage();
        msg.id = buf.readInt();
        msg.activeTag = buf.readNbt();
        return msg;
    }

    public static void encode(ActiveState2SyncMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeNbt(msg.activeTag);
    }

    public static void handle(ActiveState2SyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            if (msg.activeTag.hasUUID("BladeUniqueId")) {

                ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
                Entity target = Minecraft.getInstance().level.getEntity(msg.id);
                if (target instanceof LivingEntity) {
                    ItemStack stack = ((LivingEntity) target).getItemInHand(InteractionHand.MAIN_HAND);
                    if (stack.isEmpty()) {
                        return;
                    }

                    if (!(stack.getItem() instanceof ItemSlashBlade)) {
                        return;
                    }

                    CompoundTag tag = stack.getOrCreateTag();
                    stack.getCapability(ShineArt.BLADESTATE_PLUST).filter((state) -> state.getUniqueId().equals(msg.activeTag.getUUID("BladeUniqueId"))).ifPresent((state) -> {
                        state.setActiveState(msg.activeTag);
                        tag.put("charge_power_state", state.serializeNBT());
                    });
                }
            }
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}

package net.exmo.rough_blade.network;

import net.exmo.rough_blade.content.client.MingLiRenderEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record ClearMingLiChangeMessage(UUID uuid) {


        public static void encode(ClearMingLiChangeMessage msg, FriendlyByteBuf buffer) {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("uuid", msg.uuid);
            buffer.writeNbt(tag);


        }
        public static void handle(ClearMingLiChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> MingLiRenderEvent.MingLiMap.remove(msg.uuid));
            ctx.get().setPacketHandled(true);
        }
        public static ClearMingLiChangeMessage decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readNbt();
            return new ClearMingLiChangeMessage(tag.getUUID("uuid"));
        }

    }


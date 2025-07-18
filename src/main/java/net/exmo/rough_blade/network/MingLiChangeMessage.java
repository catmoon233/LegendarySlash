package net.exmo.rough_blade.network;

import net.exmo.rough_blade.content.client.MingLiRenderEvent;
import net.exmo.rough_blade.content.client.screen.ExSkillOverlay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record MingLiChangeMessage(UUID uuid,int level) {


        public static void encode(MingLiChangeMessage msg, FriendlyByteBuf buffer) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("level", msg.level);
            tag.putUUID("uuid", msg.uuid);
            buffer.writeNbt(tag);


        }
        public static void handle(MingLiChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> MingLiRenderEvent.MingLiMap.put(msg.uuid,msg.level));
            ctx.get().setPacketHandled(true);
        }
        public static MingLiChangeMessage decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readNbt();
            return new MingLiChangeMessage(tag.getUUID("uuid"),tag.getInt("level"));
        }

    }


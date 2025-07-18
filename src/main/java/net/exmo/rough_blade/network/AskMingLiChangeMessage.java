package net.exmo.rough_blade.network;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.client.MingLiRenderEvent;
import net.exmo.rough_blade.content.specialEffects.MingLiEffectHandle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public record AskMingLiChangeMessage(UUID uuid) {


        public static void encode(AskMingLiChangeMessage msg, FriendlyByteBuf buffer) {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("uuid", msg.uuid);
            buffer.writeNbt(tag);


        }
        public static void handle(AskMingLiChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                Entity entity = sender.serverLevel().getEntity(msg.uuid);
                if (entity instanceof  LivingEntity livingEntity) {
                    if (MingLiEffectHandle.isMingLi(livingEntity)) {
                        int mingLi = MingLiEffectHandle.getMingLi(livingEntity);
                        MingLiChangeMessage mingLiChangeMessage = new MingLiChangeMessage(msg.uuid, mingLi);
                        Rough_blade.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) sender), mingLiChangeMessage);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
        public static AskMingLiChangeMessage decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readNbt();
            return new AskMingLiChangeMessage(tag.getUUID("uuid"));
        }

    }


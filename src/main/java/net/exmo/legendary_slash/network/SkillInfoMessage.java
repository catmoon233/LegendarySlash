package net.exmo.legendary_slash.network;

import net.exmo.legendary_slash.content.client.screen.ExSkillOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SkillInfoMessage(Component skill_name) {


        public static void encode(SkillInfoMessage msg, FriendlyByteBuf buffer) {
            buffer.writeComponent(msg.skill_name());


        }
        public static void handle(SkillInfoMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> ExSkillOverlay.lastUpdate.put( System.currentTimeMillis(),msg.skill_name()));
            ctx.get().setPacketHandled(true);
        }
        public static SkillInfoMessage decode(FriendlyByteBuf buffer) {
            return new SkillInfoMessage(buffer.readComponent());
        }

    }


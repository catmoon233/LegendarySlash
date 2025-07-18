package net.exmo.rough_blade.network;


import net.exmo.rough_blade.Rough_blade;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.exmo.rough_blade.Rough_blade.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CareerWarModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		Rough_blade.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerVariables.class);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			clone.career = original.career;
			clone.HasCareer = original.HasCareer;
			clone.CareerProficiency = original.CareerProficiency;
			if (!event.isWasDeath()) {
				clone.playercooldown = original.playercooldown;
				clone.HeavenlyEyeUUID = original.HeavenlyEyeUUID;
				clone.magicGether = original.magicGether;
				clone.KillRecord = original.KillRecord;
			}
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}
	}

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
	});

	@Mod.EventBusSubscriber
	private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
				event.addCapability(new ResourceLocation("rough_blade", "player_variables"), new PlayerVariablesProvider());
		}

		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LSVARB.PlayerVariables playerVariables1 = new LSVARB.PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);
		private final LazyOptional<LSVARB.PlayerVariables> instance1 = LazyOptional.of(() -> playerVariables1);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == PLAYER_VARIABLES_CAPABILITY){
				return instance.cast();
			}
			if (cap == LSVARB.PLAYER_VARIABLES_CAPABILITY){
				return instance1.cast();
			}
			return  LazyOptional.empty();
		}

		@Override
		public Tag serializeNBT() {
			//marge p1
			return ((CompoundTag) playerVariables.writeNBT()).merge(((CompoundTag) playerVariables1.writeNBT()));

		}

		@Override
		public void deserializeNBT(Tag nbt) {
			playerVariables.readNBT(nbt) ;
			playerVariables1.readNBT(nbt);
		}
	}

	public static class PlayerVariables {
		public String career = "\"\"";
		public String HasCareer = "\"\"";
		public String HeavenlyEyeUUID = "";
		public Map<String, Integer> playercooldown = new HashMap<>();
		public Map<String, Integer> CareerProficiency = new HashMap<>();
		public String magicGether = "\"\"";
		public int KillRecord =0;
		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				Rough_blade.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(entity.level()::dimension), new PlayerVariablesSyncMessage(this, entity.getId()));
		}

		public Tag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			if (career!=null) nbt.putString("career", career);
			nbt.putString("HasCareer", HasCareer);
			nbt.putString("HeavenlyEyeUUID", HeavenlyEyeUUID);
			CompoundTag stringMapTag = new CompoundTag();
			for (Map.Entry<String, Integer> entry : playercooldown.entrySet()) {
				stringMapTag.putInt(entry.getKey(), entry.getValue());
			}
			nbt.put("playercooldown", stringMapTag);

			CompoundTag stringMapTag1 = new CompoundTag();
			for (Map.Entry<String, Integer> entry : CareerProficiency.entrySet()) {
				stringMapTag1.putInt(entry.getKey(), entry.getValue());
			}
			nbt.put("CareerProficiency", stringMapTag1);

			nbt.putInt("KillRecord", KillRecord);
			nbt.putString("magicGether", magicGether);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			career = nbt.getString("career");
			HasCareer = nbt.getString("HasCareer");
			HeavenlyEyeUUID = nbt.getString("HeavenlyEyeUUID");
			CompoundTag stringMapTag = nbt.getCompound("playercooldown");
			for (String key : stringMapTag.getAllKeys()) {
				int value = stringMapTag.getInt(key);
				playercooldown.put(key, value);
			}

			CompoundTag stringMapTag1 = nbt.getCompound("CareerProficiency");
			for (String key : stringMapTag1.getAllKeys()) {
				int value = stringMapTag1.getInt(key);
				CareerProficiency.put(key, value);
			}
			magicGether = nbt.getString("magicGether");
			KillRecord = nbt.getInt("KillRecord");
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		Rough_blade.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	public static class PlayerVariablesSyncMessage {
		private final int target;
		private final PlayerVariables data;

		public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerVariables();
			this.data.readNBT(buffer.readNbt());
			this.target = buffer.readInt();
		}

		public PlayerVariablesSyncMessage(PlayerVariables data, int entityid) {
			this.data = data;
			this.target = entityid;
		}

		public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeNbt((CompoundTag) message.data.writeNBT());
			buffer.writeInt(message.target);
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.level().getEntity(message.target).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
					variables.career = message.data.career;
					variables.HasCareer = message.data.HasCareer;
					variables.playercooldown = message.data.playercooldown;
					variables.magicGether = message.data.magicGether;
					variables.CareerProficiency = message.data.CareerProficiency;
					variables .KillRecord = message.data.KillRecord;
					variables.HeavenlyEyeUUID = message.data.HeavenlyEyeUUID;
				}
			});
			context.setPacketHandled(true);
		}
	}
}

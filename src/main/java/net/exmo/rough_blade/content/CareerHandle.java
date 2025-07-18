package net.exmo.rough_blade.content;

import com.mojang.util.UUIDTypeAdapter;
import io.netty.buffer.Unpooled;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.network.CareerWarModVariables;
import net.exmo.rough_blade.utils.AutoInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.network.NetworkHooks;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.util.*;



@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CareerHandle {

	public final static Random random = new Random();

	public static void init() throws Exception {

	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) throws Exception {

		SkillHandle.registeredSkills = new ArrayList<>();

		CareerHandle.init();
		load(CareerSkill.class);
	}
	@SuppressWarnings("unchecked")
	private static <T> T getPlugin(String className, Class<T> type) {
		try {
			Class<?> asmClass = Class.forName(className);
			if (!type.isAssignableFrom(asmClass)) return null;
			Class<? extends T> asmInstanceClass = asmClass.asSubclass(type);
			Constructor<? extends T> constructor = asmInstanceClass.getDeclaredConstructor();
			T instance = constructor.newInstance();
			return instance;
		} catch (ReflectiveOperationException | LinkageError e) {
			System.err.println("Failed to load plugin: " + className);
			e.printStackTrace();
		}
		return null;
	}
	public static <T> Collection<T> load(Class<T> pluginInterface) {
		List<T> plugins = new ArrayList<>();

		for (ModFileScanData scanData : ModList.get().getAllScanData()) {
			Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
			for (ModFileScanData.AnnotationData a : annotations) {
				if (Objects.equals(a.annotationType(), Type.getType(AutoInit.class))) {
					String memberName = a.memberName();
					T plugin = getPlugin(memberName, pluginInterface);
					if (plugin != null) plugins.add(plugin);
				}
			}
		}
		return plugins;
	}

	@Mod.EventBusSubscriber
	public static class ForgeBusEvents {

		@SubscribeEvent
		public static void onQ(LivingDropsEvent event) {
			if (event.getEntity() instanceof Player player) {
				event.getDrops().forEach(item -> {
					ItemStack item1 = item.getItem();
					if (item1.getTag() != null && item1.getTag().contains("Bing")) {
						player.addItem(item1);
						item.remove(Entity.RemovalReason.DISCARDED);
					}
				});
			}
		}

		@SubscribeEvent
		public static void onM(ItemStackedOnOtherEvent event) {
			if (event.getCarriedItem().getTag() != null && event.getCarriedItem().getTag().contains("Bind"))
				event.setCanceled(true);
			if (event.getSlot().getItem().getTag() != null && event.getSlot().getItem().getTag().contains("Bind"))
				event.setCanceled(true);
			//	if (event.getCarriedItem().getTag() !=null&& event.getSlot().getItem().getTag().contains("Bind"))event.setCanceled(true);
		}


		@SubscribeEvent
		public static void playerTick(TickEvent.PlayerTickEvent e) {
			Player player = e.player;
			if (player.level().isClientSide) return;
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

				Map<String, Integer> s = capability.playercooldown;
				for (Map.Entry<String, Integer> entry : s.entrySet()) {
					String key = entry.getKey();
					Integer value = entry.getValue();
					if ((value) > 0) {
						CareerSkill skill = SkillHandle.getSkill(key);
						if (skill != null) {
							if (skill.isSelfCooldown) continue;
						}
						s.put(key, (value - 1));
					}
				}
				capability.playercooldown = s;
				capability.syncPlayerVariables(player);
			});
		}


		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
		}
	}
}


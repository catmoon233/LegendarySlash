package net.exmo.legendary_slash;

import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.exmo.legendary_slash.config.ClientConfigs;

import net.exmo.legendary_slash.content.effects.LSArmorStart;
import net.exmo.legendary_slash.content.effects.LSGuardEffect;
import net.exmo.legendary_slash.content.effects.LSGuardSuccessEffect;
import net.exmo.legendary_slash.init.*;
import net.exmo.legendary_slash.network.DashMessage;
import net.exmo.legendary_slash.network.SkillInfoMessage;
import net.exmo.legendary_slash.render.entity.EntityDrivePlusRenderer;
import net.exmo.legendary_slash.render.entity.SummonedSwordPlusRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Legendary_slash.MODID)
public class Legendary_slash {
    private static final String PROTOCOL_VERSION = "1";
    public static final String MODID = "legendary_slash";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path);
    }
    @SuppressWarnings("Deprecated")
    public Legendary_slash() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PACKET_HANDLER.messageBuilder(DashMessage.class, messageID++)
                .encoder(DashMessage::encode)
                .decoder(DashMessage::decode)
                .consumerMainThread(DashMessage::handle)
                .add();
        PACKET_HANDLER.messageBuilder(SkillInfoMessage.class, messageID++)
                .encoder(SkillInfoMessage::encode)
                .decoder(SkillInfoMessage::decode)
                .consumerMainThread(SkillInfoMessage::handle)
                .add();
        modEventBus.addListener(OverlayRegistry::onRegisterOverlays);
        ComboStateRegistry.COMBO_STATE.register(modEventBus);
        effectAbout.REGISTRY.register(modEventBus);

        LSSlashArtRegistry.SLASH_ARTS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::dataGen);
        LSEntityRegistry.register(modEventBus);
        LSSpecialEffectRegistry.REGISTRY_KEY2.register(modEventBus);
        modEventBus.addListener(LSEntityRegistry::registerEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);


    }
    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }
    public static class effectAbout{
        public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Legendary_slash.MODID);
        public static final RegistryObject<LSGuardEffect> GuardEffect = REGISTRY.register("guard_effect", LSGuardEffect::new);
        public static final RegistryObject<LSArmorStart> ArmorStart = REGISTRY.register("armor_start", LSArmorStart::new);
        public static final RegistryObject<LSGuardSuccessEffect> GuardEffectSuc = REGISTRY.register("guard_effect_suc", LSGuardSuccessEffect::new);
    }
    public  void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        final RegistrySetBuilder bladeBuilder = new RegistrySetBuilder().add(SlashBladeDefinition.REGISTRY_KEY,
                LSBuiltInRegistry::registerAll);
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        dataGenerator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, bladeBuilder, Set.of(MODID)));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

}

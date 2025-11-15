package net.exmo.rough_blade;

import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.exmo.rough_blade.config.ClientConfigs;

import net.exmo.rough_blade.content.effects.*;
import net.exmo.rough_blade.init.*;
import net.exmo.rough_blade.network.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
@Mod(Rough_blade.MODID)
public class    Rough_blade {
    private static final String PROTOCOL_VERSION = "1";
    public static final String MODID = "rough_blade";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public final static  RegistryObject<CreativeModeTab> ExModifierTab =  CREATIVE_MODE_TABS.register("rough_blade_tab", () -> CreativeModeTab.builder()
            .icon(Rough_blade::getTabIcon)
            .withSearchBar()
            .title(Component.translatable("itemGroup.rough_blade_tab"))
            .displayItems((parameters, output) -> {
                for (var a : RBItem.ITEMS.getEntries().stream().toList()){
                    output.accept(a.get());
                }
            }).build());
    private static ItemStack getTabIcon() {
       ItemStack TabIcon = new ItemStack(RBItem.SHINE_CORE.get());

        return TabIcon;
    }
    public static <MSG> void registerMessage(Class<MSG> messageClass) {
        try {
            // 获取 encode 方法
            java.lang.reflect.Method encodeMethod = messageClass.getMethod("encode", messageClass, FriendlyByteBuf.class);
            // 获取 decode 方法
            java.lang.reflect.Method decodeMethod = messageClass.getMethod("decode", FriendlyByteBuf.class);
            // 获取 handle 方法
            java.lang.reflect.Method handleMethod = messageClass.getMethod("handle", messageClass, Supplier.class);

            // 将方法转换为 BiConsumer 和 Function
            BiConsumer<MSG, FriendlyByteBuf> encoder = (msg, buffer) -> {
                try {
                    encodeMethod.invoke(null, msg, buffer);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to encode message", e);
                }
            };

            Function<FriendlyByteBuf, MSG> decoder = buffer -> {
                try {
                    return (MSG) decodeMethod.invoke(null, buffer);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode message", e);
                }
            };

            BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer = (msg, ctx) -> {
                try {
                    handleMethod.invoke(null, msg, ctx);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to handle message", e);
                }
            };

            // 注册消息
            PACKET_HANDLER.registerMessage(messageID++, messageClass, encoder, decoder, messageConsumer);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find required methods in message class " + messageClass.getName(), e);
        }
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path);
    }
    @SuppressWarnings("Deprecated")
    public Rough_blade() {

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
        PACKET_HANDLER.messageBuilder(ActiveState2SyncMessage.class, messageID++)
                .encoder(ActiveState2SyncMessage::encode)
                .decoder(ActiveState2SyncMessage::decode)
                .consumerMainThread(ActiveState2SyncMessage::handle)
                .add();

        registerMessage(MingLiChangeMessage.class);
        registerMessage(ClearMingLiChangeMessage.class);
        registerMessage(AskMingLiChangeMessage.class);
        modEventBus.addListener(OverlayRegistry::onRegisterOverlays);
        ComboStateRegistry.COMBO_STATE.register(modEventBus);
        effectAbout.REGISTRY.register(modEventBus);

        RBSlashArtRegistry.SLASH_ARTS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::dataGen);
        RBEntityRegistry.register(modEventBus);
        RBSpecialEffectRegistry.REGISTRY_KEY2.register(modEventBus);
        RBParticlesTypeRegistry.PARTICLES.register(modEventBus);
        modEventBus.addListener(RBEntityRegistry::registerEvent);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC);
        Config.tryLoad();


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
        public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Rough_blade.MODID);
        public static final RegistryObject<LSGuardEffect> GuardEffect = REGISTRY.register("guard_effect", LSGuardEffect::new);
        public static final RegistryObject<LSArmorStart> ArmorStart = REGISTRY.register("armor_start", LSArmorStart::new);
        public static final RegistryObject<LSShineDance> SHINE_DANCE = REGISTRY.register("shine_dance", LSShineDance::new);
        public static final RegistryObject<LSGuardSuccessEffect> GuardEffectSuc = REGISTRY.register("guard_effect_suc", LSGuardSuccessEffect::new);
        public static final RegistryObject<LSMingYunEffect> MingYunEffect = REGISTRY.register("ming_li_effect", LSMingYunEffect::new);
        public static final RegistryObject<LSZhenLiEffect> ZhenLiEffect = REGISTRY.register("zhen_li_effect", LSZhenLiEffect::new);
        public static final RegistryObject<StarImprintEffect> StarImprintEffect = REGISTRY.register("star_imprint", net.exmo.rough_blade.content.effects.StarImprintEffect::new);
        public static final RegistryObject<TheInfinityMoonEffect> TheInfinityMoonEffect = REGISTRY.register("the_infinity_moon", net.exmo.rough_blade.content.effects.TheInfinityMoonEffect::new);
    }
    public  void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        final RegistrySetBuilder bladeBuilder = new RegistrySetBuilder().add(SlashBladeDefinition.REGISTRY_KEY,
                RBBuiltInRegistry::registerAll);
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

package net.exmo.rough_blade.init;

import net.exmo.rough_blade.Rough_blade;
import net.exmo.rough_blade.content.item.ShineCoreItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Rough_blade.MODID);
    public static final RegistryObject<ShineCoreItem> SHINE_CORE = ITEMS.register("shine_core", () -> new ShineCoreItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> Shine_Fire  = ITEMS.register("shine_fire", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
            components.add(Component.translatable("tooltip.rough_blade.shine_fire"));
            super.appendHoverText(stack, p_41422_, components, p_41424_);
        }
    });
    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        });
    }

}

package net.exmo.rough_blade.init;

import net.exmo.rough_blade.Rough_blade;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBAttribute {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Rough_blade.MODID);

    // 属性注册对象
    public static final RegistryObject<Attribute> Max_Slash_Power = registerAttribute("max_slash_power", 200, 0, Double.MAX_VALUE);
    public static final RegistryObject<Attribute> Slash_Power_effect = registerAttribute("slash_power_effect", 1, -Double.MAX_VALUE, Double.MAX_VALUE);
    private static RegistryObject<Attribute> registerAttribute(String name, double defaultValue, double minValue, double maxValue) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + Rough_blade.MODID + "." + name, defaultValue, minValue, maxValue).setSyncable(true));
    }
    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
        });
    }
    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        List<EntityType<? extends LivingEntity>> entityTypes = event.getTypes();

        entityTypes.forEach((e) -> {

            if (e.equals(EntityType.PLAYER)) {
                event.add(e, Max_Slash_Power.get());
                event.add(e, Slash_Power_effect.get());

            }
        });


    }
    @Mod.EventBusSubscriber
    public static class Utils {
        @SubscribeEvent
        public static void persistAttributes(PlayerEvent.Clone event) {
            Player oldP = event.getOriginal();
            Player newP = (Player) event.getEntity();
            newP.getAttribute(Max_Slash_Power.get()).setBaseValue(oldP.getAttribute(Max_Slash_Power.get()).getBaseValue());
            newP.getAttribute(Slash_Power_effect.get()).setBaseValue(oldP.getAttribute(Slash_Power_effect.get()).getBaseValue());

        }
    }
}

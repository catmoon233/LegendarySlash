package net.exmo.legendary_slash.init;

import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.exmo.legendary_slash.Legendary_slash;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;

import java.awt.*;
import java.util.List;

import static net.exmo.legendary_slash.content.SpecialEffectEx.getEnchantmentID;

public class LSBuiltInRegistry {
    //流萤
    public static final ResourceKey<SlashBladeDefinition> LiuYing;

    static {
        LiuYing = register("liu_ying");
    }
    private static ResourceKey<SlashBladeDefinition> register(String id) {
        return ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY, Legendary_slash.prefix(id));
    }
    public static void registerAll(BootstapContext<SlashBladeDefinition> bootstrap) {
        bootstrap.register(
                LiuYing, new SlashBladeDefinition(Legendary_slash.prefix("liu_ying"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(Legendary_slash.prefix("textures/model/sam.png"))
                                .modelName(Legendary_slash.prefix("models/sam.obj"))
                                .effectColor(Color.green.getRGB())
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .slashArtsType(LSSlashArtRegistry.FULL_FIRE.getId())
                                .baseAttackModifier(3)
                                .addSpecialEffect(LSSpecialEffectRegistry.STAR_FIRE.getId())
                                .maxDamage(180)
                                .build(),
                        List.of(
                                new EnchantmentDefinition(new ResourceLocation("minecraft","sharpness"),17)
                                , new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 17),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 17),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 17)
                        )
                ));
    }
    }

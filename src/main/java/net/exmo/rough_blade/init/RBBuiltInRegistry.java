package net.exmo.rough_blade.init;

import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.exmo.rough_blade.Rough_blade;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;

import java.awt.*;
import java.util.List;

import static net.exmo.rough_blade.content.SpecialEffectEx.getEnchantmentID;

public class RBBuiltInRegistry {
    //流萤
    public static final ResourceKey<SlashBladeDefinition> LiuYing;
    public static final ResourceKey<SlashBladeDefinition> MingLi;

    static {
        LiuYing = register("liu_ying");
        MingLi = register("ming_li");
    }
    private static ResourceKey<SlashBladeDefinition> register(String id) {
        return ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY, Rough_blade.prefix(id));
    }
    public static void registerAll(BootstapContext<SlashBladeDefinition> bootstrap) {
        bootstrap.register(
                LiuYing, new SlashBladeDefinition(Rough_blade.prefix("liu_ying"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(Rough_blade.prefix("textures/model/sam.png"))
                                .modelName(Rough_blade.prefix("models/sam.obj"))
                                .effectColor(Color.green.getRGB())
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                                .slashArtsType(RBSlashArtRegistry.FULL_FIRE.getId())
                                .baseAttackModifier(3)
                                .addSpecialEffect(RBSpecialEffectRegistry.STAR_FIRE.getId())
                                .maxDamage(180)
                                .build(),
                        List.of(
                                new EnchantmentDefinition(new ResourceLocation("minecraft","sharpness"),17)
                                , new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 17),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 17),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 17)
                        )
                ));
        bootstrap.register(
                MingLi, new SlashBladeDefinition(Rough_blade.prefix("ming_li"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(Rough_blade.prefix("textures/model/mingli.png"))
                                .modelName(Rough_blade.prefix("models/mingli.obj"))
                                .effectColor(Color.RED.getRGB())
                                .build(),
                        PropertiesDefinition.Builder.newInstance()
                                .defaultSwordType(List.of(SwordType.BEWITCHED))
                               // .slashArtsType(LSSlashArtRegistry.FULL_FIRE.getId())
                                .baseAttackModifier(1)
                                .addSpecialEffect(RBSpecialEffectRegistry.MingLi.getId())
                                .maxDamage(60)
                                .build(),
                        List.of(
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_PROTECTION), 0),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 2),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 1)


                        )
                ));
    }
    }

package net.exmo.rough_blade.content;

import mods.flammpfeil.slashblade.client.renderer.entity.SummonedSwordRenderer;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.exmo.rough_blade.Config;
import net.exmo.rough_blade.content.client.SlashBladeIItemDamageDecorator;
import net.exmo.rough_blade.content.client.SlashBladeIItemDecorator;
import net.exmo.rough_blade.entity.StarrySkySlash;
import net.exmo.rough_blade.init.RBEntityRegistry;
import net.exmo.rough_blade.render.SlashBladeRender;
import net.exmo.rough_blade.render.entity.*;
import net.exmo.rough_blade.render.other.AngelWingsLayer;
import net.exmo.rough_blade.render.other.AngelWingsModel;
import net.exmo.rough_blade.render.other.MingLiLayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static net.exmo.rough_blade.Rough_blade.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RBClientEvent {
        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {

            event.registerEntityRenderer(RBEntityRegistry.DRIVEPLUS, EntityDrivePlusRenderer::new);
            event.registerEntityRenderer(RBEntityRegistry.STARRY_SKY_SLASH, StarrySkySlashRenderer::new);
            event.registerEntityRenderer(RBEntityRegistry.SUMMONEDSWORDPLUS, SummonedSwordPlusRenderer::new);
            event.registerEntityRenderer(RBEntityRegistry.SUMMONEDSWORDPPROLUS, SummonedSwordPlusRenderer::new);
            event.registerEntityRenderer(RBEntityRegistry.TBS, TheBrokenSwordRender::new);
            event.registerEntityRenderer(RBEntityRegistry.FZB, FaZhenBaseRender::new);
            event.registerEntityRenderer(RBEntityRegistry.FZU, FaZhenUnderRender::new);
            event.registerEntityRenderer(RBEntityRegistry.JIGUAN, LaserUnderRender::new);
            event.registerEntityRenderer(RBEntityRegistry.ZLENTITY, ZhenLiRender:: new);
            event.registerEntityRenderer(RBEntityRegistry.STORMSOWRDPLUS, SummonedSwordRenderer::new);
            event.registerEntityRenderer(RBEntityRegistry.ShineCoreEntity, ItemEntityRenderer::new);


        }
        @SubscribeEvent
        public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
           Config.tryLoad();
            ForgeRegistries.ITEMS.forEach(item ->
            {
                if (item instanceof ItemSlashBlade itemSlashBlade){
                  event.register(item, new SlashBladeRender());
                    event.register(item, new SlashBladeIItemDecorator());
                    event.register(item, new SlashBladeIItemDamageDecorator());
                }
            });
//            event.register(SBItems.slashblade, new SlashBladeIItemDecorator());
//            event.register(SBItems.slashblade_bamboo, new SlashBladeIItemDecorator());
//            event.register(SBItems.slashblade_silverbamboo, new SlashBladeIItemDecorator());
//            event.register(SBItems.slashblade_white, new SlashBladeIItemDecorator());
//            event.register(SBItems.slashblade_wood, new SlashBladeIItemDecorator());
        }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        addLayerToPlayerSkin(event, "default");
        addLayerToPlayerSkin(event, "slim");
//        for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues()) {
//            // 修复类型转换：直接使用EntityType进行判断和转型
//            if (LivingEntity.class.isAssignableFrom(entityType.getBaseClass())) {
//                @SuppressWarnings("unchecked")
//                EntityType<? extends LivingEntity> livingType = (EntityType<? extends LivingEntity>) entityType;
//                EntityRenderer<? extends LivingEntity> renderer = event.getRenderer(livingType);
//                // 添加空指针检查
//                if (renderer instanceof LivingEntityRenderer livingRenderer) {
//                    livingRenderer.addLayer(new MingLiLEffectayer(livingRenderer));
//                }
//            }
//        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new AngelWingsLayer<>(livingRenderer));
            livingRenderer.addLayer(new MingLiLayer<>(livingRenderer));


        }


    /*.valu((entityType)->{
            if(entityType. instanceof EntityType<LivingEntity> livingType)
            if(event.getRenderer(entityType) instanceof EntityRenderer<? extends LivingEntity> livingRenderer)
                livingRenderer.addlayer
        });*/

        //EntityRenderer<? extends AbstractSpellCastingMob> renderer = event.getRenderer(EntityRegistry.PYROMANCER.get());
    }
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(AngelWingsModel.ANGEL_WINGS_LAYER, AngelWingsModel::createLayer);

    }
}

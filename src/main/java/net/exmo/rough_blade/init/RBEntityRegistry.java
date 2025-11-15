package net.exmo.rough_blade.init;

import com.google.common.base.CaseFormat;
import net.exmo.rough_blade.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import static net.exmo.rough_blade.Rough_blade.MODID;

public class RBEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES;
    public static final ResourceLocation DRIVEPLUSLoc;
    public static final ResourceLocation SUMMONEDSWORDLoc;
    public static final ResourceLocation STORMSOWRDPLUSLoc;
    public static final ResourceLocation ShineCoreEntityLoc;
    public static EntityType<EntityStormSwordsPlus> STORMSOWRDPLUS;
    public static EntityType<TheBreakSwordPlus> TBS;
    public static ResourceLocation TBSLoc;
    public static final ResourceLocation SUMMONEDSWORDPROLoc;
    public static EntityType<EntityDrivePlus> DRIVEPLUS;

    public static EntityType<StarrySkySlash> STARRY_SKY_SLASH;
    public static EntityType<FallenStarEntity> FALLEN_STAR_ENTITY;
    public static EntityType<SummonedSwordPlus> SUMMONEDSWORDPLUS;
    public static EntityType<SummonSwordPROEntity> SUMMONEDSWORDPPROLUS;
    public static EntityType<ShineCoreItemEntity> ShineCoreEntity;
    public static EntityType<ZhenLiEntity> ZLENTITY;
    public static EntityType<TrackTailEntity> TRACK_TAIL_ENTITY;

    public static final ResourceLocation ZLENTITYLoc;
    public static final ResourceLocation STARRY_SKY_SLASH_LOC;
    public static final ResourceLocation FALLEN_STAR_ENTITY_LOC;
    public static final ResourceLocation TRACK_TAIL_ENTITY_LOC;
    public static final ResourceLocation FZBLoc;
    public static EntityType<FaZhenBase> FZB;
    public static final ResourceLocation FZULoc;
    public static EntityType<FaZhenUnder> FZU;
    public static final ResourceLocation JIGUANLoc;
    public static EntityType<LaserUnder> JIGUAN;
    public RBEntityRegistry() {
    }
    public static String classToString(Class<? extends Entity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
    static {
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
        DRIVEPLUSLoc = new ResourceLocation(MODID, classToString(EntityDrivePlus.class));
        TBSLoc = new ResourceLocation(MODID, classToString(TheBreakSwordPlus.class));
        SUMMONEDSWORDLoc = new ResourceLocation(MODID, classToString(SummonedSwordPlus.class));
        SUMMONEDSWORDPROLoc = new ResourceLocation(MODID, classToString(SummonSwordPROEntity.class));
        STORMSOWRDPLUSLoc = new ResourceLocation(MODID, classToString(EntityStormSwordsPlus.class));
        ShineCoreEntityLoc = new ResourceLocation(MODID, classToString(ShineCoreItemEntity.class));
        STARRY_SKY_SLASH_LOC = new ResourceLocation(MODID, classToString(StarrySkySlash.class));
        FZBLoc = new ResourceLocation(MODID, classToString(FaZhenBase.class));
        FZULoc = new ResourceLocation(MODID, classToString(FaZhenUnder.class));
        JIGUANLoc = new ResourceLocation(MODID, classToString(LaserUnder.class));
        ZLENTITYLoc = new ResourceLocation(MODID, classToString(ZhenLiEntity.class));
        FALLEN_STAR_ENTITY_LOC = new ResourceLocation(MODID, classToString(FallenStarEntity.class));
        TRACK_TAIL_ENTITY_LOC =  new ResourceLocation(MODID, classToString(TrackTailEntity.class));
    }
    public static void registerEvent(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
            {
                EntityType<TrackTailEntity> entity = TRACK_TAIL_ENTITY = EntityType.Builder
                        .of(TrackTailEntity::new, MobCategory.MISC).sized(1F, 1F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(TrackTailEntity::createInstance)
                        .build(TRACK_TAIL_ENTITY_LOC.toString());
                helper.register(TRACK_TAIL_ENTITY_LOC, entity);
            }
            {
                EntityType<FallenStarEntity> entity = FALLEN_STAR_ENTITY = EntityType.Builder
                        .of(FallenStarEntity::new, MobCategory.MISC).sized(1F, 1F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(FallenStarEntity::createInstance)
                        .build(FALLEN_STAR_ENTITY_LOC.toString());
                helper.register(FALLEN_STAR_ENTITY_LOC, entity);
            }
            {
                EntityType<StarrySkySlash> entity = STARRY_SKY_SLASH = EntityType.Builder
                        .of(StarrySkySlash::new, MobCategory.MISC).sized(1F, 1F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(StarrySkySlash::createInstance)
                        .build(STARRY_SKY_SLASH_LOC.toString());
                helper.register(STARRY_SKY_SLASH_LOC, entity);
            }
            {
                EntityType<LaserUnder> entity = JIGUAN = EntityType.Builder
                        .of(LaserUnder::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(LaserUnder::createInstance)
                        .build(JIGUANLoc.toString());
                helper.register(JIGUANLoc, entity);
            }
            {
                EntityType<ZhenLiEntity> entity = ZLENTITY = EntityType.Builder
                        .of(ZhenLiEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(ZhenLiEntity::createInstance)
                        .build(ZLENTITYLoc.toString());
                helper.register(ZLENTITYLoc, entity);
            }
            {
                EntityType<FaZhenUnder> entity = FZU = EntityType.Builder
                        .of(FaZhenUnder::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(FaZhenUnder::createInstance)
                        .build(FZULoc.toString());
                helper.register(FZULoc, entity);
            }
            {
                EntityType<FaZhenBase> entity = FZB = EntityType.Builder
                        .of(FaZhenBase::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(FaZhenBase::createInstance)
                        .build(FZBLoc.toString());
                helper.register(FZBLoc, entity);
            }
            {
                EntityType<EntityDrivePlus> entity = DRIVEPLUS = EntityType.Builder
                        .of(EntityDrivePlus::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(EntityDrivePlus::createInstance)
                        .build(DRIVEPLUSLoc.toString());
                helper.register(DRIVEPLUSLoc, entity);
            }
            {
                EntityType<TheBreakSwordPlus> entity = TBS = EntityType.Builder
                        .of(TheBreakSwordPlus::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(TheBreakSwordPlus::createInstance)
                        .build(TBSLoc.toString());
                helper.register(TBSLoc, entity);
            }
            {
                EntityType<SummonedSwordPlus> entity = SUMMONEDSWORDPLUS = EntityType.Builder
                        .of(SummonedSwordPlus::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(SummonedSwordPlus::createInstance)
                        .build(SUMMONEDSWORDLoc.toString());
                helper.register(SUMMONEDSWORDLoc, entity);
            }
                {
                    EntityType <SummonSwordPROEntity> entity = SUMMONEDSWORDPPROLUS = EntityType.Builder.of(SummonSwordPROEntity::new, MobCategory.MISC)
                            .sized(3.0F, 3.0F).setTrackingRange(4).setUpdateInterval(20)
                            .setCustomClientFactory(SummonSwordPROEntity::createInstance).build(SUMMONEDSWORDPROLoc.toString());
                    helper.register(SUMMONEDSWORDPROLoc, entity);
                }
            {
                EntityType<ShineCoreItemEntity> entity = ShineCoreEntity = EntityType.Builder
                        .of(ShineCoreItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4)
                        .setUpdateInterval(20).setCustomClientFactory(ShineCoreItemEntity::createInstanceFromPacket)
                        .build(ShineCoreEntityLoc.toString());
                helper.register(ShineCoreEntityLoc, entity);
            }
            {
                EntityType <EntityStormSwordsPlus> entity = STORMSOWRDPLUS = EntityType.Builder.of(EntityStormSwordsPlus::new, MobCategory.MISC)
                            .sized(3.0F, 3.0F).setTrackingRange(4).setUpdateInterval(20)
                            .setCustomClientFactory(EntityStormSwordsPlus::createInstance).build(STORMSOWRDPLUSLoc.toString());
                    helper.register(STORMSOWRDPLUSLoc, entity);
                }
        });
    }

}


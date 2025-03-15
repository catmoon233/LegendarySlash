package net.exmo.legendary_slash.init;

import com.google.common.base.CaseFormat;
import net.exmo.legendary_slash.entity.EntityDrivePlus;
import net.exmo.legendary_slash.entity.SummonedSwordPlus;
import net.exmo.legendary_slash.entity.SummonSwordPROEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import static net.exmo.legendary_slash.Legendary_slash.MODID;

public class LSEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES;
    public static final ResourceLocation DRIVEPLUSLoc;
    public static final ResourceLocation SUMMONEDSWORDLoc;
    public static final ResourceLocation SUMMONEDSWORDPROLoc;
    public static EntityType<EntityDrivePlus> DRIVEPLUS;
    public static EntityType<SummonedSwordPlus> SUMMONEDSWORDPLUS;
    public static EntityType<SummonSwordPROEntity> SUMMONEDSWORDPPROLUS;
    public LSEntityRegistry() {
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
        SUMMONEDSWORDLoc = new ResourceLocation(MODID, classToString(SummonedSwordPlus.class));
        SUMMONEDSWORDPROLoc = new ResourceLocation(MODID, classToString(SummonSwordPROEntity.class));
    }
    public static void registerEvent(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
            {
                EntityType<EntityDrivePlus> entity = DRIVEPLUS = EntityType.Builder
                        .of(EntityDrivePlus::new, MobCategory.MISC).sized(0.5F, 0.5F)
                        .setTrackingRange(4).setUpdateInterval(20)
                        .setCustomClientFactory(EntityDrivePlus::createInstance)
                        .build(DRIVEPLUSLoc.toString());
                helper.register(DRIVEPLUSLoc, entity);
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
        });
    }
}


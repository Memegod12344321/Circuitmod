package com.memegod.thestalker.registry;

import com.memegod.thestalker.TheStalkerMod;
import com.memegod.thestalker.entity.StalkerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TheStalkerMod.MOD_ID);

    public static final RegistryObject<EntityType<StalkerEntity>> STALKER =
            ENTITY_TYPES.register("stalker", () -> EntityType.Builder
                    .of(StalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.9F, 3.2F)
                    .clientTrackingRange(10)
                    .updateInterval(2)
                    .build("thestalker:stalker"));

    private ModEntities() {}
}

package com.memegod.aihorrorcreator.registry;

import com.memegod.aihorrorcreator.AIHorrorCreator;
import com.memegod.aihorrorcreator.entity.StalkerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AIHorrorCreator.MOD_ID);

    public static final RegistryObject<EntityType<StalkerEntity>> STALKER =
            ENTITY_TYPES.register("stalker", () -> EntityType.Builder
                    .of(StalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(AIHorrorCreator.MOD_ID + ":stalker"));

    private ModEntities() {}
}

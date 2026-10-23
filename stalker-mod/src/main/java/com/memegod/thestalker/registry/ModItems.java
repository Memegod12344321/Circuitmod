package com.memegod.thestalker.registry;

import com.memegod.thestalker.TheStalkerMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TheStalkerMod.MOD_ID);

    public static final RegistryObject<Item> STALKER_SPAWN_EGG = ITEMS.register(
            "stalker_spawn_egg",
            () -> new SpawnEggItem(ModEntities.STALKER.get(), 0x17151C, 0x8D101C, new Item.Properties()));

    private ModItems() {}
}

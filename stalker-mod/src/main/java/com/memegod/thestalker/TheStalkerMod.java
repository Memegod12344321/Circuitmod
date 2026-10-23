package com.memegod.thestalker;

import com.mojang.logging.LogUtils;
import com.memegod.thestalker.entity.StalkerEntity;
import com.memegod.thestalker.registry.ModEntities;
import com.memegod.thestalker.registry.ModItems;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TheStalkerMod.MOD_ID)
public final class TheStalkerMod {
    public static final String MOD_ID = "thestalker";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TheStalkerMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::registerAttributes);
        LOGGER.info("The Stalker is watching.");
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STALKER.get(), StalkerEntity.createAttributes().build());
    }
}

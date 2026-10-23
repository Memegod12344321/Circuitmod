package com.memegod.aihorrorcreator;

import com.memegod.aihorrorcreator.registry.ModEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AIHorrorCreator.MOD_ID)
public class AIHorrorCreator {
    public static final String MOD_ID = "aihorrorcreator";

    public AIHorrorCreator() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(modBus);
        MinecraftForge.EVENT_BUS.register(new StalkerNightEvents());
    }
}

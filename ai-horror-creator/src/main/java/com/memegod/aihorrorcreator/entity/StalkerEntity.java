package com.memegod.aihorrorcreator.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class StalkerEntity extends Zombie {
    public StalkerEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
        this.setCustomNameVisible(false);
        this.setCanPickUpLoot(false);
        this.xpReward = 8;
    }

    @Override
    public boolean isBaby() {
        return false;
    }
}

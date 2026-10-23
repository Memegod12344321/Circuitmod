package com.memegod.aihorrorcreator;

import com.memegod.aihorrorcreator.entity.StalkerEntity;
import com.memegod.aihorrorcreator.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StalkerNightEvents {
    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) {
            return;
        }
        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL
                || level.getDayTime() % 24000L < 13000L
                || level.getGameTime() % 1200L != 0L
                || level.players().isEmpty()) {
            return;
        }
        if (level.random.nextInt(100) != 0) {
            return;
        }

        Player player = level.players().get(level.random.nextInt(level.players().size()));
        int dx = level.random.nextBoolean() ? 1 : -1;
        int dz = level.random.nextBoolean() ? 1 : -1;
        int x = player.blockPosition().getX() + dx * (12 + level.random.nextInt(9));
        int z = player.blockPosition().getZ() + dz * (12 + level.random.nextInt(9));
        BlockPos surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                new BlockPos(x, player.blockPosition().getY(), z));

        if (!level.getBlockState(surface).isAir()
                || !level.getBlockState(surface.below()).blocksMotion()) {
            return;
        }

        StalkerEntity stalker = ModEntities.STALKER.get().create(level);
        if (stalker == null) {
            return;
        }
        stalker.moveTo(surface.getX() + 0.5D, surface.getY(), surface.getZ() + 0.5D,
                level.random.nextFloat() * 360.0F, 0.0F);
        level.addFreshEntity(stalker);
    }
}

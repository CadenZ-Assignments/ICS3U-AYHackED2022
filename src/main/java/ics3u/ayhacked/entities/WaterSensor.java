package ics3u.ayhacked.entities;

import com.google.common.collect.ImmutableSet;
import ics3u.ayhacked.registration.ModVillagerBrain;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;
import java.util.Set;

public class WaterSensor extends Sensor<VillagerEntity> {
    @Override
    protected void update(ServerWorld serverWorld, VillagerEntity sailorEntity) {
        Brain<?> brain = sailorEntity.getBrain();
        brain.setMemory(ModVillagerBrain.NearestWater.get(), findNearestWater(serverWorld, sailorEntity));
    }

    @Override
    public Set<MemoryModuleType<?>> getUsedMemories() {
        return ImmutableSet.of(ModVillagerBrain.NearestWater.get());
    }

    private static Optional<GlobalPos> findNearestWater(ServerWorld world, VillagerEntity sailorEntity) {
        Optional<BlockPos> pos = BlockPos.getClosestMatchingPosition(
                sailorEntity.getPosition(),
                8,
                8,
                p -> {
                    FluidState state = world.getFluidState(p);
                    return state.isSource() && state.isTagged(FluidTags.WATER);
                }
        );
        return pos.map(blockPos -> GlobalPos.getPosition(world.getDimensionKey(), blockPos));
    }
}

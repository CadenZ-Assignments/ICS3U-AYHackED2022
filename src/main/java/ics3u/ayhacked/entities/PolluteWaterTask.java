package ics3u.ayhacked.entities;

import com.google.common.collect.ImmutableMap;
import ics3u.ayhacked.registration.ModVillagerBrain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;

public class PolluteWaterTask extends Task<SailorEntity> {
    public PolluteWaterTask() {
        super(ImmutableMap.of(ModVillagerBrain.NearestWater.get(), MemoryModuleStatus.VALUE_PRESENT));
    }


}

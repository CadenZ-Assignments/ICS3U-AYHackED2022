package ics3u.ayhacked.registration;

import java.util.Optional;
import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.entities.SailorEntity;
import ics3u.ayhacked.entities.WaterSensor;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.util.math.GlobalPos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModVillagerBrain {
    private static final DeferredRegister<Activity> ACTIVITY_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ACTIVITIES, AYHackED.MODID);
    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, AYHackED.MODID);
    private static final DeferredRegister<SensorType<?>> SENSOR_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, AYHackED.MODID);

    public static final RegistryObject<Activity> PolluteWaterActivity = ACTIVITY_DEFERRED_REGISTER.register("pollute_water", () -> new Activity("pollute_water"));
    public static final RegistryObject<MemoryModuleType<GlobalPos>> NearestWater = MEMORY_MODULE_TYPE_DEFERRED_REGISTER.register("nearest_water", () -> new MemoryModuleType<>(Optional.of(GlobalPos.CODEC)));
    public static final RegistryObject<SensorType<WaterSensor>> NearestWaterSensor = SENSOR_TYPE_DEFERRED_REGISTER.register("water_sensor", () -> new SensorType<>(WaterSensor::new));

    public static void register(IEventBus modEventBus) {
        ACTIVITY_DEFERRED_REGISTER.register(modEventBus);
        MEMORY_MODULE_TYPE_DEFERRED_REGISTER.register(modEventBus);
        SENSOR_TYPE_DEFERRED_REGISTER.register(modEventBus);
    }
}

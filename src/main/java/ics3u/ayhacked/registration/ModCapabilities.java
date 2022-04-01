package ics3u.ayhacked.registration;

import ics3u.ayhacked.AYHackED;
import ics3u.ayhacked.capabilities.Thirst;
import ics3u.ayhacked.capabilities.base.CapabilityStorage;
import ics3u.ayhacked.capabilities.WaterPollution;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {
    @CapabilityInject(WaterPollution.class)
    public static Capability<WaterPollution> WATER_POLLUTION_CAPABILITY;

    @CapabilityInject(Thirst.class)
    public static Capability<Thirst> THIRST_CAPABILITY;

    public static void register() {
        AYHackED.LOGGER.info("Registering water pollution capability");
        CapabilityManager.INSTANCE.register(WaterPollution.class, new CapabilityStorage<>(), WaterPollution::new);
        CapabilityManager.INSTANCE.register(Thirst.class, new CapabilityStorage<>(), Thirst::new);
    }
}

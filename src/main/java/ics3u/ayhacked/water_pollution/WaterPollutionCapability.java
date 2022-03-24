package ics3u.ayhacked.water_pollution;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class WaterPollutionCapability {
    @CapabilityInject(WaterPollution.class)
    public static Capability<WaterPollution> WATER_POLLUTION_CAPABILITY;

    public static void register() {
        CapabilityManager.INSTANCE.register(WaterPollution.class, new Storage(), WaterPollution::new);
    }

    private static class Storage implements Capability.IStorage<WaterPollution> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<WaterPollution> capability, WaterPollution instance, Direction side) {
            return instance.write();
        }

        @Override
        public void readNBT(Capability<WaterPollution> capability, WaterPollution instance, Direction side, INBT nbt) {
            instance.read((CompoundNBT) nbt);
        }
    }
}

package ics3u.ayhacked.water_pollution;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterPollutionCapProv implements ICapabilitySerializable<CompoundNBT> {
    private final WaterPollution pollution = new WaterPollution();
    private final LazyOptional<WaterPollution> lazyOptional = LazyOptional.of(() -> pollution);

    public void invalidate() {
        lazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == WaterPollutionCapability.WATER_POLLUTION_CAPABILITY) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (WaterPollutionCapability.WATER_POLLUTION_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) WaterPollutionCapability.WATER_POLLUTION_CAPABILITY.writeNBT(pollution, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (WaterPollutionCapability.WATER_POLLUTION_CAPABILITY != null) {
            WaterPollutionCapability.WATER_POLLUTION_CAPABILITY.readNBT(pollution, null, nbt);
        }
    }
}

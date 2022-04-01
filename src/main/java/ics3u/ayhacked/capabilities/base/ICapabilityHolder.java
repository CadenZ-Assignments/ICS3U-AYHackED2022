package ics3u.ayhacked.capabilities.base;

import net.minecraft.nbt.CompoundNBT;

public interface ICapabilityHolder {
    CompoundNBT write();
    void read(CompoundNBT nbt);
}

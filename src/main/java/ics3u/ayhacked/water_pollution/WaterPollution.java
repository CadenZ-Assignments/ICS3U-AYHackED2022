package ics3u.ayhacked.water_pollution;

import net.minecraft.nbt.CompoundNBT;

public class WaterPollution {
    private long pollutionAmount;

    public WaterPollution(long pollutionAmount) {
        this.pollutionAmount = pollutionAmount;
    }

    public WaterPollution() {
        this(0);
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("waterPollution", pollutionAmount);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        pollutionAmount = nbt.getInt("waterPollution");
    }

    public void addPollution(long amount) {
        pollutionAmount += amount;
    }

    public void removePollution(long amount) {
        pollutionAmount -= amount;
    }

    public long getPollutionAmount() {
        return pollutionAmount;
    }
}

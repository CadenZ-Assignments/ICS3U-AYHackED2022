package ics3u.ayhacked.capabilities;

import ics3u.ayhacked.capabilities.base.ICapabilityHolder;
import net.minecraft.nbt.CompoundNBT;

public class Thirst implements ICapabilityHolder {
    private int thirst;

    public Thirst(int thirst) {
        this.thirst = thirst;
    }

    public Thirst() {
        this(10);
    }

    @Override
    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("thirst", thirst);
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {
        thirst = nbt.getInt("thirst");
    }

    public void setThirst(int amount) {
        this.thirst = amount;
    }

    public void addThirst(int amount) {
        if (thirst + amount > 10) {
            thirst = 10;
            return;
        }
        thirst += amount;
    }

    public void removeThirst(int amount) {
        if (thirst - amount < 0) {
            thirst = 0;
            return;
        }
        thirst -= amount;
    }

    public int getThirst() {
        return thirst;
    }
}

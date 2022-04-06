package ics3u.ayhacked.te;

import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.chunk.Chunk;

public abstract class AbstractChunkModifierTE extends TileEntity implements ITickableTileEntity {

    protected static final int maxProgress = 100;
    protected int progress = 0;

    public AbstractChunkModifierTE(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world == null) return;
        if (world.isRemote()) return;
        ((Chunk) world.getChunk(getPos())).getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
            if (progress == 0) {
                progress = maxProgress;
            }

            if (progress > 0) {
                progress--;

                if (progress <= 0) {
                    modify(cap);
                    progress = 0;
                }
            }
        });
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        progress = nbt.getInt("progress");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("progress", progress);
        return super.write(compound);
    }

    protected abstract void modify(WaterPollution cap);
}

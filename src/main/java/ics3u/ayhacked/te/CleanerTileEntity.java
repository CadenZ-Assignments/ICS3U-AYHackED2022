package ics3u.ayhacked.te;

import ics3u.ayhacked.water_pollution.WaterPollutionCapability;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.ItemStackHandler;

public class CleanerTileEntity extends TileEntity implements ITickableTileEntity {

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1);
    private static final int maxProgress = 100;

    private int progress = 0;

    public CleanerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (world == null) return;
        if (world.isRemote()) return;
        if (itemStackHandler.getStackInSlot(0).isEmpty()) {
            progress = 0;
            return;
        }
        ((Chunk) world.getChunk(getPos())).getCapability(WaterPollutionCapability.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
            if (progress == 0) {
                progress = maxProgress;
            }

            if (progress > 0) {
                progress--;

                if (progress <= 0) {
                    cap.removePollution(100);
                    itemStackHandler.extractItem(0, 1, false);
                    progress = 0;
                }
            }
        });
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT((CompoundNBT) nbt.get("items"));
        progress = nbt.getInt("progress");
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("items", itemStackHandler.serializeNBT());
        compound.putInt("progress", progress);
        return super.write(compound);
    }
}

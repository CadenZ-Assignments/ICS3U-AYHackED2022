package ics3u.ayhacked.te;

import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.registration.ModBlocks;
import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.ItemStackHandler;

public class CleanerTileEntity extends AbstractChunkModifierTE {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1);

    public CleanerTileEntity() {
        super(ModBlocks.CLEANER_TE.get());
    }

    @Override
    public void tick() {
        if (world == null) return;
        if (world.isRemote()) return;
        if (itemStackHandler.getStackInSlot(0).isEmpty()) {
            progress = 0;
            return;
        }
        ((Chunk) world.getChunk(getPos())).getCapability(ModCapabilities.WATER_POLLUTION_CAPABILITY).ifPresent(cap -> {
            if (progress == 0) {
                progress = maxProgress;
            }

            if (progress > 0) {
                progress--;

                if (progress <= 0) {
                    modify(cap);
                    itemStackHandler.extractItem(0, 1, false);
                    progress = 0;
                }
            }
        });
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT((CompoundNBT) nbt.get("items"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("items", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    @Override
    protected void modify(WaterPollution cap) {
        cap.removePollution(100);
    }
}

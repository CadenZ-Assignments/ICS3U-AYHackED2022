package ics3u.ayhacked.te;

import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.registration.ModBlocks;
import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.ItemStackHandler;

public class CleanerTileEntity extends AbstractChunkModifierTE {

    public CleanerTileEntity() {
        super(ModBlocks.CLEANER_TE.get());
    }

    @Override
    protected void modify(WaterPollution cap) {
        cap.removePollution(100);
    }
}

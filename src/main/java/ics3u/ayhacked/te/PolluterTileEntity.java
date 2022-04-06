package ics3u.ayhacked.te;

import ics3u.ayhacked.capabilities.WaterPollution;
import ics3u.ayhacked.registration.ModBlocks;

public class PolluterTileEntity extends AbstractChunkModifierTE {
    public PolluterTileEntity() {
        super(ModBlocks.POLLUTER_TE.get());
    }

    @Override
    protected void modify(WaterPollution cap) {
        cap.addPollution(20);
    }
}

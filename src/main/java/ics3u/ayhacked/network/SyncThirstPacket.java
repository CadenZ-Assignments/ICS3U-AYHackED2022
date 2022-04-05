package ics3u.ayhacked.network;

import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncThirstPacket {
    private final int thirst;

    public SyncThirstPacket(int thirst) {
        this.thirst = thirst;
    }

    public SyncThirstPacket(PacketBuffer buffer) {
        thirst = buffer.readInt();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(thirst);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity clientPlayer = Minecraft.getInstance().player;

        if (clientPlayer != null) {
            clientPlayer.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> cap.setThirst(thirst));
        }

        ctx.get().setPacketHandled(true);
    }
}

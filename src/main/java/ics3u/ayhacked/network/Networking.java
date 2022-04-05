package ics3u.ayhacked.network;

import ics3u.ayhacked.AYHackED;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        AYHackED.LOGGER.info("Registering AYHackED networkings");

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(AYHackED.MODID, "ayhacked_networking"),
                () -> "1.0",
                s -> true,
                s -> true);
        INSTANCE.messageBuilder(SyncThirstPacket.class, nextID())
                .encoder(SyncThirstPacket::encode)
                .decoder(SyncThirstPacket::new)
                .consumer(SyncThirstPacket::handle)
                .add();

    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}

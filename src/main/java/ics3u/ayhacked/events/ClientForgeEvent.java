package ics3u.ayhacked.events;

import ics3u.ayhacked.client.ThirstHud;
import ics3u.ayhacked.registration.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientForgeEvent {

    @SubscribeEvent
    public static void renderHud(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            // get client side player
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;

            player.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> {
                ThirstHud.INSTANCE.render(event.getMatrixStack());
            });
        }
    }
}

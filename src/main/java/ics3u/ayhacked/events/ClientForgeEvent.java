package ics3u.ayhacked.events;

import com.mojang.blaze3d.matrix.MatrixStack;
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
//        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            render(event.getMatrixStack(), 0);
//        } else if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
//            render(event.getMatrixStack(), -15);
//        }
    }

    private static void render(MatrixStack matrixStack, int yOffset) {
        // get client side player
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.isCreative()) return;
        player.getCapability(ModCapabilities.THIRST_CAPABILITY).ifPresent(cap -> {
            ThirstHud.INSTANCE.init(cap.getThirst());
            // make it higher up if air bubbles that shows up when player is in water is rendering
            ThirstHud.INSTANCE.render(matrixStack, yOffset);
        });
    }
}

package ics3u.ayhacked.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import ics3u.ayhacked.AYHackED;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class ThirstHud extends AbstractGui {
    public static final ThirstHud INSTANCE = new ThirstHud();
    private static final ResourceLocation DROPLET = new ResourceLocation(AYHackED.MODID, "textures/gui/droplet.png");

    private int thirst;

    public void init(int thirst) {
        this.thirst = thirst;
    }

    public void render(MatrixStack mStack, int yOffset) {
        Minecraft mc = Minecraft.getInstance();

        mc.textureManager.bindTexture(DROPLET);

        int width = mc.getMainWindow().getScaledWidth();
        int height = mc.getMainWindow().getScaledHeight();

        int left = width / 2 + 91;
        int top = height - ForgeIngameGui.right_height - 15;

        for (int i = 0; i < 10; ++i)
        {
            int x = left - i * 8 - 12;
            blit(mStack, x, top + yOffset, getBlitOffset(), 0, i < thirst ? 0 : 16, 16, 16, 32, 16);
        }
    }
}

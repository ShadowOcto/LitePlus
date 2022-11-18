package me.rhys.base.util.hook;

import me.rhys.base.Lite;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RenderHook extends GuiIngame {

    public RenderHook(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);
        GlStateManager.pushMatrix();

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        Lite.EVENT_BUS.call(new RenderGameOverlayEvent(partialTicks, resolution.getScaledWidth(), resolution.getScaledHeight()));

        GlStateManager.popMatrix();
    }

}
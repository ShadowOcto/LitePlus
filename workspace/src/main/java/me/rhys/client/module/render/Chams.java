package me.rhys.client.module.render;

import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.render.RenderEntityEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created on 07/09/2020 Package me.rhys.client.module.render
 */
public class Chams extends Module {
    public Chams(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @EventTarget
    public void onRender(RenderEntityEvent event) {
        if (event.getEntity().isEntityEqual(player())
                || !(event.getEntity() instanceof EntityPlayer)) return;

        HUD hud = (HUD) Lite.MODULE_FACTORY.findByClass(HUD.class);

        if (event.getType() == Event.Type.PRE) {

            if (!event.isLayers()) {
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 772);
                float[] color = RenderUtil.getColors((hud.colorMode == HUD.ColorMode.RAINBOW
                        ? Color.getHSBColor(((Minecraft.getSystemTime() + (10 * Minecraft.getMinecraft().thePlayer.ticksExisted)) % 5000F) / 5000F,
                        (float) 1, (float) 0.20).getRGB() : new Color(52, 189, 235).getRGB()));

                if (mc.thePlayer.canEntityBeSeen(event.getEntity())) {
                    if (hud.colorMode != HUD.ColorMode.RAINBOW) {
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    }
                    GlStateManager.color(color[0], color[1], color[2]);
                } else {
                    GlStateManager.color(1F, 0F, 0F, 1);
                }
            }

            GlStateManager.doPolygonOffset(event.isLayers() ? -2000000000.0F : -20000000.0F, -3.0F);
            GlStateManager.enablePolygonOffset();

        } else {
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();

            if (!event.isLayers()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }
}

package me.rhys.base.util.render;

import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

public class RenderUtil {

    private RenderUtil() {
    }


    public static boolean renderAltSkin(String name, int x, int y, int size) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, size, size, 64, 64);
        } catch (Exception e) {
            return false;
        }
        return true;
    }



    public static boolean renderAltSkin(EntityPlayer player, Vec2f pos, int size) {
        if (((AbstractClientPlayer) player).getLocationSkin() == null)
            return false;
        try {
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(((AbstractClientPlayer) player).getLocationSkin());
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) pos.getX(), (int) pos.getY(), 8, 8, 8, 8, size, size, 64, 64);
            GL11.glPopMatrix();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean renderAltSkin(EntityPlayer player, float x, float y, int size) {
        if (((AbstractClientPlayer) player).getLocationSkin() == null)
            return false;
        try {
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(((AbstractClientPlayer) player).getLocationSkin());
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8, 8, 8, 8, size, size, 64, 64);
            GL11.glPopMatrix();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean renderAltSkin(ResourceLocation location, Vec2f pos, int size) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) pos.x, (int) pos.y, 8, 8, 8, 8, size, size, 64, 64);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean renderAltSkin(Vec2f pos, int size) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkin(new UUID(0, 10)));
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) pos.getX(), (int) pos.getY(), 8, 8, 8, 8, size, size, 64, 64);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean renderAltSkin(String name, Vec2f pos, int size, UUID uuid) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(DefaultPlayerSkin.getDefaultSkin(uuid));
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect((int) pos.getX(), (int) pos.getY(), 8, 8, 8, 8, size, size, 64, 64);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static void drawOutlineRect(Vec2f start, Vec2f end, int color) {
        GlStateManager.pushMatrix();
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(start.x, end.y, 0.0D).endVertex();
        worldrenderer.pos(end.x, end.y, 0.0D).endVertex();
        worldrenderer.pos(end.x, start.y, 0.0D).endVertex();
        worldrenderer.pos(start.x, start.y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBorderedRectangle(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float insideAlpha = (float) (insideColor >> 24 & 255) / 255.0F;
        float insideRed = (float) (insideColor >> 16 & 255) / 255.0F;
        float insideGreen = (float) (insideColor >> 8 & 255) / 255.0F;
        float insieBlue = (float) (insideColor & 255) / 255.0F;

        float borderAlpha = (float) (borderColor >> 24 & 255) / 255.0F;
        float borderRed = (float) (borderColor >> 16 & 255) / 255.0F;
        float borderGreen = (float) (borderColor >> 8 & 255) / 255.0F;
        float borderBlue = (float) (borderColor & 255) / 255.0F;

        if (insideAlpha > 0.0f) {
            GlStateManager.color(insideRed, insideGreen, insieBlue, insideAlpha);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(left, bottom, 0.0D).endVertex();
            worldrenderer.pos(right, bottom, 0.0D).endVertex();
            worldrenderer.pos(right, top, 0.0D).endVertex();
            worldrenderer.pos(left, top, 0.0D).endVertex();
            tessellator.draw();
        }

        if (borderAlpha > 0.0f) {
            GlStateManager.color(borderRed, borderGreen, borderBlue, borderAlpha);

            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(left - (borderIncludedInBounds ? 0 : borderWidth), bottom, 0.0D).endVertex();
            worldrenderer.pos(left - (borderIncludedInBounds ? borderWidth : 0), bottom, 0.0D).endVertex();
            worldrenderer.pos(left - (borderIncludedInBounds ? borderWidth : 0), top, 0.0D).endVertex();
            worldrenderer.pos(left - (borderIncludedInBounds ? 0 : borderWidth), top, 0.0D).endVertex();
            tessellator.draw();

            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(left, top - (borderIncludedInBounds ? borderWidth : 0), 0.0D).endVertex();
            worldrenderer.pos(right, top - (borderIncludedInBounds ? borderWidth : 0), 0.0D).endVertex();
            worldrenderer.pos(right, top - (borderIncludedInBounds ? 0 : borderWidth), 0.0D).endVertex();
            worldrenderer.pos(left, top - (borderIncludedInBounds ? 0 : borderWidth), 0.0D).endVertex();
            tessellator.draw();

            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(right + (borderIncludedInBounds ? borderWidth : 0), bottom, 0.0D).endVertex();
            worldrenderer.pos(right + (borderIncludedInBounds ? 0 : borderWidth), bottom, 0.0D).endVertex();
            worldrenderer.pos(right + (borderIncludedInBounds ? 0 : borderWidth), top, 0.0D).endVertex();
            worldrenderer.pos(right + (borderIncludedInBounds ? borderWidth : 0), top, 0.0D).endVertex();
            tessellator.draw();

            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(left, bottom - +(borderIncludedInBounds ? 0 : borderWidth), 0.0D).endVertex();
            worldrenderer.pos(right, bottom + (borderIncludedInBounds ? 0 : borderWidth), 0.0D).endVertex();
            worldrenderer.pos(right, bottom + (borderIncludedInBounds ? borderWidth : 0), 0.0D).endVertex();
            worldrenderer.pos(left, bottom + (borderIncludedInBounds ? borderWidth : 0), 0.0D).endVertex();
            tessellator.draw();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawRect(Vec2f pos, Vec2f end, int color) {
        Gui.drawRect((int) pos.getX(), (int) pos.getY(), (int) end.getX(), (int) end.getY(), color);
    }

    public static void drawRect(float x, float y, int width, int height, int color) {
        Gui.drawRect((int) x, (int) y, (int) x + width, (int) y + height, color);
    }

    public static void drawRect(Vec2f pos, int width, int height, int color) {
        drawRect(pos.x, pos.y, width, height, color);
    }

    public static double rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 13.0);
        rainbowState %= 360;
        return rainbowState;
    }

    public static void color(int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static float[] getColors(int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        return new float[]{red, green, blue, alpha};
    }

    public static void drawImage(ResourceLocation location, float x, float y, int width, int height) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableTexture2D();

        GlStateManager.color(1, 1, 1, 1);

        Minecraft.getMinecraft().getTextureManager().bindTexture(location);

        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, width, height, width, height);

        GlStateManager.popMatrix();
    }

    public static void drawImage(ResourceLocation location, Vec2f pos, int width, int height) {
        drawImage(location, pos.x, pos.y, width, height);
    }

    public static void clipRect(Vec2f pos, Vec2f endPos, int scale) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft(), scale);
        int factor = resolution.getScaleFactor();
        GL11.glScissor((int) (pos.x * factor), (int) ((resolution.getScaledHeight() - endPos.y) * factor), (int) ((endPos.x - pos.x) * factor), (int) ((endPos.y - pos.y) * factor));
    }

}

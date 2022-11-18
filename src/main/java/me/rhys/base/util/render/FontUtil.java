package me.rhys.base.util.render;

import me.rhys.base.Lite;
import me.rhys.base.font.Fonts;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.Objects;

public class FontUtil {

    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    private FontUtil() {
    }

    public static void drawString(String str, float x, float y, int color) {
        if (getType() == HUD.Fonts.MINECRAFT) {
            fontRenderer.drawString(str, x, y, color);
        } else {
            typeToFont().drawString(str, new Vec2f(x, y), color);
        }
    }

    public static void drawString(String str, Vec2f pos, int color) {
        drawString(str, pos.x, pos.y, color);
    }

    public static void drawCenteredString(String str, float x, float y, int color) {
        drawString(str, x - getStringWidth(str) / 2.0f, y - getFontHeight() / 2.0f, color);
    }

    public static void drawCenteredString(String str, Vec2f pos, int color) {
        drawCenteredString(str, pos.x, pos.y, color);
    }

    public static void drawStringWithShadow(String str, float x, float y, int color) {
        if (getType() == HUD.Fonts.MINECRAFT) {
            fontRenderer.drawStringWithShadow(str, x, y, color);
        } else {
            typeToFont().drawStringWithShadow(str, x, y, color);
        }
    }

    public static void drawStringWithShadow(String str, Vec2f pos, int color) {
        drawStringWithShadow(str, pos.x, pos.y, color);
    }

    public static void drawCenteredStringWithShadow(String str, float x, float y, int color) {
        drawStringWithShadow(str, x - getStringWidth(str) / 2.0f, y - getFontHeight() / 2.0f, color);
    }

    public static void drawCenteredStringWithShadow(String str, Vec2f pos, int color) {
        drawCenteredStringWithShadow(str, pos.x, pos.y, color);
    }

    public static float getStringWidth(String str) {
        if (getType() == HUD.Fonts.MINECRAFT) {
            return fontRenderer.getStringWidth(str);
        } else {
            return typeToFont().getStringWidth(str);
        }
    }

    public static float getFontHeight() {
        if (getType() == HUD.Fonts.MINECRAFT) {
            return fontRenderer.FONT_HEIGHT - 3;
        } else {
            return typeToFont().getHeight();
        }
    }

    public static me.rhys.base.font.FontRenderer typeToFont() {
        switch (getType()) {
            //TODO: add more..

            case APPLE: {
                return Fonts.INSTANCE.getApple();
            }
        }

        return Fonts.INSTANCE.getApple();
    }

    private static HUD.Fonts getType() {
        return ((HUD) Lite.MODULE_FACTORY.findByClass(HUD.class)).font;
    }
}

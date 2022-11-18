package me.rhys.base.util.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rhys.base.util.vec.Vec4f;

import java.awt.*;

public class ColorUtil {

    public static Color darken(Color color, int percent) {
        return darken(color.getRed(), color.getGreen(), color.getBlue(), percent);
    }

    public static Color darken(int color, int percent) {
        int[] values = getColorsI(color);
        return darken(values[0], values[1], values[2], percent);
    }

    public static Vec4f getColor(int color) {
        return new Vec4f(
                (color >> 16 & 255) / 255.0f,
                (color >> 8 & 255) / 255.0f,
                (color & 255) / 255.0f,
                (color >> 24 & 255) / 255.0f
        );
    }

    public static int getColor(Vec4f color) {
        return new java.awt.Color(color.x, color.y, color.z, color.w).getRGB();
    }

    public static int getColor(int r, int g, int b) {
        return new Color(r, g, b, (255)).getRGB();
    }

    public static Color getHealthColor(float h) {
        if (h >= 15 && h <= 20) {
            return new Color(28, 255, 0);
        } else if (h >= 10 && h < 20) {
            return new Color(255, 254, 19);
        } else if (h > 0 && h <= 10) {
            return new Color(255, 0, 26);
        }

        return new Color(221, 24, 34);
    }

    public static Color darken(int r, int g, int b, int percent) {
        return new Color((int) Math.max(0, Math.min(255, (r - (r * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (g - (g * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (b - (b * (percent / 100.0f))))));
    }

    public static Color lighten(int r, int g, int b, int percent) {
        return new Color((int) Math.max(0, Math.min(255, (r + (r * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (g + (g * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (b + (b * (percent / 100.0f))))));
    }

    public static Color lighten(int color, int percent) {
        int[] values = getColorsI(color);
        return lighten(values[0], values[1], values[2], percent);
    }

    public static Color lighten(Color color, int percent) {
        return lighten(color.getRed(), color.getGreen(), color.getBlue(), percent);
    }

    public static int rgba(int r, int g, int b, float alpha) {
        return new Color(r, g, b, (int) (alpha * 255.0f)).getRGB();
    }

    public static int[] getColorsI(int color) {
        return new int[]
                {
                        color >> 16 & 255,
                        color >> 8 & 255,
                        color & 255,
                        color >> 24 & 255
                };
    }


    public static boolean isTransparent(int color) {
        return (float) (color >> 24 & 255) / 255.0F == 0.0f;
    }

    @AllArgsConstructor
    public enum Colors {

        TRANSPARENT(rgba(0, 0, 0, 0.0f)),
        BLACK_TRANSPARENT(rgba(0, 0, 0, 0.6f)),
        DARK_GRAY(rgba(50, 50, 50, 1.0f));

        @Getter
        int color;

    }

}

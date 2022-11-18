package me.rhys.base.font;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public enum Fonts {
    INSTANCE;

    @Getter
    private FontRenderer apple, bigapple;

    @Getter
    private net.minecraft.client.gui.FontRenderer bit;

    public void setup() {
        apple = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/apple.ttf"),
                18, Font.PLAIN), true, true);
        bigapple = new FontRenderer(fontFromTTF(new ResourceLocation("Lite/fonts/applebold.ttf"),
                22, Font.PLAIN), true, true);
        loadBitFont();
    }

    public void loadBitFont() {
        bit = new net.minecraft.client.gui.FontRenderer(Minecraft.getMinecraft().gameSettings,
                new ResourceLocation("Lite/fonts/Bit.png"),
                Minecraft.getMinecraft().renderEngine, false);

        if (Minecraft.getMinecraft().gameSettings.language != null) {
            bit.setUnicodeFlag(Minecraft.getMinecraft().isUnicode());
            bit.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
        }

        Minecraft.getMinecraft().mcResourceManager.registerReloadListener(bit);
    }

    public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
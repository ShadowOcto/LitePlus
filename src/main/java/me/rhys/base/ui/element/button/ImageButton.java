package me.rhys.base.ui.element.button;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class ImageButton extends Button {

    private final ResourceLocation image;

    private final int imgWidth;
    private final int imgHeight;

    @Getter
    @Setter
    private float scale;

    private Color hoverColor;

    public ImageButton(ResourceLocation image, Vec2f offset, int width, int height, int imgWidth, int imgHeight) {
        super("", offset, width, height);
        this.image = image;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.scale = 1.225f;
    }

    public ImageButton(ResourceLocation image, Vec2f offset, int width, int height, int imgWidth, int imgHeight, Color color) {
        super("", offset, width, height);
        this.image = image;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.scale = 1.225f;
        this.hoverColor = color;
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        // draw the background
        if (background == ColorUtil.Colors.TRANSPARENT.getColor()) {
            RenderUtil.drawRect(pos, width, height, screen.theme.buttonColors.background);
        }


        GlStateManager.pushMatrix();

        GlStateManager.scale(scale, scale, scale);

        RenderUtil.drawImage(image, pos.clone().add((width - imgWidth) / 2.0f, (height - imgHeight) / 2.0f).div(scale, scale), imgWidth, imgHeight);

        GlStateManager.popMatrix();
    }

}

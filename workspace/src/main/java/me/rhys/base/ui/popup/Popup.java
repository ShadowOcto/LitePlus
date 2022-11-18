package me.rhys.base.ui.popup;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Popup extends Panel {

    protected static final int BACKGROUND_COLOR = new Color(27, 34, 44, 255).getRGB();
    protected static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 15).getRGB();

    private final String title;
    private Panel panel;

    public Popup(String title, int width, int height) {
        super(new Vec2f(), width, height);

        this.title = title;
        this.background = BACKGROUND_COLOR;
    }

    public void addToBody(Element element) {
        panel.add(element);
    }

    public void onShow() {
        getContainer().getItems().clear();

        panel = new Panel(new Vec2f(1, FontUtil.getFontHeight() + 11), width - 2, (int) (height - (FontUtil.getFontHeight() + 12)));

        add(panel);
    }

    public void onHide() {

    }

    public void onDraw() {

    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        GlStateManager.pushMatrix();

        GL11.glLineWidth(4f);

        RenderUtil.drawOutlineRect(pos, pos.clone().add(width, height), SHADOW_COLOR);

        GL11.glLineWidth(1f);

        GlStateManager.popMatrix();

        int headerHeight = (int) (FontUtil.getFontHeight() + 10);

        RenderUtil.drawRect(pos.clone().add(1, 1), width - 2, headerHeight, ColorUtil.darken(BACKGROUND_COLOR, 5).getRGB());

        RenderUtil.drawRect(pos.clone().add(0, headerHeight), width, 1, SHADOW_COLOR);

        GlStateManager.pushMatrix();

        float scale = 0.8f;

        GlStateManager.scale(scale, scale, scale);

        FontUtil.drawCenteredString(title, pos.clone().add(width / 2.0f, headerHeight / 2.0f).div(scale, scale), -1);

        GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);

        GlStateManager.popMatrix();

        super.draw(mouse, partialTicks);

        onDraw();
    }

}

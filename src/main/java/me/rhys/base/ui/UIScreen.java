package me.rhys.base.ui;

import lombok.Getter;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.ui.theme.Theme;
import me.rhys.base.ui.theme.impl.DarkTheme;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class UIScreen extends GuiScreen implements UIElement {

    protected final Panel panel = new Panel(new Vec2f(), 0, 0);
    protected final MovementProcessor movementProcessor = new MovementProcessor();

    public final Theme theme = new DarkTheme();

    @Getter
    private Popup popup = null;

    public void add(Element element) {
        panel.add(element);
    }

    public void remove(Element element) {
        panel.remove(element);
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void initGui() {
        // set the moving surface of the movement processor
        movementProcessor.setParent(panel);

        if (panel.getContainer().isEmpty()) {
            // init all the elements
            init();
        }
    }

    protected void init() {
    }

    protected void preDraw(Vec2f mouse, float partialTicks) {
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Vec2f mouse = getMousePosition();

        if (popup != null) {
            popup.clickMouse(mouse, mouseButton);
        } else {
            // update the processor positions
            movementProcessor.clickMouse(mouse, mouseButton);

            panel.clickMouse(mouse, mouseButton);
            clickMouse(mouse, mouseButton);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Vec2f mouse = getMousePosition();

        if (popup != null) {
            popup.dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
        } else {
            panel.dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
            dragMouse(mouse, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Vec2f mouse = getMousePosition();

        if (popup != null) {
            popup.releaseMouse(mouse, state);
        } else {
            // release all the elements
            movementProcessor.releaseMouse(mouse, state);

            panel.releaseMouse(mouse, state);
            releaseMouse(mouse, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (popup != null) {
            if (keyCode == 1) {
                displayPopup(null);
            } else {
                popup.typeKey(typedChar, keyCode);
            }
        } else {
            if (keyCode == 1) {
                mc.displayGuiScreen(null);

                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }

            panel.typeKey(typedChar, keyCode);
            typeKey(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();

        ScaledResolution resolution;

        mc.entityRenderer.setupOverlayRendering(resolution = new ScaledResolution(mc, getScale()));

        width = resolution.getScaledWidth();
        height = resolution.getScaledHeight();

        panel.setParent(null);
        panel.setScreen(this);

        Vec2f mouse = getMousePosition();

        // update the positions from the processor
        movementProcessor.updatePositions(mouse);

        // pre draw the screen if there is anything
        preDraw(mouse, partialTicks);

        // back end draw
        panel._draw(mouse, partialTicks);

        // front end draw
        draw(mouse, partialTicks);

        if (popup != null) {
            // always center the popup to the middle of the screen
            popup.offset = new Vec2f((resolution.getScaledWidth() - popup.getWidth()) / 2.0f, (resolution.getScaledHeight() - popup.getHeight()) / 2.0f);

            popup._draw(mouse, partialTicks);
        }

        mc.entityRenderer.setupOverlayRendering();

        GlStateManager.popMatrix();
    }

    public void displayPopup(Popup popup) {
        if (this.popup != null) {
            this.popup.onHide();
        }

        this.popup = popup;

        if (this.popup != null) {
            this.popup.setScreen(this);
            this.popup.onShow();
        }
    }

    protected Vec2f getMousePosition() {
        ScaledResolution resolution = getResolution();
        return new Vec2f(Mouse.getX() * resolution.getScaledWidth() / (float) mc.displayWidth, resolution.getScaledHeight() - Mouse.getY() * resolution.getScaledHeight() / (float) mc.displayHeight - 1);
    }

    public ScaledResolution getResolution() {
        return new ScaledResolution(mc, getScale());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public int getScale() {
        return 2;
    }

}

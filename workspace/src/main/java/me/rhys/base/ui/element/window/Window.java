package me.rhys.base.ui.element.window;

import lombok.Getter;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

@Getter
public class Window extends Element {

    private final Panel panel;

    public Window(Vec2f offset, int width, int height) {
        super(offset, width, height);
        // create the panel
        this.panel = new Panel(new Vec2f(0, 0), width, height);
        this.panel.setParent(this);

        // make it movable
        this.movable = true;
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        panel.clickMouse(pos, button);
    }

    @Override
    public void dragMouse(Vec2f pos, int button, long lastClickTime) {
        panel.dragMouse(pos, button, lastClickTime);
    }

    @Override
    public void releaseMouse(Vec2f pos, int button) {
        panel.releaseMouse(pos, button);
    }

    @Override
    public void typeKey(char keyChar, int keyCode) {
        panel.typeKey(keyChar, keyCode);
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        // draw the border
        RenderUtil.drawRect(pos, width, height, getScreen().theme.windowColors.border);

        // draw the background
        RenderUtil.drawRect(pos.clone().add(1, 1), width - 2, height - 2, getScreen().theme.windowColors.background);

        // draw all the panel elements
        panel._draw(mouse, partialTicks);
    }

}

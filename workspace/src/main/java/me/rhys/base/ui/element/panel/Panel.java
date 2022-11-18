package me.rhys.base.ui.element.panel;

import lombok.Getter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.container.Container;
import me.rhys.base.util.vec.Vec2f;

public class Panel extends Element {

    @Getter
    protected final Container<Element> container = new Container<>();

    public Panel(Vec2f offset, int width, int height) {
        super(offset, width, height);
    }

    public void add(Element element) {
        element.setParent(this);
        element.setScreen(getScreen());

        container.add(element);
    }

    public void remove(Element element) {
        container.remove(element);
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        container.filter(element -> element.isHovered(pos)).forEach(element -> element.clickMouse(pos, button));
    }

    @Override
    public void dragMouse(Vec2f pos, int button, long lastClickTime) {
        container.filter(element -> element.isHovered(pos)).forEach(element -> element.dragMouse(pos, button, lastClickTime));
    }

    @Override
    public void releaseMouse(Vec2f pos, int button) {
        container.filter(element -> element.isHovered(pos)).forEach(element -> element.releaseMouse(pos, button));
    }

    @Override
    public void typeKey(char keyChar, int keyCode) {
        container.forEach(element -> element.typeKey(keyChar, keyCode));
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        container.forEach(element -> element._draw(mouse, partialTicks));
    }

    @Override
    public void setScreen(UIScreen screen) {
        container.forEach(element -> element.setScreen(screen));
        super.setScreen(screen);
    }

}

package me.rhys.base.ui;

import me.rhys.base.util.vec.Vec2f;

public interface UIElement {

    default void clickMouse(Vec2f pos, int button) {

    }

    default void dragMouse(Vec2f pos, int button, long lastClickTime) {

    }

    default void releaseMouse(Vec2f pos, int button) {

    }

    default void typeKey(char keyChar, int keyCode) {

    }

    default void draw(Vec2f mouse, float partialTicks) {

    }

}

package me.rhys.base.ui;

import lombok.Setter;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.container.MapContainer;
import me.rhys.base.util.vec.Vec2f;

public class MovementProcessor extends MapContainer<Element, MovementProcessor.Entry> {

    @Setter
    private Panel parent;

    public void clickMouse(Vec2f pos, int button) {
        if (button == 0) {
            parent.getContainer().filter(element -> element.movable && (element.isHovered(pos) && (element.callback != null && element.callback.handle(pos)))).forEach(element -> {
                getMap().putIfAbsent(element, new Entry());

                Entry entry = get(element);
                entry.mouseDown = true;
                entry.lockPos = pos.clone().sub(element.offset.x, element.offset.y);
            });
        }
    }

    public void releaseMouse(Vec2f pos, int button) {
        if (button == 0) {
            parent.getContainer().filter(element -> element.movable).forEach(element -> {
                getMap().putIfAbsent(element, new Entry());

                get(element).mouseDown = false;
            });
        }
    }


    public void updatePositions(Vec2f mouse) {
        parent.getContainer().filter(element -> element.movable).forEach(element -> {
            getMap().putIfAbsent(element, new Entry());

            Entry entry = get(element);
            if (entry.mouseDown) {
                element.offset = mouse.clone().sub(entry.lockPos.x, entry.lockPos.y);
            }
        });
    }

    public static class Entry {

        boolean mouseDown;
        Vec2f lockPos;

        Entry() {
            mouseDown = false;
            lockPos = new Vec2f();
        }

    }

}

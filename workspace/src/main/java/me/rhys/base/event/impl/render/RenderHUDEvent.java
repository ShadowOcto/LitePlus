package me.rhys.base.event.impl.render;


import me.rhys.base.event.Event;

public class RenderHUDEvent extends Event {

    private int width, height;
    private float partialTicks;

    public RenderHUDEvent(int width, int height, float partialTicks) {
        this.width = width;
        this.height = height;
        this.partialTicks = partialTicks;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

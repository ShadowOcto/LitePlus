package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;

public class Render3DEvent extends Event {

    private float partialTicks;

    public Render3DEvent(float partialTicks) {
        super();
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

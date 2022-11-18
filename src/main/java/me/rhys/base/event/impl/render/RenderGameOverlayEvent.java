package me.rhys.base.event.impl.render;

import lombok.Getter;

@Getter
public class RenderGameOverlayEvent extends RenderEvent {

    private int width;
    private int height;

    public RenderGameOverlayEvent(float partialTicks, int width, int height) {
        super(partialTicks);
        this.width = width;
        this.height = height;
    }

}

package me.rhys.base.event.impl.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rhys.base.event.Event;

@Getter
@AllArgsConstructor
public class RenderEvent extends Event {

    private final float partialTicks;

}
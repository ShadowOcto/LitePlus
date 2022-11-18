package me.rhys.base.event.impl.player;

import lombok.Getter;
import me.rhys.base.event.Event;

@Getter
public class StrafeEvent extends Event {
    public float strafe, friction, forward;

    public StrafeEvent(float strafe, float friction, float forward) {
        this.strafe = strafe;
        this.friction = friction;
        this.forward = forward;
    }
}

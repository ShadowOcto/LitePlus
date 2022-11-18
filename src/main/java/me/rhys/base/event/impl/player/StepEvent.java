package me.rhys.base.event.impl.player;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.event.Event;
import net.minecraft.entity.Entity;

@Setter
@Getter
public class StepEvent extends Event {

    private float stepHeight;
    private Entity entity;
    private boolean shouldStep;

    public StepEvent(Entity entity, float stepHeight) {
        super();
        this.entity = entity;
        this.stepHeight = stepHeight;
    }
}

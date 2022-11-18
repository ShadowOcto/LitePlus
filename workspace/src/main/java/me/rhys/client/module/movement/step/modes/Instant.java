package me.rhys.client.module.movement.step.modes;

import me.rhys.client.module.movement.step.Step;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.StepEvent;
import me.rhys.base.module.ModuleMode;

/**
 * Created on 22/09/2020 Package me.rhys.client.module.movement.step.modes
 */
public class Instant extends ModuleMode<Step> {
    public Instant(String name, Step parent) {
        super(name, parent);
    }

    @EventTarget
    public void onStep(StepEvent event) {
        if (mc.thePlayer == null)
            return;

        if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isCollidedVertically
                && !mc.thePlayer.checkBlockAbove(1)) {
            float stepHeight;
            if ((stepHeight = parent.getNeededStepHeight()) > parent.height) return;

            parent.lastStep = System.currentTimeMillis();
            event.setStepHeight(stepHeight);
        }
    }
}

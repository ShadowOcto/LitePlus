package me.rhys.client.module.movement.step;

import me.rhys.client.module.movement.step.modes.Instant;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

/**
 * Created on 07/09/2020 Package me.rhys.client.module.movement.step
 */
public class Step extends Module {
    @Name("Height")
    @Clamp(min = 1, max = 2)
    public double height = 1.5;
    public long lastStep;

    public Step(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(new Instant("Instant", this));
    }

    public float getNeededStepHeight() {

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX,
                1.1, mc.thePlayer.motionZ)).size() == 0)
            return 1.0F;

        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(mc.thePlayer.motionX,
                1.6, mc.thePlayer.motionZ)).size() == 0)
            return 1.5F;

        return (float) 2D;
    }
}

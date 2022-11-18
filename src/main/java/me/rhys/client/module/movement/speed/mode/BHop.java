package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class BHop extends ModuleMode<Speed> {
    public BHop(String name, Speed parent) {
        super(name, parent);
    }

    @Name("Speed")
    @Clamp(min = 0.1, max = 9)
    public double movementSpeed = 0.5;

    @Name("Jump")
    public boolean jump = true;

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (mc.thePlayer.isPlayerMoving()) {

            if (mc.thePlayer.onGround && this.jump) {
                event.motionY = mc.thePlayer.motionY = .42F;
            }

            event.setMovementSpeed(this.movementSpeed);
        } else {
            parent.pausePlayerSpeed(event);
        }
    }
}

package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;

public class YPort extends ModuleMode<Speed> {
    public YPort(String name, Speed parent) {
        super(name, parent);
    }

    @Name("Speed")
    @Clamp(min = 0.1, max = 9)
    public double movementSpeed = 0.5;

    @Name("Mode")
    public Mode mode = Mode.SET_MOTION;

    @Name("Silent YTick")
    @Clamp(min = 1, max = 20)
    public int silentTick = 3;

    @Name("Silent Ground Spoof")
    public boolean silentGroundSpoof = true;

    @EventTarget
    void onMove(PlayerMoveEvent event) {

        switch (this.mode) {
            case SET_MOTION: {
                if (mc.thePlayer.isPlayerMoving()) {
                    if (mc.thePlayer.onGround) {
                        event.motionY = mc.thePlayer.motionY = .42f;
                    } else {
                        event.motionY = mc.thePlayer.motionY - .42f;
                    }

                    event.setMovementSpeed(this.movementSpeed);
                } else {
                    parent.pausePlayerSpeed(event);

                    if (mc.thePlayer.motionY > .0f) {
                        event.motionY = mc.thePlayer.motionY - .42f;
                    }
                }
                break;
            }

            case SILENT: {
                if (mc.thePlayer.isPlayerMoving()) {
                    event.setMovementSpeed(this.movementSpeed);
                } else {
                    event.motionX = event.motionZ = mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                }
                break;
            }
        }
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.mode == Mode.SILENT && mc.thePlayer.ticksExisted % this.silentTick == 0) {

            if (this.silentGroundSpoof) {
                event.setOnGround(false);
            }

            event.getPosition().setY(event.getPosition().getY() + .42F);
        }
    }

    public enum Mode {
        SET_MOTION,
        SILENT
    }
}

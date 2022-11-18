package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.speed.Speed;

public class BlocksMC extends ModuleMode<Speed> {
    public boolean speed;

    public BlocksMC(String name, Speed parent) {
        super(name, (Speed) parent);
        this.speed = false;
    }

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (this.mc.thePlayer.isPlayerMoving()) {
            if (this.mc.thePlayer.onGround)
                event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D;
            if (this.mc.thePlayer.getMovementSpeed() > 38.0D)
                this.speed = true;
            if (!this.mc.gameSettings.keyBindLeft.pressed && !this.mc.gameSettings.keyBindRight.pressed) {
                if (!this.speed) {
                    event.setMovementSpeed(this.mc.thePlayer.getMovementSpeed());
                } else {
                    event.setMovementSpeed(this.mc.thePlayer.getMovementSpeed() / 1.2D);
                }
            } else if (!this.speed) {
                event.setMovementSpeed(this.mc.thePlayer.getMovementSpeed() / 1.2D);
            }
        } else {
            ((Speed)this.parent).pausePlayerSpeed(event);
        }
    }
}

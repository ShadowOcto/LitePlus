package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.client.module.movement.fly.Fly;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.entity.EntityPlayerSP;

public class Vanilla extends ModuleMode<Fly> {

    @Name("Speed")
    @Clamp(min = 0, max = 9)
    public double speed = 1;

    @Name("Ground Spoof")
    public boolean groundSpoof = false;

    @Name("PositionY Ground")
    public boolean positionGround = false;

    public Vanilla(String name, Fly parent) {
        super(name, parent);
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        event.setOnGround(this.groundSpoof);

        if (this.positionGround) {
            event.getPosition().setY(Math.round(event.getPosition().getY()));
        }
    }

    @EventTarget
    void playerMove(PlayerMoveEvent event) {
        EntityPlayerSP player = event.getPlayer();
        if (player == null) {
            return;
        }

        event.motionY = player.motionY = mc.gameSettings.keyBindJump.pressed
                ? speed : mc.gameSettings.keyBindSneak.pressed ? -speed : 0;

        if (mc.thePlayer.isPlayerMoving()) {
            event.setMovementSpeed(speed);
        } else {
            event.motionZ = event.motionX = 0;
        }
    }
}

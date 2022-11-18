package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.speed.Speed;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

import java.util.Collections;

public class StrafeSlow extends ModuleMode<Speed> {
    public StrafeSlow(String name, Speed parent) {
        super(name, parent);
    }

    public boolean speed = false;

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (mc.thePlayer.isPlayerMoving()) {
            if (mc.thePlayer.onGround) {
                event.motionY = mc.thePlayer.motionY = .42F;
            }
            if (mc.thePlayer.getMovementSpeed() > 38) {
                speed = true;
            }
            if (!mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed) {
                if (!speed) {
                    event.setMovementSpeed(mc.thePlayer.getMovementSpeed());
                }
                else {
                    event.setMovementSpeed(mc.thePlayer.getMovementSpeed() / 1.2);
                }
            }
            else {
                if (!speed) {
                    event.setMovementSpeed(mc.thePlayer.getMovementSpeed() / 1.2);
                }
            }
        } else {
            parent.pausePlayerSpeed(event);
        }
    }
}
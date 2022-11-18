package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;

public class Verus extends ModuleMode<Fly> {
    public Verus(String name, Fly parent) {
        super(name, parent);
    }

    @Name("$$$ Verus Heavy $$$")
    public boolean heavy = false;

    private double startY;

    @Override
    public void onEnable() {
        //Store the start position y so we know when to jump mid-air
        this.startY = mc.thePlayer.posY;
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.heavy) {
            //Bypass Verus Heavy Mode

            //Checking if our position y lands at the start y
            if (mc.thePlayer.posY < this.startY) {
                //Jump
                mc.thePlayer.motionY = .42F;

                //Spoof ground so verus doesn't cry to staff
                event.setOnGround(true);
            }

        } else {
            //Legit just ground spoof, no cap
            event.setOnGround(true);
            mc.thePlayer.motionY = .0f;

            //So we don't flag Speed 10A
            mc.thePlayer.setSprinting(false);
        }
    }

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (!this.heavy) {
            //Set the movement speed when the player moves so the fly doesn't drag behind
            if (mc.thePlayer.isPlayerMoving()) {
                event.setMovementSpeed(mc.thePlayer.defaultSpeed());
            } else {
                event.motionZ = event.motionX = mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
            }
        }
    }
}

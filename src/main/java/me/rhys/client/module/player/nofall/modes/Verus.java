package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;

public class Verus extends ModuleMode<NoFall> {
    public Verus(String name, NoFall parent) {
        super(name, parent);
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE && mc.thePlayer.fallDistance > 3) {
            mc.thePlayer.fallDistance = 0;
            mc.thePlayer.motionY = 0;
            event.setOnGround(true);
        }
    }
}

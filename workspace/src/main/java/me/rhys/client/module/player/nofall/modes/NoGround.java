package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;

public class NoGround extends ModuleMode<NoFall> {
    public NoGround(String name, NoFall parent) {
        super(name, parent);
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        event.setOnGround(false);
    }
}

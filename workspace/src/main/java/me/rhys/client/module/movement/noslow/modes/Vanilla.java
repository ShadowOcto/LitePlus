package me.rhys.client.module.movement.noslow.modes;

import me.rhys.client.module.movement.noslow.NoSlow;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerNoSlowEvent;
import me.rhys.base.module.ModuleMode;

/**
 * Created on 07/09/2020 Package me.rhys.client.module.movement.noslow.modes
 */
public class Vanilla extends ModuleMode<NoSlow> {
    public Vanilla(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventTarget
    public void onNoSlow(PlayerNoSlowEvent event) {
        event.setCancelled(true);
    }
}

package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class KeepAliveCancel extends ModuleMode<Disabler> {
    public KeepAliveCancel(String name, Disabler parent) {
        super(name, parent);
    }

    @EventTarget
    void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C00PacketKeepAlive) {
            event.setCancelled(true);
        }
    }
}

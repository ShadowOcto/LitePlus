package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C00PacketKeepAlive;

import java.util.concurrent.ThreadLocalRandom;

public class KeyChange extends ModuleMode<Disabler> {
    public KeyChange(String name, Disabler parent) {
        super(name, parent);
    }

    @Name("Max Random")
    @Clamp(min = 1, max = 100)
    public int maxRandom = 20;

    @EventTarget
    void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive c00PacketKeepAlive = (C00PacketKeepAlive) event.getPacket();
            c00PacketKeepAlive.key -= ThreadLocalRandom.current().nextInt(this.maxRandom);
        }
    }
}

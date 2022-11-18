package me.rhys.client.module.player.pingspoof.mode;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.pingspoof.PingSpoof;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;

import java.util.concurrent.TimeUnit;

public class Key extends ModuleMode<PingSpoof> {
    public Key(String name, PingSpoof parent) {
        super(name, parent);
    }

    @Name("Delay")
    @Clamp(min = 1, max = 2000)
    public int delay = 100;

    @EventTarget
    void onEvent(PacketEvent event) {
        if(event.getPacket() instanceof S00PacketKeepAlive) {
            event.setCancelled(true);
            S00PacketKeepAlive keepalive = (S00PacketKeepAlive) event.getPacket();
            Lite.EXECUTOR_SERVICE.schedule(() ->
                            mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(keepalive.func_149134_c())),
                    this.delay, TimeUnit.MILLISECONDS);
        }
    }
}

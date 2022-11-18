package me.rhys.client.module.combat.velocity;

import me.rhys.client.module.combat.velocity.modes.Normal;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S27PacketExplosion;


public class Velocity extends Module {

    @Name("Explosions")
    private boolean explosions = true;


    public Velocity(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(new Normal("Normal", this));
    }

    @EventTarget
    public void packetReceive(PacketEvent event) {
        if (event.getDirection().equals(Event.Direction.IN)) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof S27PacketExplosion) {
                event.setCancelled(explosions);
            }
        }
    }
}

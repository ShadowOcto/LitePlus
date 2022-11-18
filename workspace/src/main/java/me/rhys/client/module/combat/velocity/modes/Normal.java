package me.rhys.client.module.combat.velocity.modes;

import me.rhys.client.module.combat.velocity.Velocity;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Normal extends ModuleMode<Velocity> {

    @Name("Vertical")
    @Clamp(min = 0.0, max = 100)
    public double vertical = 0.0;

    @Name("Horizontal")
    @Clamp(min = 0.0, max = 100)
    public double horizontal = 0.0;

    public Normal(String name, Velocity parent) {
        super(name, parent);
    }

    @EventTarget
    public void packetReceive(PacketEvent event) {

        if (event.getDirection().equals(Event.Direction.IN)) {

            Packet<?> packet = event.getPacket();

            if (packet instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity velocity = (S12PacketEntityVelocity) packet;
                if (velocity.getEntityID() == mc.thePlayer.getEntityId()) {

                    if (vertical == 0.0 && horizontal == 0.0) {
                        event.setCancelled(true);
                    }

                    velocity.setMotionX((int) (velocity.getMotionX() * (horizontal / 100.0D)));
                    velocity.setMotionY((int) (velocity.getMotionY() * (vertical / 100.0D)));
                    velocity.setMotionZ((int) (velocity.getMotionZ() * (horizontal / 100.0D)));
                }
            }
        }
    }
}

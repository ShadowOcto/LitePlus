package me.rhys.base.event.impl.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rhys.base.event.Event;
import net.minecraft.network.Packet;

@Getter
@AllArgsConstructor
public class PacketEvent extends Event {

    private Packet<?> packet;

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

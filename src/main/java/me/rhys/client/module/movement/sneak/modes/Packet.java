package me.rhys.client.module.movement.sneak.modes;

import me.rhys.client.module.movement.sneak.Sneak;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Packet extends ModuleMode<Sneak> {
    public Packet(String name, Sneak parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
//        player().sendQueue.getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(player(), C0BPacketEntityAction.Action.START_SNEAKING));
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer && player().ticksExisted % 15 == 0) {
            player().sendQueue.getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(player(), C0BPacketEntityAction.Action.STOP_SNEAKING));
            player().sendQueue.getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(player(), C0BPacketEntityAction.Action.START_SNEAKING));
        }
    }

    @Override
    public void onDisable() {
        player().sendQueue.getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(player(), C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
}

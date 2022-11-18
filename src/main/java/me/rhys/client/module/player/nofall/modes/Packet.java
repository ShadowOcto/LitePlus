package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Packet extends ModuleMode<NoFall> {
    public Packet(String name, NoFall parent) {
        super(name, parent);
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE && mc.thePlayer.fallDistance > 3) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer
                    .C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
                    mc.thePlayer.posZ, true));
        }
    }
}

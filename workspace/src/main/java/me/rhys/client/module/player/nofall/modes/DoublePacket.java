package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.nofall.NoFall;
import net.minecraft.network.play.client.C03PacketPlayer;

public class DoublePacket extends ModuleMode<NoFall> {
    public DoublePacket(String name, NoFall parent) {
        super(name, parent);
    }

    @Name("Amount")
    @Clamp(min = 1, max = 20)
    public int packets = 5;

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE && mc.thePlayer.fallDistance > 3) {

            for (int i = 0; i < this.packets; i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer
                        .C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
                        mc.thePlayer.posZ, true));
            }
        }
    }
}

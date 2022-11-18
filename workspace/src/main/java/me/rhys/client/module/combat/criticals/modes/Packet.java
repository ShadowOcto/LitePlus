package me.rhys.client.module.combat.criticals.modes;

import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.client.module.combat.criticals.Criticals;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Packet extends ModuleMode<Criticals> {
    public Packet(String name, Criticals parent) {
        super(name, parent);
    }

    private final double[] offsets = {0.05, 0.0, 0.05, 0.0};
    private final Timer timer = new Timer();

    public void doCriticalHit() {
        if (this.timer.hasReached(60L) && mc.thePlayer.onGround) {
            this.timer.reset();
            for (double offset : this.offsets) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            }
        }
    }
}

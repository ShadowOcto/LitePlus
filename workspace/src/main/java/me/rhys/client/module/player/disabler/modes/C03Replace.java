package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C03PacketPlayer;

public class C03Replace extends ModuleMode<Disabler> {
    public C03Replace(String name, Disabler parent) {
        super(name, parent);
    }

    @EventTarget
    void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            event.setCancelled(true);
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer
                    .C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
        }
    }
}

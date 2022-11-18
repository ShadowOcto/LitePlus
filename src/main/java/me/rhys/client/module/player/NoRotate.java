package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

    public NoRotate(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {

        Packet<?> packet = event.getPacket();
        if (!(packet instanceof S08PacketPlayerPosLook))
            return;

        S08PacketPlayerPosLook posLook = (S08PacketPlayerPosLook) packet;

        if (mc != null && mc.thePlayer != null) {
            posLook.setYaw(mc.thePlayer.rotationYaw);
            posLook.setPitch(mc.thePlayer.rotationPitch);
        }
    }
}

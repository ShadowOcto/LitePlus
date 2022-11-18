package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Packet extends ModuleMode<Fly> {
    public Packet(String name, Fly parent) {
        super(name, parent);
    }

    @Name("Speed")
    @Clamp(min = 1, max = 9)
    public int speed = 3;

    @Name("Tick")
    @Clamp(min = 1, max = 20)
    public int tick = 2;

    private boolean tp, sentAll;
    private int wait;
    private final List<net.minecraft.network.Packet> packetList = new CopyOnWriteArrayList<>();

    @Override
    public void onEnable() {
        this.tp = this.sentAll = false;
        this.wait = 0;
        this.packetList.clear();
    }

    @EventTarget
    public void onMotion(PlayerMoveEvent event) {
        if (event.getType() == Event.Type.PRE) {
            if (this.wait < 1 && !this.sentAll) {
                this.sentAll = true;

                for (net.minecraft.network.Packet packet : this.packetList) {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packet);
                    this.packetList.remove(packet);
                }

                this.packetList.clear();
            }
        }
    }

    @EventTarget
    public void onMove(PlayerMoveEvent event) {
        if (this.wait > 0) {
            this.wait--;
            return;
        }

        if (mc.thePlayer.ticksExisted % this.tick == 0 && this.tp) {
            double x1 = -Math.sin(mc.thePlayer.getDirection()) * this.speed;
            double z1 = Math.cos(mc.thePlayer.getDirection()) * this.speed;
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer
                    .C04PacketPlayerPosition(mc.thePlayer.posX
                    + (mc.thePlayer.isPlayerMoving() ? x1 : 0), mc.thePlayer.posY
                    + (mc.gameSettings.keyBindJump.isKeyDown() ?
                    5 : (mc.gameSettings.keyBindSneak.isKeyDown() ? -5 : 0)),
                    mc.thePlayer.posZ + (mc.thePlayer.isPlayerMoving() ? z1 : 0), true));
            this.tp = false;
        }

        if (!this.tp) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer
                    .C04PacketPlayerPosition(mc.thePlayer.posX
                    * 3,
                    mc.thePlayer.posY - 9999999, mc.thePlayer.posZ * 3, mc.thePlayer.onGround));
            this.tp = true;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (this.wait > 0 && event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
            this.packetList.add(event.getPacket());
        }
    }
}

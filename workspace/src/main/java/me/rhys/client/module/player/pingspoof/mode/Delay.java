package me.rhys.client.module.player.pingspoof.mode;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import me.rhys.client.module.player.pingspoof.PingSpoof;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Delay extends ModuleMode<PingSpoof> {
    public Delay(String name, PingSpoof parent) {
        super(name, parent);
    }

    @Name("Delay")
    @Clamp(min = 1000, max = 99000)
    public int delay = 1500;

    @Name("Delay C0F")
    public boolean delayC0F = true;

    private final Timer timer = new Timer();
    private final List<Packet> packetList = new CopyOnWriteArrayList<>();

    @Override
    public void onEnable() {
        this.packetList.clear();
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.sendAll();
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.packetList.size() > 0 && this.timer.hasReached(this.delay) && event.getType() == Event.Type.PRE) {
            this.sendAll();
        }
    }

    @EventTarget
    void onPacket(PacketEvent event) {
        boolean isTransaction = false;
        if (event.getPacket() instanceof C00PacketKeepAlive
                || (isTransaction = event.getPacket() instanceof C0FPacketConfirmTransaction)) {
            if (this.delayC0F && isTransaction) return;
            event.setCancelled(true);
            this.packetList.add(event.getPacket());
        }
    }

    void sendAll() {
        this.packetList.forEach(packet -> {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packet);
            packetList.remove(packet);
        });
        this.timer.reset();
    }
}

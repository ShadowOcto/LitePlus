package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class TransactionCancel extends ModuleMode<Disabler> {
    public TransactionCancel(String name, Disabler parent) {
        super(name, parent);
    }

    @Name("Block C0F Below 0")
    public boolean blockNegs = true;

    @EventTarget
    void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
            if (this.blockNegs) {
                C0FPacketConfirmTransaction c0FPacketConfirmTransaction = (C0FPacketConfirmTransaction) event.getPacket();
                if (c0FPacketConfirmTransaction.getUid() < 0) event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }
}

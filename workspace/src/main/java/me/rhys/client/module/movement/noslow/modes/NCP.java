package me.rhys.client.module.movement.noslow.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerNoSlowEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.client.module.movement.noslow.NoSlow;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Created on 07/09/2020 Package me.rhys.client.module.movement.noslow.modes
 */
public class NCP extends ModuleMode<NoSlow> {
    private final Timer timer = new Timer();

    public NCP(String name, NoSlow parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @EventTarget
    public void onMotion(PlayerMotionEvent event) {
        if (!mc.thePlayer.isBlocking()) return;

        if (timer.hasReached(50L)) {
            timer.reset();
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            return;
        }

        if (event.getType() == Event.Type.PRE) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        } else {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }
    }

    @EventTarget
    public void onNoSlow(PlayerNoSlowEvent event) {
        event.setCancelled(true);
    }
}

package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.vec.Vec3f;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NullPlace extends ModuleMode<Disabler> {
    public NullPlace(String name, Disabler parent) {
        super(name, parent);
    }
    
    @Name("Check inventory slot")
    public boolean checkSlot = true;
    
    @EventTarget
    void onUpdate(PlayerUpdateEvent event) {
        if (this.checkSlot && mc.thePlayer.getHeldItem() != null) {
            return;
        }

        Vec3f hitVec = (new Vec3f(new BlockPos(
                mc.thePlayer.posX,
                mc.thePlayer.posY - 1,
                mc.thePlayer.posZ)))
                .add(0.5f, 0.5f, 0.5f)
                .add((new Vec3f(EnumFacing.DOWN.getDirectionVec())).scale(0.5F));

        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
                new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ),
                EnumFacing.DOWN.getIndex(), null, hitVec.x, hitVec.y, hitVec.z));
    }
}

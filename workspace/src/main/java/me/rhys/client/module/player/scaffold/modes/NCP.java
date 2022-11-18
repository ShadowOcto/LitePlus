package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

public class NCP extends ModuleMode<Scaffold> {
    public NCP(String name, Scaffold parent) {
        super(name, parent);
    }

    private Scaffold.BlockEntry lastBlockEntry;
    private int lastSlot, startSlot;
    private final Timer towerTimer = new Timer();

    @Override
    public void onEnable() {
        this.startSlot = mc.thePlayer.inventory.currentItem;

        if (parent.getSlotWithBlock() >- 1) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(parent.getSlotWithBlock()));
        }

        this.lastSlot = parent.getSlotWithBlock();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.startSlot));
        this.lastBlockEntry = null;
    }

    @EventTarget
    public void onMotion(PlayerMotionEvent event) {

        if (this.lastBlockEntry != null) {
            Vec2f rotation = RotationUtil.getRotations(parent.getPositionByFace(this.lastBlockEntry.getPosition(),
                    this.lastBlockEntry.getFacing()));

            if (mc.thePlayer.isPlayerMoving()) {
                mc.thePlayer.setSprinting(parent.sprint);
            }

            event.getPosition().setRotation(rotation);
        }

        Scaffold.BlockEntry blockEntry = parent.find(new Vec3(0, 0, 0));
        this.lastBlockEntry = blockEntry;

        if (event.getType() == Event.Type.POST && blockEntry != null) {
            int slot = parent.getSlotWithBlock();

            if (slot > -1) {

                if (this.lastSlot != slot) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    this.lastSlot = slot;
                }

                if (parent.placeBlock(blockEntry.getPosition().add(0, 0, 0), blockEntry.getFacing(),
                        slot, parent.swing)) {

                   if (parent.tower && !mc.thePlayer.isPotionActive(Potion.jump) && !mc.thePlayer.isPlayerMoving()
                           && mc.gameSettings.keyBindJump.isKeyDown()) {
                       mc.thePlayer.motionY = .42f;
                       mc.thePlayer.motionZ = mc.thePlayer.motionX = 0;

                       if (this.towerTimer.hasReached(1500L)) {
                           this.towerTimer.reset();
                           mc.thePlayer.motionY =- .28f;
                       }
                   }
                }
            }
        }
    }
}

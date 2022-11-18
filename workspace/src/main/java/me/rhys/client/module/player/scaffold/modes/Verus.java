package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.util.Vec3;

public class Verus extends ModuleMode<Scaffold> {

    private Scaffold.BlockEntry blockEntry;

    private int startSlot, lastSlot;
    private boolean didPlaceBlock;

    public Verus(String name, Scaffold parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        blockEntry = null;
        this.didPlaceBlock = false;

        this.startSlot = mc.thePlayer.inventory.currentItem;

        if (parent.getSlotWithBlock() > -1) {
            mc.thePlayer.inventory.currentItem = parent.getSlotWithBlock();
        }

        lastSlot = parent.getSlotWithBlock();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer.inventory.currentItem != this.startSlot) {
            mc.thePlayer.inventory.currentItem = this.startSlot;
        }
    }


    @EventTarget
    public void onMotion(PlayerMotionEvent event) {
        Vec2f vec2f = null;

        if (mc.thePlayer.isPlayerMoving()) {
            getMc().thePlayer.setSprinting(parent.sprint);
        }

        if (this.blockEntry != null) {
            vec2f = RotationUtil.getRotations(
                    parent.getPositionByFace(blockEntry.getPosition(),
                            blockEntry.getFacing()));

            vec2f.setVecY(90);
        }

        Scaffold.BlockEntry blockEntry = (parent.find(new Vec3(0, 0, 0)));

        if (blockEntry == null) return;

        this.blockEntry = blockEntry;

        if (vec2f != null) {
            event.getPosition().setRotation(vec2f);
        }

        int slot = parent.getSlotWithBlock();

        if (parent.getBlockCount() < 1 && this.didPlaceBlock) {
            mc.thePlayer.motionY -= 20;
            this.didPlaceBlock = false;
            return;
        }

        if (this.blockEntry != null && vec2f != null && slot > -1 && event.getType() == Event.Type.PRE) {
            if (lastSlot != slot) {
                mc.thePlayer.inventory.currentItem = slot;
                lastSlot = slot;
            }

            parent.placeBlockVerus(this.blockEntry.getPosition().add(0, 0, 0), this.blockEntry.getFacing(),
                    slot, parent.swing);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
    }

    @EventTarget
    public void onKeyboard(KeyboardInputEvent event) {
        if (!parent.sprint && event.getKeyCode() == 29) {
            event.setCancelled(true);
        }
    }
}
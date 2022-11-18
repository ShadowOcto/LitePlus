package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.util.Vec3;

public class Expand extends ModuleMode<Scaffold> {

    private int startSlot, lastSlot;

    @Name(value = "Expansion")
    @Clamp(min = 1, max = 6)
    private int expansion = 4;

    public Expand(String name, Scaffold parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
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

        int slot = parent.getSlotWithBlock();
        if (slot > -1 && event.getType() == Event.Type.PRE) {

            if (lastSlot != slot) {
                mc.thePlayer.inventory.currentItem = slot;
                lastSlot = slot;
            }

            int expand = expansion * 5;

            for (int i = 0; i < expand; i++) {
                Scaffold.BlockEntry blockEntry = parent.findExpand(new Vec3(mc.thePlayer.motionX * i,
                        0, mc.thePlayer.motionZ * i), i);

                if (blockEntry != null) {
                    event.getPosition().setRotation((RotationUtil.getRotations(
                            parent.getPositionByFace(blockEntry.getPosition(),
                                   blockEntry.getFacing()))));
                    parent.placeBlock(blockEntry.getPosition(), blockEntry.getFacing(),
                            parent.getSlotWithBlock(), parent.swing);
                }
            }
        }
    }

    @EventTarget
    public void onKeyboard(KeyboardInputEvent event) {
        if (!parent.sprint && event.getKeyCode() == 29) {
            event.setCancelled(true);
        }
    }
}

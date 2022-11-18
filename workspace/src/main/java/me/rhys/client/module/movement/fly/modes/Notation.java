package me.rhys.client.module.movement.fly.modes;

import com.sun.org.apache.xpath.internal.operations.Mod;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.client.module.movement.fly.Fly;

public class Notation extends ModuleMode<Fly> {
    public Notation(String name, Fly parent) {
        super(name, parent);
    }

    @Name("Spoof Ground")
    public boolean spoofGround = true;

    @Name("Notation Tick")
    @Clamp(min = 1, max = 60)
    public int notationTick = 2;

    @Name("Negative")
    public boolean negative = false;

    @Name("Add Randomization")
    public boolean random = true;

    @Name("Mode")
    public Mode mode = Mode.SILENT;

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        event.motionY = mc.thePlayer.motionY = 0;

        if (mc.thePlayer.isPlayerMoving()) {
            event.setMovementSpeed(mc.thePlayer.defaultSpeed());
        } else {
            event.motionX = event.motionZ = mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        }
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.spoofGround && !mc.thePlayer.onGround) {
            event.setOnGround(true);
        }

        double addition = this.random ? MathUtil.randFloat(0.000000000010f, 0.000000000070f) : 0;
        double value = event.getPosition().getY();

        if (mc.thePlayer.ticksExisted % this.notationTick == 0) {
            value = event.getPosition().getY() + 1E-5F + addition;
        } else {
            if (this.negative) {
                value = event.getPosition().getY() - 1E-5F - addition;
            }
        }

        switch (mode) {
            case SILENT: {
                event.getPosition().setY(value);
                break;
            }

            case SET_POSITION: {
                mc.thePlayer.setPosition(mc.thePlayer.posX, value, mc.thePlayer.posZ);
                break;
            }
        }
    }

    public enum Mode {
        SILENT,
        SET_POSITION
    }
}

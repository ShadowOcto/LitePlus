package me.rhys.client.module.longjump.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.longjump.Longjump;


public class BlocksMC extends ModuleMode<Longjump> {
    public BlocksMC(String name, Longjump parent) {
        super(name, (Longjump) parent);
        this.time = 20.0D;
    }

    @Name("Timer Speed")
    @Clamp(min = 0.1D, max = 2.0D)
    public static double timerspeed = 0.3D;

    public double time;

    @Name("MotionY")
    @Clamp(min = 0.1D, max = 0.6D)
    public static double my = 0.42D;

    @Name("Speed")
    @Clamp(min = 0.1D, max = 0.8D)
    public static double sp = 0.51D;

    public void onEnable() {
        this.mc.thePlayer.motionY = my;
        this.time = 0.0D;
        if (mc.thePlayer.onGround) {
            this.mc.thePlayer.motionY = my;
            this.time = 0.0D;
        }
    }

    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0F;
    }

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (this.time < 14.0D) {
            this.time++;
            this.mc.timer.timerSpeed = (float)timerspeed;
            event.setMovementSpeed(sp);
        } else {
            this.mc.thePlayer.setSpeed(this.mc.thePlayer.getMovementSpeed() / 1.2D);
        }
        }
    }


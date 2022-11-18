package me.rhys.client.module.movement.speed.mode;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.speed.Speed;
import net.minecraft.potion.Potion;

public class NCP extends ModuleMode<Speed> {

    private double lastDistance, movementSpeed;
    private int stage;

    public NCP(String name, Speed parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        this.stage = 0;
        this.lastDistance = 0;
        this.movementSpeed = mc.thePlayer.getMovementSpeed();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        double x = (mc.thePlayer.posX - mc.thePlayer.prevPosX);
        double z = (mc.thePlayer.posZ - mc.thePlayer.prevPosZ);

        this.lastDistance = Math.sqrt(x * x + z * z);
    }

    @EventTarget
    void onMove(PlayerMoveEvent event) {
        if (!mc.thePlayer.isPlayerMoving()) return;

        boolean hasSpeed = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null;

        if (mc.thePlayer.onGround || stage == 0) {
            stage = 0;
            event.motionY = mc.thePlayer.motionY = 0.42f;
            movementSpeed = mc.thePlayer.getMovementSpeed() * (hasSpeed ? 2.1F : 2.20F);
        } else if (stage == 1) {
            event.motionY = mc.thePlayer.motionY - 0.00500f;
            movementSpeed = lastDistance - (0.66 * (lastDistance - mc.thePlayer.getMovementSpeed()));
        } else {
            event.motionY = mc.thePlayer.motionY - 0.00820f;
            movementSpeed = (lastDistance - mc.thePlayer.getMovementSpeed() / 33.1);
        }

        event.setMovementSpeed(Math.max(movementSpeed, mc.thePlayer.getMovementSpeed()));
        stage++;
    }
}

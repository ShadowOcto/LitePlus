package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;

public class Creative extends ModuleMode<Fly> {

    @Name("Speed")
    @Clamp(min = 0, max = 3)
    public double speed = 1;

    @Name("Ground Spoof")
    public boolean groundSpoof = false;

    @Name("PositionY Ground")
    public boolean positionGround = false;

    public Creative(String name, Fly parent) {
        super(name, parent);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        event.setOnGround(this.groundSpoof);
        if (this.positionGround) {
            event.getPosition().setY(Math.round(event.getPosition().getY()));
        }
    }

    @EventTarget
    void playerMove(PlayerMoveEvent event) {
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.capabilities.setFlySpeed((float) speed);
    }
}

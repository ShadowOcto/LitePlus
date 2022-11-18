package me.rhys.client.module.combat;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PVPBot extends Module {
    public PVPBot(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @Name("Max Distance")
    @Clamp(min = 1, max = 30)
    public int distance = 15;

    @Name("Tab Check")
    public boolean tabCheck = false;

    private Vec2f lockViewRotation;
    private int aimTicks;
    private boolean allowMovement;
    private final Timer attackTimer = new Timer();

    @Override
    public void onEnable() {
        this.allowMovement = true;
        this.lockViewRotation = null;
        this.aimTicks = 0;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (this.lockViewRotation != null) {
            mc.thePlayer.rotationYaw = this.lockViewRotation.getVecX();
            mc.thePlayer.rotationPitch = this.lockViewRotation.getVecY();

            if (++this.aimTicks > 20) {
                mc.gameSettings.keyBindForward.pressed = this.allowMovement;
            }
        }
    }

    @EventTarget
    public void onMove(PlayerMoveEvent event) {
        if (this.aimTicks > 22 && mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
            event.motionY = mc.thePlayer.motionY = .42f;
        }
    }

    @EventTarget
    public void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE) {
            Entity entity = this.getTarget();

            if (entity != null) {
                this.allowMovement = mc.thePlayer.getDistanceToEntity(entity) > 3;
                this.lockViewRotation = RotationUtil.getRotations(entity);

                if (this.aimTicks > 22) {
                    this.attack(entity);
                }
            } else {
                this.aimTicks = 0;
            }
        }
    }

    private Entity getTarget() {
        for (Entity entity : mc.theWorld.loadedEntityList) {

            if (entity == null || mc.thePlayer.getDistanceToEntity(entity) > this.distance * 5
                    || mc.thePlayer.isEntityEqual(entity) || (this.tabCheck
                    && !this.checkTab(entity)) || !(entity instanceof EntityPlayer)) continue;

            return entity;
        }
        return null;
    }

    private void attack(Entity entity) {
        int cps = 13;

        if (mc.thePlayer.getDistanceToEntity(entity) < 2.8) {
            mc.gameSettings.keyBindSprint.pressed = false;

            double aps = (cps + MathUtil.randFloat(MathUtil.randFloat(1, 3), MathUtil.randFloat(3, 5)));

            if (this.attackTimer.hasReached(1000L / aps)) {
                this.attackTimer.reset();

                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(player(), entity);
            }
        } else {
            mc.gameSettings.keyBindSprint.pressed = true;
        }
    }

    private boolean checkTab(Entity entity) {
        return entity instanceof EntityPlayer && GuiPlayerTabOverlay.getPlayerList().contains(entity);
    }
}

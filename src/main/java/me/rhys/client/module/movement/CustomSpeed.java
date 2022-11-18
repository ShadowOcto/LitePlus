package me.rhys.client.module.movement;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.MoveUtils;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.MathHelper;

import java.util.concurrent.ThreadLocalRandom;

public class CustomSpeed extends Module {
    public CustomSpeed(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @Name("Set Speed On Toggle")
    public boolean setSpeedOnToggle = true;

    @Name("Set MotionY On Ground")
    public boolean motionOnGround = true;

    @Name("Custom MotionY")
    public boolean customMotionY = false;

    @Name("Custom MotionY Jump")
    @Clamp(min = 0.0, max = 1)
    public double customMotionYOnJump = 0.42;

    @Name("Custom Speed")
    @Clamp(min = 0, max = 9)
    public double customSpeed = 1.5;

    @Name("Speed Up Type")
    public SpeedUpMode speedUpMode = SpeedUpMode.SET_SPEED;

    @Name("Speed State")
    public SpeedState speedState = SpeedState.ALWAYS;

    @Name("Fast Fall")
    public boolean fastFall = false;

    @Name("Fast Fall Below Motion")
    public boolean fastFallMotionBelow = true;

    @Name("Use Custom Fall Motion")
    public boolean useCustomFallMotion = false;

    @Name("Custom Fall Motion")
    @Clamp(min = 0, max = 9)
    public double customFallMotion = 0.42;

    @Name("Set Speed On Move")
    public boolean setSpeedOnMove = true;

    @Name("Set Motion On Move")
    public boolean setMotionOnMove = true;

    @Name("Spoof On Ground")
    public boolean spoofOnGround = false;

    @Name("Spoof Off Ground")
    public boolean spoofOffGround = false;

    @Name("Be 1/64 On Ground")
    public boolean magicGround = false;

    @Name("Be 1/64 In Air")
    public boolean magicValueAir = false;

    @Name("Magic Value State")
    public MagicValueState magicValueState = MagicValueState.BLOCK;

    private double lastDistance, movementSpeed;
    private int stage;

    private int voidIndex = 0;

    @Override
    public void onEnable() {
        this.lastDistance = 0;
        this.movementSpeed = 0;
        this.stage = 0;
        this.voidIndex = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    @EventTarget
    public void onMotion(PlayerMotionEvent event) {

        double x = (mc.thePlayer.posX - mc.thePlayer.prevPosX);
        double z = (mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
        this.lastDistance = Math.sqrt(x * x + z * z);

        if (event.getType() == Event.Type.PRE) {

            if (this.spoofOnGround && !mc.thePlayer.onGround) {
                event.setOnGround(true);
            }

            if (this.spoofOffGround && mc.thePlayer.onGround) {
                event.setOnGround(false);
            }

            if ((this.magicGround && (mc.thePlayer.onGround || mc.thePlayer.posY % 0.015625 == 0))
                    || this.magicValueAir && !((mc.thePlayer.onGround || mc.thePlayer.posY % 0.015625 == 0))) {
                double offset = .5D;

                if (this.magicValueState == MagicValueState.NORMAL_JUMP) {
                    offset = .42f;
                }

                event.getPosition().setY(event.getPosition().getY() + offset);
            }
        }
    }

    @EventTarget
    public void onMove(PlayerMoveEvent event) {
        if (this.motionOnGround && mc.thePlayer.onGround) {
            this.handleJump(event);
        } else if (!this.motionOnGround) {
            this.handleJump(event);
        }

        if (this.fastFall && !mc.thePlayer.onGround) {
            if (this.fastFallMotionBelow) {
                if (mc.thePlayer.motionY < 0) {
                    this.handleFall(event);
                }
            } else {
                this.handleFall(event);
            }
        }

        switch (speedState) {
            case ON_GROUND: {
                if (mc.thePlayer.onGround) {
                    this.doSpeed(this.customSpeed, event);
                }
                break;
            }

            case ALWAYS: {
                this.doSpeed(this.customSpeed, event);
            }

            case FALLING: {
                if (mc.thePlayer.motionY < 0 && !mc.thePlayer.onGround) {
                    this.doSpeed(this.customSpeed, event);
                }
                break;
            }

            case ON_GROUND_AND_FALLING: {
                if (mc.thePlayer.onGround || (mc.thePlayer.motionY != 0)) {
                    this.doSpeed(this.customSpeed, event);
                }
                break;
            }
        }
    }

    private void handleJump(PlayerMoveEvent event) {
        if (this.setMotionOnMove && !this.isMoving()) return;

        if (this.customMotionY) {
            event.motionY = mc.thePlayer.motionY = this.customMotionYOnJump;
        } else {
            event.motionY = mc.thePlayer.motionY = .42f;
        }
    }

    private void handleFall(PlayerMoveEvent event) {
        if (this.setMotionOnMove && !this.isMoving()) return;

        if (this.useCustomFallMotion) {
            event.motionY = mc.thePlayer.motionY -= this.customFallMotion;
        } else {
            event.motionY = mc.thePlayer.motionY - .42f;
        }
    }

    private void doSpeed(double speed, PlayerMoveEvent playerMoveEvent) {

        if (this.setSpeedOnMove && !this.isMoving()) return;

        switch (speedUpMode) {
            case SET_SPEED: {
                playerMoveEvent.setMovementSpeed(speed);
                break;
            }

            case FRICTION: {
                MoveUtils.setFriction(speed);
                break;
            }

            case ACCELERATION: {
                if (mc.thePlayer.onGround || stage == 0) {
                    stage = 0;
                    movementSpeed = mc.thePlayer.getMovementSpeed() * this.customSpeed;
                } else if (stage == 1) {
                    movementSpeed = lastDistance - (0.66 * (lastDistance - mc.thePlayer.getMovementSpeed()));
                } else {
                    movementSpeed = (lastDistance - mc.thePlayer.getMovementSpeed() / 33.1);
                }

                stage++;
                playerMoveEvent.setMovementSpeed(movementSpeed);
                break;
            }

            case TELEPORT: {
                MoveUtils.teleportForward(speed, MoveUtils.TeleportMode.SET_POSITION_UPDATE);
                break;
            }
        }
    }

    private boolean isMoving() {
        return mc.thePlayer.isPlayerMoving()
                || mc.thePlayer.moveForward != 0
                || mc.thePlayer.moveStrafing != 0;
    }

    public enum VoidTPMode {
        PRE,
        POST,
        BOTH
    }

    public enum VoidType {
        DOWN,
        UP,
        SWITCH,
        RANDOM
    }

    public enum MagicValueState {
        BLOCK,
        NORMAL_JUMP
    }

    public enum SpeedState {
        ON_GROUND,
        ALWAYS,
        FALLING,
        ON_GROUND_AND_FALLING
    }

    public enum SpeedUpMode {
        SET_SPEED,
        FRICTION,
        ACCELERATION,
        TELEPORT
    }
}

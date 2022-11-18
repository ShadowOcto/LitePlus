package me.rhys.base.util;

import me.rhys.base.util.entity.Location;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * Created on 07/09/2020 Package me.rhys.lite.util
 */
public class RotationUtil {

    private static float lastYaw = -1, lastPitch = -1;
    public static Location location, lastLocation;
    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public static Vec2f getNormalRotations(Entity entity) {
        return getNormalRotations(minecraft.thePlayer.getPositionVector().addVector(0.0D,
                minecraft.thePlayer.getEyeHeight(), 0.0D), entity.getPositionVector().addVector(0.0D, entity.getEyeHeight() / 2, 0.0D));
    }

    public static Vec2f getNormalRotations(Vec3 origin, Vec3 position) {
        Vec3 org = new Vec3(origin.xCoord, origin.yCoord, origin.zCoord);
        Vec3 difference = position.subtract(org);

        double distance = difference.flat().lengthVector();

        float yaw = ((float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));

        return new Vec2f(yaw, pitch);
    }

    public static Vec2f getRotations(Vec3 origin, Vec3 position) {

        Vec3 org = new Vec3(origin.xCoord, origin.yCoord, origin.zCoord);
        Vec3 difference = position.subtract(org);
        double distance = difference.flat().lengthVector();
        float yaw = ((float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));

        return new Vec2f(yaw, pitch);
    }

    public static Vec2f getRotations(Entity entity) {
        return getRotations(minecraft.thePlayer.getPositionVector().addVector(0.0D,
                minecraft.thePlayer.getEyeHeight(), 0.0D), entity.getPositionVector().addVector(0.0D, entity.getEyeHeight() / 2, 0.0D));
    }

    public static Vec2f getRotations(Vec3 position) {
        return getRotations(minecraft.thePlayer.getPositionVector().addVector(0.0D, minecraft.thePlayer.getEyeHeight(), 0.0D), position);
    }


    public static Vec2f clampRotation(Vec2f rotation) {
        float f = minecraft.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 1.2f;

        return new Vec2f(rotation.x - (rotation.x % f1), rotation.y - (rotation.y % f1));
    }

    public static float updateYawRotation(float playerYaw, float targetYaw, float maxSpeed) {
        float speed = MathHelper.wrapAngleTo180_float(((targetYaw - playerYaw)));
        if (speed > maxSpeed)
            speed = maxSpeed;
        if (speed < -maxSpeed)
            speed = -maxSpeed;
        return (playerYaw + speed);
    }

    public static float updatePitchRotation(float playerPitch, float targetPitch, float maxSpeed) {
        float speed = MathHelper.wrapAngleTo180_float(((targetPitch - playerPitch)));
        if (speed > maxSpeed)
            speed = maxSpeed;
        if (speed < -maxSpeed)
            speed = -maxSpeed;
        return (playerPitch + speed);
    }

    public static Vec2f getRandomizedRotations(Entity entity) {
        return getRandomizedRotations(minecraft.thePlayer.getPositionVector().addVector(0.0D,
                minecraft.thePlayer.getEyeHeight(), 0.0D), entity.getPositionVector().addVector(0.0D, entity.getEyeHeight() / 2, 0.0D));
    }

    public static Vec2f getRandomizedRotations(Vec3 origin, Vec3 position) {
        Vec3 org = new Vec3(origin.xCoord, origin.yCoord, origin.zCoord);
        Vec3 difference = position.subtract(org);

        double distance = difference.flat().lengthVector();
        float rYaw = MathUtil.randFloat(4, 8);
        float rPitch = MathUtil.randFloat(5, 15);

        if (rYaw == lastYaw) rYaw *= -1;
        if (rPitch == lastPitch) rPitch *= -1;

        float yaw = ((float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));

        if (MathUtil.randFloat(1, 20) < 15) {
            yaw += rYaw / 3;
            if (Math.abs((pitch + rPitch)) <= 90) {
                pitch += rPitch / 3;
            }
        } else {
            yaw += rYaw;
            if (Math.abs((pitch + rPitch)) <= 90) {
                pitch += rPitch;
            }
        }

        lastYaw = rYaw;
        lastPitch = rPitch;
        return new Vec2f(yaw, pitch);
    }
}

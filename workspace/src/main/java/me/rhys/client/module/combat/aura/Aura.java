package me.rhys.client.module.combat.aura;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.entity.RayCast;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.combat.aura.modes.Single;
import me.rhys.client.module.combat.criticals.Criticals;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.*;

public class Aura extends Module {
    public Aura(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(new Single("Single", this));
    }

    @Name("Attack Method")
    public AttackMethod attackMethod = AttackMethod.PRE;

    @Name("Rounding Type")
    public RoundingType roundingType = RoundingType.MODULO;

    @Name("Rotation Type")
    public RotationType rotationType = RotationType.NORMAL;

    @Name("Block Type")
    public BlockMode blockMode = BlockMode.NCP_INTERACT;

    @Name("Unblock Type")
    public UnBlockMode unBlockMode = UnBlockMode.SWING;

    @Name("CPS")
    @Clamp(min = 1, max = 20)
    public double cps = 15;

    @Name("Reach")
    @Clamp(min = 1, max = 9)
    public double reach = 4.25f;

    @Name("Smoothness")
    @Clamp(min = 0, max = 100)
    public float smoothness = 0f;

    @Name("AutoBlock")
    public boolean autoBlock = false;

    @Name("RayCheck")
    public boolean rayCheck = true;

    @Name("RayCast")
    public boolean rayCast = true;

    @Name("Monsters")
    public boolean monsters = false;

    @Name("Sleeping")
    public boolean sleeping = true;

    @Name("Invisibles")
    public boolean invisible = true;

    @Name("Dead Players")
    public boolean deadPlayers = false;

    @Name("KeepSprint")
    public boolean keepSprint = false;

    @Name("LockView")
    public boolean lockView = false;

    @Name("Swing")
    public boolean swing = true;

    @Name("Crack")
    public boolean crack = false;

    @Name("Crack Type")
    public CrackType crackType = CrackType.NORMAL;

    @Name("Crack Size")
    @Clamp(min = 1, max = 20)
    public int crackSize = 4;

    public EntityLivingBase target;

    public final Timer attackTimer = new Timer();
    public Vec2f currentRotation = null;
    public boolean blocking;

    @Override
    public void onEnable() {
        this.attackTimer.reset();
    }

    @Override
    public void onDisable() {
        this.blocking = false;
        this.target = null;
        this.currentRotation = null;
        mc.thePlayer.itemInUseCount = 0;
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        this.target = this.findTarget();

        if (this.rayCast && this.target != null && this.currentRotation != null) {
            EntityLivingBase rayCast;
            if ((rayCast = RayCast.rayCast(target, currentRotation.getVecX(), currentRotation.getVecY())) != null) {
                this.target = rayCast;
            }
        }

        if (this.target != null) {
            mc.thePlayer.setSprinting(this.keepSprint);

            if (!this.keepSprint) {
                mc.gameSettings.keyBindSprint.pressed = false;
            }
        }
    }

    EntityLivingBase findTarget() {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != null) {
                if (!this.isEntityValid(entity) || !(entity instanceof EntityLivingBase)) continue;
                return (EntityLivingBase) entity;
            }
        }
        return null;
    }

    boolean isEntityValid(Entity entity) {
        if (mc.thePlayer.isEntityEqual(entity)) return false;

        //Checking reach distance
        if (rayCheck) {
            final AxisAlignedBB targetBox = entity.getEntityBoundingBox();
            final Vec2f rotation = RotationUtil.getRotations(entity);

            final Vec3 origin = mc.thePlayer.getPositionEyes(1.0f);
            Vec3 look = entity.getVectorForRotation(rotation.y, rotation.x);

            look = origin.addVector(look.xCoord * reach,
                    look.yCoord * reach,
                    look.zCoord * reach);
            MovingObjectPosition collision = targetBox.calculateIntercept(origin, look);

            if (collision == null) return false;
        } else if (mc.thePlayer.getDistanceToEntity(entity) >= reach) return false;

        if (Lite.FRIEND_MANAGER.getFriend(entity.getName()) != null) return false;

        if (!sleeping && ((EntityLivingBase) entity).isPlayerSleeping())
            return false;

        if (!sleeping && ((EntityLivingBase) entity).isPlayerSleeping())
            return false;


        if (entity instanceof EntityArmorStand)
            return false;

        if (entity.isInvisible() && !invisible)
            return false;

        if (!monsters && (entity instanceof EntityMob || entity instanceof EntityVillager))
            return false;

        if (!this.deadPlayers && entity.isDead)
            return false;

        return monsters || entity instanceof EntityPlayer;
    }

    void doCritical() {
        Criticals criticals = (Criticals) Lite.MODULE_FACTORY.findByClass(Criticals.class);

        if (criticals.getData().isEnabled()) {
            criticals.processCriticalHit();
        }

        if (this.crack) {
            for (int i = 0; i < this.crackSize; i++) {
                if (this.crackType == CrackType.NORMAL) {
                    mc.thePlayer.onCriticalHit(this.target);
                }

                if (this.crackType == CrackType.ENCHANT) {
                    mc.thePlayer.onEnchantmentCritical(this.target);
                }
            }
        }
    }

   public void swing(Entity target, PlayerMotionEvent playerMotionEvent) {
        if (this.autoBlock) {
            this.useItem(playerMotionEvent);
        }

        double aps = (cps + MathUtil.randFloat(MathUtil.randFloat(1, 3), MathUtil.randFloat(3, 5)));

        if (this.attackTimer.hasReached(1000L / aps)) {
            this.attackTimer.reset();

            // unblock before attack
            if (this.autoBlock && this.unBlockMode == UnBlockMode.ATTACK && this.blocking
                    && mc.thePlayer.isBlocking()) {
                this.blocking = false;
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN,
                        EnumFacing.DOWN
                ));
            }

            if (this.swing) {
                mc.thePlayer.swingItem();
            }

            doCritical();

            // unblock before attack
            if (this.autoBlock && this.unBlockMode == UnBlockMode.ATTACK && this.blocking
                    && mc.thePlayer.isBlocking()) {
                this.blocking = false;
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN,
                        EnumFacing.DOWN
                ));
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        }
    }

    void useItem(PlayerMotionEvent event) {
        if (this.autoBlock) {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                this.blocking = true;

                switch (this.blockMode) {
                    case NCP: {
                        mc.playerController.syncCurrentPlayItem();
                        ItemStack itemstack = mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                        if (itemstack != mc.thePlayer.getHeldItem() || itemstack != null) {
                            mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = itemstack;
                            if (itemstack.stackSize == 0)
                                mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                        }
                        break;
                    }
                    case FAKE: {
                        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            mc.thePlayer.itemInUseCount = 1;
                        }
                    }
                    break;
                    case NCP_INTERACT: {
                        mc.playerController.syncCurrentPlayItem();
                        ItemStack itemstack = mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);

                        if (target instanceof EntityPlayer) {
                            mc.playerController.interactWithEntitySendPacket(mc.thePlayer, this.target);
                            target.interactAt((EntityPlayer) this.target, new Vec3(-1, -1, -1));
                        }

                        if (itemstack != mc.thePlayer.getHeldItem() || itemstack != null) {
                            mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = itemstack;
                            if (itemstack.stackSize == 0)
                                mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                        }
                        break;

                    }
                }
            }
        }
    }

    public void aimAtTarget(PlayerMotionEvent event, Entity target) {
        Vec2f rotation = getRotations(target);
        if (rotation == null) return;

        if (smoothness > 0f) {
            if (currentRotation == null)
                currentRotation = new Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            float yaw = RotationUtil.updateYawRotation(currentRotation.x, rotation.x,
                    Math.max(1, 180 * (1 - smoothness / 100f)));
            float pitch = RotationUtil.updatePitchRotation(currentRotation.y, rotation.y,
                    Math.max(1, 90f * (1 - smoothness / 100f)));

            rotation.x = yaw;
            rotation.y = pitch;
            currentRotation = rotation;
        }

        if (this.roundingType == RoundingType.MINECRAFT) rotation = RotationUtil.clampRotation(rotation);
        event.getPosition().setRotation(rotation);
    }

    public Vec2f getRotations(Entity entity) {
        switch (this.rotationType) {
            case NORMAL: {
                return this.wrapRotation(RotationUtil.getNormalRotations(entity));
            }

            case RANDOM: {
                return this.wrapRotation(RotationUtil.getRandomizedRotations(entity));
            }
        }

        return null;
    }

    Vec2f wrapRotation(Vec2f vec2f) {
        float yaw = vec2f.getVecX();
        float pitch = vec2f.getVecY();

        switch (this.roundingType) {
            case MODULO: {
                float sensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
                float f = sensitivity * 0.6F + 0.2F;
                float f2 = f * f * f * 1.2F;

                yaw -= yaw % f2;
                pitch -= pitch % f2;
                break;
            }

            case MODULO2: {
                float sensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
                float f = sensitivity * 0.6F + 0.2F;
                float f2 = f * f * f * 1.2F;

                yaw -= yaw % (f2 / 4);
                pitch -= pitch % (f2 / 4);
                break;
            }

            case ROUND: {
                yaw = (float) MathUtil.preciseRound(yaw, 1);
                pitch = (float) MathUtil.preciseRound(pitch, 1);
                break;
            }
            case SMOOTH: {
                float sensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
                yaw = (float) MathUtil.preciseRound(yaw, 2);
                pitch = (float) MathUtil.preciseRound(pitch, 2);
                break;
            }
        }

        return new Vec2f(yaw, pitch);
    }

    public enum CrackType {
        ENCHANT,
        NORMAL
    }

    public enum AttackMethod {
        PRE,
        POST
    }

    public enum RoundingType {
        NONE,
        MINECRAFT,
        MODULO,
        MODULO2,
        SMOOTH,
        ROUND,
    }

    public enum RotationType {
        NORMAL,
        RANDOM,
        NONE
    }

    public enum BlockMode {
        NCP,
        NCP_INTERACT,
        FAKE
    }

    public enum UnBlockMode {
        SWING,
        ATTACK
    }
}

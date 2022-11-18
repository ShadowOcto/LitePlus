package me.rhys.client.module.player.breaker;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.entity.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Breaker extends Module {
    public Breaker(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    private BlockPos globalPos;

    public static TimeHelper timer = new TimeHelper();

    @Name("Range")
    @Clamp(min = 3, max =  6)
    public float range = 4.0f;
    @Name("EnderStone")
    public boolean EnderStone = false;
    @Name("Bed")
    public boolean Bed = true;
    @Name("Cake")
    public boolean Cake = false;
    @Name("Egg")
    public boolean Egg = false;
    @Name("Chest")
    public boolean Chest = false;
    @Name("Ore")
    public boolean Ore = false;

    @EventTarget()
    private void onUpdatePre(final PlayerUpdateEvent event) {
        if (event.isCancelled()) {
            globalPos = null;
            return;
        }

        globalPos = null;

        if (event.isCancelled() || mc.thePlayer.ticksExisted % 20 == 0 || mc.currentScreen instanceof GuiContainer) {
            return;
        }

        float radius;
        float y = radius = range + 2;
        while (y >= -radius) {
            float x = -radius;
            while (x <= radius) {
                float z = -radius;
                while (z <= radius) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5 + (double) x, mc.thePlayer.posY - 0.5 + (double) y, mc.thePlayer.posZ - 0.5 + (double) z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (this.getFacingDirection(pos) != null && mc.thePlayer.getDistance(mc.thePlayer.posX + (double) x, mc.thePlayer.posY + (double) y, mc.thePlayer.posZ + (double) z) < (double) mc.playerController.getBlockReachDistance() && isValidBlock(block)) {
                        float[] rotations = BlockUtil.getBlockRotations(pos.getX(), pos.getY(), pos.getZ());
                        this.globalPos = pos;
                        return;
                    }
                    ++z;
                }
                ++x;
            }
            --y;
        }

    }

    @EventTarget
    public void onUpdatePost(PlayerUpdateEvent event) {
        if (this.globalPos != null && !(mc.currentScreen instanceof GuiContainer)) {
            EnumFacing direction;
            if ((direction = this.getFacingDirection(this.globalPos)) != null) {
            }
            if (timer.isDelayComplete(500)) {
                //destroy!
                mc.thePlayer.sendQueue.addToSendQueue(
                        new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                mc.thePlayer.sendQueue
                        .addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                mc.thePlayer.sendQueue.addToSendQueue(
                        new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                mc.thePlayer.swingItem();
                timer.reset();
            }
        }
    }

    private boolean isValidBlock(Block block) {

        if (Block.getIdFromBlock(block) == 121 && EnderStone)
            return true;

        if (Block.getIdFromBlock(block) == 26 && Bed)
            return true;

        if (block instanceof BlockDragonEgg && Egg)
            return true;

        if (block instanceof BlockCake && Cake)
            return true;

        if (block instanceof BlockOre && Ore)
            return true;

        if (block instanceof BlockChest && Chest)
            return true;

        return false;
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.UP;
        } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.DOWN;
        } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.EAST;
        } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.WEST;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }
}
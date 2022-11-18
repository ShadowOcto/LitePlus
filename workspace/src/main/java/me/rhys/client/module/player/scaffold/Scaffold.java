package me.rhys.client.module.player.scaffold;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.SafeWalkEvent;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.base.util.vec.Vec3f;
import me.rhys.client.module.player.scaffold.modes.Expand;
import me.rhys.client.module.player.scaffold.modes.NCP;
import me.rhys.client.module.player.scaffold.modes.Verus;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Scaffold extends Module {
    public Scaffold(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(
                new NCP("NCP", this),
                new Verus("Verus", this),
                new Expand("Expand", this)
        );
    }

    @Name("Swing")
    public boolean swing = false;

    @Name("Sprint")
    public boolean sprint = true;

    @Name("Tower")
    public boolean tower = true;

    @Name("Show Amount")
    public boolean showAmount = true;

    @Name("SafeWalk")
    public boolean safeWalk = true;

    @Name("Delay")
    @Clamp(min = 0, max = 9000)
    public int delay = 0;

    public final Timer delayTimer = new Timer();

    @Override
    public void onEnable() {
        this.delayTimer.reset();
    }

    @EventTarget
    void onRender(RenderGameOverlayEvent event) {
        if (this.showAmount) {
            int amount = getBlockCount();
            String str = EnumChatFormatting.GRAY +  "Blocks: " + (amount > 60 ? EnumChatFormatting.GREEN
                    : EnumChatFormatting.RED) + amount;

            FontUtil.drawStringWithShadow(str, new Vec2f(
                    (event.getWidth() - FontUtil.typeToFont().getStringWidth(str)) / 2.0F,
                    (event.getHeight() - FontUtil.typeToFont().getHeight()) / 2.0F - 15), -1);
        }
    }

    @EventTarget
    void onSafeWalk(SafeWalkEvent event) {
        event.setCancelled(this.safeWalk);
    }

    public BlockEntry findExpand(Vec3 offset3, int expand) {
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector().add(offset3))
                .offset(EnumFacing.DOWN);
        if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir))
            return null;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir ||
                    !rayTrace(mc.thePlayer.getLook(0.0f),
                            this.getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockEntry(offset, invert[facing.ordinal()]);
        }
        for (int i = 0; i < expand; i++) {
            BlockPos[] offsets = new BlockPos[]{new BlockPos(-i, 0, 0), new BlockPos(i, 0, 0),
                    new BlockPos(0, 0, -i), new BlockPos(0, 0, i)};
            for (BlockPos offset : offsets) {
                BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
                if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
                for (EnumFacing facing : EnumFacing.values()) {
                    BlockPos offset2 = offsetPos.offset(facing);
                    if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir ||
                            !rayTrace(mc.thePlayer.getLook(0.0f),
                                    this.getPositionByFace(offset, invert[facing.ordinal()])))
                        continue;
                    return new BlockEntry(offset2, invert[facing.ordinal()]);
                }
            }
        }
        return null;
    }

    public boolean placeBlock(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
        if (this.delayTimer.hasReached(this.delay)) {
            this.delayTimer.reset();

            BlockPos offset = blockPos.offset(facing);
            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                    EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            if (rayTrace(mc.thePlayer.getLook(0.0f), this.getPositionByFace(offset,
                    invert[facing.ordinal()]))) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
                Vec3f hitVec = (new Vec3f(blockPos)).add(0.5f, 0.5f, 0.5f)
                        .add((new Vec3f(facing.getDirectionVec())).scale(0.5f));
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack,
                        blockPos, facing, new Vec3(hitVec.x, hitVec.y, hitVec.z))) {
                    if (swing) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPlaceable(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            Block block = (((ItemBlock) itemStack.getItem()).getBlock());
            return !(block instanceof BlockNote) && !(block instanceof BlockFurnace)
                    && !block.getLocalizedName().equalsIgnoreCase("Crafting Table")
                    && !(block instanceof BlockWeb) && !(block instanceof BlockFence)
                    && !(block instanceof BlockFenceGate)
                    && !(block instanceof BlockSlab) && !(block instanceof BlockStairs);
        }
        return true;
    }

    public int getSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (!isPlaceable(itemStack)) continue;
            if (itemStack != null && (itemStack.getItem() instanceof ItemAnvilBlock
                    || (itemStack.getItem() instanceof ItemBlock
                    && ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockSand))) {
                continue;
            }
            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)
                    || (((ItemBlock) itemStack.getItem()).getBlock().maxY -
                    ((ItemBlock) itemStack.getItem()).getBlock().minY != 1) && !(itemStack.getItem() instanceof ItemAnvilBlock)) {
                continue;
            }
            return i;
        }

        return -1;
    }

    public int getBlockCount() {
        AtomicInteger count = new AtomicInteger(0);
        player().inventoryContainer.getInventory().stream()
                .filter(Objects::nonNull).filter(itemStack ->
                (itemStack.getItem() instanceof ItemBlock)
                        && isPlaceable(itemStack)).forEach(itemStack ->
                count.addAndGet(itemStack.stackSize));
        return count.get();
    }

    public boolean placeBlockVerus(BlockPos blockPos, EnumFacing facing, int slot, boolean swing) {
        if (this.delayTimer.hasReached(this.delay)) {
            this.delayTimer.reset();

            BlockPos offset = blockPos.offset(facing);
            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH,
                    EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            if (rayTrace(mc.thePlayer.getLook(0.0f), this.getPositionByFace(offset,
                    invert[facing.ordinal()]))) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

                float f = MathUtil.randFloat(.3f, .5f);

                Vec3f hitVec = (new Vec3f(blockPos)).add(f, f, f)
                        .add((new Vec3f(facing.getDirectionVec())).scale(f));
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                        stack, blockPos, facing, new Vec3(hitVec.x, hitVec.y, hitVec.z))) {
                    if (swing) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double)
                facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.75,
                (double) position.getZ() + 0.5);
        return point.add(offset);
    }

    public BlockEntry find(Vec3 offset3) {
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(mc.thePlayer.getPositionVector().add(offset3)).offset(EnumFacing.DOWN);
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset = position.offset(facing);
            if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir
                    || !rayTrace(mc.thePlayer.getLook(0.0f),
                    getPositionByFace(offset, invert[facing.ordinal()])))
                continue;
            return new BlockEntry(offset, invert[facing.ordinal()]);
        }
        BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0),
                new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
        for (BlockPos offset : offsets) {
            BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
            if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) continue;
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset2 = offsetPos.offset(facing);
                if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir
                        || !rayTrace(mc.thePlayer.getLook(0.0f),
                        getPositionByFace(offset, invert[facing.ordinal()])))
                    continue;
                return new BlockEntry(offset2, invert[facing.ordinal()]);
            }
        }
        return null;
    }

    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) continue;
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition).isVecInside(point)) continue;
            return false;
        }
        return true;
    }

    public class BlockEntry {

        private final BlockPos position;
        private final EnumFacing facing;

        BlockEntry(BlockPos position, EnumFacing facing) {
            this.position = position;
            this.facing = facing;
        }

        public BlockPos getPosition() {
            return this.position;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }
    }
}

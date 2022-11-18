package me.rhys.client.module.player.nofall.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.nofall.NoFall;
import net.minecraft.util.AxisAlignedBB;

public class Glitch extends ModuleMode<NoFall> {
    public Glitch(String name, NoFall parent) {
        super(name, parent);
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE && mc.thePlayer.fallDistance > 3) {

            mc.thePlayer.fallDistance = 3;
            event.setOnGround(true);

            double toTeleport = this.findGroundLevel() + 0.05;
            double max = mc.thePlayer.posY - toTeleport;

            if (max > 0) {
                for (double yPos = mc.thePlayer.posY; yPos > max; yPos -= 8) {

                    if (yPos < max) {
                        yPos = max;
                    }

                    event.getPosition().setY(yPos - 1);
                }
            }
        }
    }

    double findGroundLevel() {
        double boxOffset = .0625;

        AxisAlignedBB axisAlignedBB = mc.thePlayer.getEntityBoundingBox().expand(
                boxOffset,
                boxOffset,
                boxOffset
        );

        double current = 1;
        double height;

        for (height = 0; height < mc.thePlayer.posY; height += current) {
            AxisAlignedBB nextLocation = axisAlignedBB.offset(.0, -height, .0);
            if (!mc.theWorld.checkBlockCollision(nextLocation)) continue;

            if (current < boxOffset) {
                break;
            }

            height -= current;
            current /= 2.0;
        }

        return height;
    }
}

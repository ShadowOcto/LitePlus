package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C0CPacketInput;

public class C0C extends ModuleMode<Disabler> {
    public C0C(String name, Disabler parent) {
        super(name, parent);
    }

    @Name("Custom Values")
    public boolean customValues = false;

    @Name("Strafe Value")
    @Clamp(min = 0, max = 9)
    public double strafeValue = 1.20;

    @Name("Forward Value")
    @Clamp(min = 0, max = 9)
    public double forwardValue = 1.20;

    @Name("Min Value")
    public boolean minValues = false;

    @Name("Jumping")
    public boolean jumping = true;

    @Name("Sneaking")
    public boolean sneaking = true;

    @Name("Custom Tick")
    public boolean customTick;

    @Name("Send Tick")
    @Clamp(min = 1, max = 500)
    public int customSendTick = 20;

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (event.getType() == Event.Type.PRE) {

            if (this.customTick && mc.thePlayer.ticksExisted % this.customSendTick != 0) {
                return;
            }

            float x = this.customValues ? (float) this.strafeValue : (this.minValues
                    ? Float.MIN_VALUE : Float.MAX_VALUE);

            float z = this.customValues ? (float) this.forwardValue : (this.minValues
                    ? Float.MIN_VALUE : Float.MAX_VALUE);

            mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(x, z, this.jumping, this.sneaking));
        }
    }
}

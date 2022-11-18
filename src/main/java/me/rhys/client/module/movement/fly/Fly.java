package me.rhys.client.module.movement.fly;

import me.rhys.client.module.movement.fly.modes.*;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class Fly extends Module {

    @Name("View Bobbing")
    public boolean viewBobbing = true;

    public Fly(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);

        add(
                new Vanilla("Vanilla", this),
                new Verus("Verus", this),
                new Packet("Packet", this),
                new Creative("Creative", this),
                new Notation("Notation", this)
        );
    }


    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.viewBobbing) {
            mc.thePlayer.cameraYaw = 0.099999376f;
        }
    }
}

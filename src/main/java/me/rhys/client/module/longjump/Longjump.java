package me.rhys.client.module.longjump;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.longjump.modes.BlocksMC;
public class Longjump extends Module {

    @Name("View Bobbing")
    public boolean viewBobbing = true;

    public Longjump(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);

        add(
                new BlocksMC("BlocksMC", this)
        );
    }

    @EventTarget
    void onMotion(PlayerMotionEvent event) {
        if (this.viewBobbing) {
            mc.thePlayer.cameraYaw = 0.099999376f;
        }
    }
}
package me.rhys.client.module.player;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class Timer extends Module {

    @Name("Speed")
    @Clamp(min = 0.05, max = 50)
    public double timerSpeed = 1.5F;

    public Timer(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    void playerUpdate(PlayerUpdateEvent event) {
        mc.timer.timerSpeed = (float) this.timerSpeed;
    }
}
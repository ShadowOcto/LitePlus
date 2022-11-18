package me.rhys.client.module.ghost;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;

public class Reach extends Module {

    @Name("Reach")
    @Clamp(min = 0.0D, max = 6.0D)
    public static double reach = 3.0D;

    public static boolean enabled = false;

    public Reach(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @Override
    public void onEnable() {
        enabled = true;
        super.onDisable();
    }

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }

    @EventTarget
    public void packetReceive(PacketEvent event) {

    }
}
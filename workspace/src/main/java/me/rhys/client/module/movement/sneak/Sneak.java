package me.rhys.client.module.movement.sneak;

import me.rhys.client.module.movement.sneak.modes.Packet;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class Sneak extends Module {
    public Sneak(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(new Packet("Packet", this));
    }
}

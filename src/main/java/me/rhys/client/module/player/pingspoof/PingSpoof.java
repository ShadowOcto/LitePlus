package me.rhys.client.module.player.pingspoof;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.pingspoof.mode.Delay;
import me.rhys.client.module.player.pingspoof.mode.Key;

public class PingSpoof extends Module {
    public PingSpoof(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(
                new Key("Key", this),
                new Delay("Delay", this)
        );
    }
}

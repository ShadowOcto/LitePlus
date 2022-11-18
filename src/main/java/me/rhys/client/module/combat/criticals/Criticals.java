package me.rhys.client.module.combat.criticals;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.combat.criticals.modes.Packet;

public class Criticals extends Module {

    public Packet packet;

    public Criticals(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        add(
                packet = new Packet("Packet", this)
        );
    }

    public void processCriticalHit() {
        switch (getCurrentMode().getName()) {
            case "Packet": {
                this.packet.doCriticalHit();
                break;
            }
        }
    }
}

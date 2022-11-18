package me.rhys.client.module.ghost.autoclicker;

import me.rhys.client.module.ghost.autoclicker.modes.Normal;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class AutoClicker extends Module {

    public AutoClicker(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);

        add(new Normal("Normal", this));
    }


}

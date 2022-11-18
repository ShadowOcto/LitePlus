package me.rhys.client.module.render;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class Cape extends Module {
    public Cape(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    @Name("Cape")
    public logo mode = logo.BASIC1;
    //Edit LayerCapes.class to add and change capes (Line 30)
    public enum logo {BASIC1, BASIC2}

}
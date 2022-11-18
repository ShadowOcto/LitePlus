package me.rhys.client.module.render.animations;

import me.rhys.client.module.render.animations.modes.*;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

/**
 * Created on 07/09/2020 Package me.rhys.client.module.render.animations
 */
public class Animations extends Module {
    @Name("Old 1.7 Animations")
    public static boolean oldAnimations = false;
    @Name("No Hit Delay Fix")
    public static boolean delayFix = false;
    @Name("Equip Reset")
    public boolean equipReset = true;
    @Name("Smooth swing")
    public boolean smoothSwing = false;
    @Name("Swing Type")
    public SwingType swingType = SwingType.SMOOTH;

    public Animations(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);

        add(
                new OneDotSeven("1.7", this),
                new Exhibition("Exhibition", this),
                new Slide("Slide", this),
                new Up("Up", this),
                new Dev("Dev", this));
    }

    public enum SwingType {
        GERMAN,
        SMOOTH
    }
}

package me.rhys.client.ui.click.element.dropdown;

import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.ui.element.button.DropDownButton;
import me.rhys.base.util.vec.Vec2f;

import java.util.Arrays;
import java.util.Comparator;

public class EnumDropDown extends DropDownButton {

    private final EnumSetting setting;

    public EnumDropDown(EnumSetting setting, Vec2f offset, int width, int height) {
        super(offset, width, height, setting.get());
        this.setting = setting;

        Arrays.stream(setting.values()).filter(value -> !value.equalsIgnoreCase(setting.get())).sorted(Comparator.comparingInt(value -> (int) value.charAt(0))).forEachOrdered(items::add);
    }

    @Override
    public void setCurrent(String current) {
        if (items.contains(current)) {
            this.label = current;
            this.current = items.indexOf(current);

            setting.set(label);
        }
    }

}

package me.rhys.client.ui.click.element.slider;

import lombok.Getter;
import me.rhys.base.module.setting.impl.number.NumberSetting;
import me.rhys.base.ui.element.slider.Slider;
import me.rhys.base.util.vec.Vec2f;

@Getter
public class SettingSlider<T extends NumberSetting> extends Slider {

    protected final T setting;

    public SettingSlider(T numberSetting, Vec2f offset, int width, int height) {
        super(offset, width, height, numberSetting.getMin(), numberSetting.getMax(), 0);
        this.setting = numberSetting;
    }

}

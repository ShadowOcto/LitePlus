package me.rhys.client.ui.click.element.slider;

import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.vec.Vec2f;

public class IntSettingSlider extends SettingSlider<IntNumberSetting> {

    public IntSettingSlider(IntNumberSetting numberSetting, Vec2f offset, int width, int height) {
        super(numberSetting, offset, width, height);

        this.current = numberSetting.get();
    }

    @Override
    public void dragMouse(Vec2f pos, int button, long lastClickTime) {
        double offset = pos.clone().sub(this.pos.x, this.pos.y).x / (double) width;

        setting.set((int) Math.max(min, Math.min(max, MathUtil.round((max * offset), 1))));

        current = setting.get();
    }

}

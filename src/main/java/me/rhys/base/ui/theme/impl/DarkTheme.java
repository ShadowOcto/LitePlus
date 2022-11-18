package me.rhys.base.ui.theme.impl;

import me.rhys.base.ui.theme.Theme;
import me.rhys.base.util.render.ColorUtil;

public class DarkTheme extends Theme {

    public DarkTheme() {
        super(
                new WindowColors(
                        ColorUtil.rgba(45, 52, 54, 0.5f),
                        ColorUtil.rgba(38, 43, 43, 0.5f)
                ),
                new ButtonColors(
                        ColorUtil.rgba(38, 43, 43, 0.7f),
                        ColorUtil.rgba(255, 255, 255, 0.7f)
                ),
                new LabelColors(
                        ColorUtil.rgba(255, 255, 255, 0.7f)
                ),
                new CheckBoxColors(
                        ColorUtil.rgba(38, 43, 43, 0.7f),
                        ColorUtil.rgba(28, 33, 33, 0.7f),
                        ColorUtil.rgba(46, 204, 113, 0.7f)
                ),
                new SliderColors(
                        ColorUtil.rgba(38, 43, 43, 0.7f),
                        ColorUtil.rgba(46, 204, 113, 0.7f)
                )
        );
    }

}

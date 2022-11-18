package me.rhys.base.ui.theme;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Theme {

    public WindowColors windowColors;
    public ButtonColors buttonColors;
    public LabelColors labelColors;
    public CheckBoxColors checkBoxColors;
    public SliderColors sliderColors;

    @AllArgsConstructor
    public static final class WindowColors {

        public int background;
        public int border;

    }

    @AllArgsConstructor
    public static final class ButtonColors {

        public int background;
        public int text;

    }

    @AllArgsConstructor
    public static final class LabelColors {

        public int text;

    }

    @AllArgsConstructor
    public static final class CheckBoxColors {

        public int background;
        public int border;
        public int active;

    }

    @AllArgsConstructor
    public static final class SliderColors {

        public int background;
        public int fill;

    }

}

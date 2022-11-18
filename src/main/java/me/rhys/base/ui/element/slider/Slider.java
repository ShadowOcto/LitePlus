package me.rhys.base.ui.element.slider;

import lombok.Getter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

@Getter
public class Slider extends Element {

    protected double min;
    protected double max;
    protected double current;

    public Slider(Vec2f offset, int width, int height, double min, double max, double current) {
        super(offset, width, height);
        this.min = min;
        this.max = max;
        this.current = current;
    }

    @Override
    public void dragMouse(Vec2f pos, int button, long lastClickTime) {
        double offset = pos.clone().sub(this.pos.x, this.pos.y).x / (double) width;

        current = Math.max(min, Math.min(max, MathUtil.round((max * offset), 1)));
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        RenderUtil.drawRect(pos.clone().sub(1, 1), width + 2, height + 2, ColorUtil.darken(screen.theme.sliderColors.background, 15).getRGB());
        RenderUtil.drawRect(pos, width, height, screen.theme.sliderColors.background);

        double fill = current / max;

        RenderUtil.drawRect(pos, (int) (width * fill), height, screen.theme.sliderColors.fill);

        FontUtil.drawCenteredStringWithShadow(String.valueOf(current), pos.clone().add((float) (width * fill), height / 2.0f), -1);
    }

}

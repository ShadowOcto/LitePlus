package me.rhys.base.ui.element.button;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.FlagCallback;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

@Setter
@Getter
public class CheckBox extends Button {

    private boolean checked;
    private FlagCallback callback;

    public CheckBox(String label, Vec2f offset, int width, int height) {
        super(label, offset, width, height);
        this.checked = false;
        this.callback = null;
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        if (callback != null) {
            checked = callback.handle(pos);
        } else {
            checked = !checked;
        }
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        // draw the background
        if (background == ColorUtil.Colors.TRANSPARENT.getColor()) {
            RenderUtil.drawRect(pos.clone().sub(1, 1), width + 2, height + 2, screen.theme.checkBoxColors.border);
            RenderUtil.drawRect(pos, width, height, screen.theme.checkBoxColors.background);
        }

        if (checked) {
            RenderUtil.drawRect(pos, width, height, screen.theme.checkBoxColors.active);
        }

        // draw the label
        FontUtil.drawStringWithShadow(label, pos.clone().add(width + 5, (height - FontUtil.getFontHeight()) / 2.0f), screen.theme.buttonColors.text);
    }

}

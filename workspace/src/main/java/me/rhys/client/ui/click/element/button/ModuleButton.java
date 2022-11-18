package me.rhys.client.ui.click.element.button;

import lombok.Getter;
import me.rhys.base.module.Module;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class ModuleButton extends Button {

    @Getter
    private final Module module;

    public ModuleButton(Module module, Vec2f offset, int width, int height) {
        super(module.getData().getName(), offset, width, height);
        this.module = module;
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        if (button == 0) {
            module.toggle();
        }
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        super.draw(mouse, partialTicks);

        RenderUtil.drawRect(pos, 1, height, module.getData().isEnabled() ? ColorUtil.rgba(0, 255, 0, 1.0f) : ColorUtil.rgba(255, 0, 0, 1.0f));
    }
}

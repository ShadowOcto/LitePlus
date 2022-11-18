package me.rhys.base.ui.element.label;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;

@Setter
@Getter
public class Label extends Element {

    private String label;
    private float scale;
    private boolean centered;

    public Label(String label, Vec2f offset) {
        super(offset, (int) FontUtil.getStringWidth(label), (int) FontUtil.getFontHeight());
        this.label = label;
        this.scale = 1.0f;
        this.centered = false;
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        if (scale != 1.0f) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
        }

        if (centered) {
            if (scale != 1.0f) {
                FontUtil.drawCenteredStringWithShadow(label, pos.div(scale, scale), getScreen().theme.labelColors.text);
            } else {
                FontUtil.drawCenteredStringWithShadow(label, pos, getScreen().theme.labelColors.text);
            }
        } else {
            if (scale != 1.0f) {
                FontUtil.drawStringWithShadow(label, pos.div(scale, scale), getScreen().theme.labelColors.text);
            } else {
                FontUtil.drawStringWithShadow(label, pos, getScreen().theme.labelColors.text);
            }
        }


        if (scale != 1.0f) {
            GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            GlStateManager.popMatrix();
        }
    }

}

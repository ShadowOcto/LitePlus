package me.rhys.base.ui.element.panel;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Setter
@Getter
public class ScrollPanel extends Panel {

    private float scrollAmount;
    private float itemMargin;

    public ScrollPanel(Vec2f offset, int width, int height) {
        super(offset, width, height);
        this.scrollAmount = 0.0f;
        this.itemMargin = 0.0f;
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        container.filter(element -> element.isHovered(pos.clone().add(0, scrollAmount))).forEach(element -> element.clickMouse(pos.clone().add(0, scrollAmount), button));
    }

    @Override
    public void dragMouse(Vec2f pos, int button, long lastClickTime) {
        container.filter(element -> element.isHovered(pos.clone().add(0, scrollAmount))).forEach(element -> element.dragMouse(pos.clone().add(0, scrollAmount), button, lastClickTime));
    }

    @Override
    public void releaseMouse(Vec2f pos, int button) {
        container.filter(element -> element.isHovered(pos.clone().add(0, scrollAmount))).forEach(element -> element.releaseMouse(pos.clone().add(0, scrollAmount), button));
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        // align all the items before drawing
        float maxItemHeight = alignItems();

        // check if scrolling is enabled
        boolean canScroll = maxItemHeight > height;

        float maxScroll = maxItemHeight - height;

        if (canScroll) {
            // handle scrolling
            if (Mouse.hasWheel() && isHovered(mouse)) {
                int scrollAmount = Mouse.getDWheel();
                if (scrollAmount != 0) {
                    if (scrollAmount > 0) {
                        scrollAmount = -1;
                    } else {
                        scrollAmount = 1;
                    }
                    this.scrollAmount += scrollAmount * 15;
                    this.scrollAmount = Math.max(0, Math.min(maxScroll, this.scrollAmount));
                }
            }

            // enable clipping
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);

            // clip the rect
            RenderUtil.clipRect(pos, pos.clone().add(width, height), getScreen().getScale());

            // translate the items
            GlStateManager.translate(0, -scrollAmount, 0);
        }

        // draw all the items
        container.forEach(element -> element._draw(mouse.clone().add(0, scrollAmount), partialTicks));

        if (canScroll) {
            // disable clipping
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }
    }

    private float alignItems() {
        AtomicDouble yOffset = new AtomicDouble(0);
        container.forEach(element -> element.offset = new Vec2f(itemMargin / 2f, itemMargin / 2f + (float) yOffset.getAndAdd(element.getHeight() + itemMargin)));
        return (float) yOffset.get();
    }

}

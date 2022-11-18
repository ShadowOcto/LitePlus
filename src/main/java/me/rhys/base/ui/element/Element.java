package me.rhys.base.ui.element;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.FlagCallback;
import me.rhys.base.ui.UIElement;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class Element implements UIElement {

    @Setter
    @Getter
    private UIScreen screen;

    @Setter
    @Getter
    private Element parent;

    public Vec2f offset;

    @Getter
    public Vec2f pos;
    public int background;
    public FlagCallback callback;

    public boolean movable;

    @Setter
    @Getter
    protected int width;

    @Setter
    @Getter
    protected int height;

    public Element(Vec2f offset, int width, int height) {
        this.offset = offset;
        this.pos = offset;
        this.width = width;
        this.height = height;
        this.movable = false;
        this.background = ColorUtil.Colors.TRANSPARENT.getColor();
        this.callback = null;
    }

    public void _draw(Vec2f mouse, float partialTicks) {
        // get the current start position
        Vec2f parent = (this.parent == null ? new Vec2f(0, 0) : this.parent.pos);

        // add the new offset to get the real position
        pos = parent.clone().add(offset.x, offset.y);

        // if the background is not transparent draw it
        if (!ColorUtil.isTransparent(background)) {
            RenderUtil.drawRect(pos, width, height, background);
        }

        // call the front end draw
        draw(mouse, partialTicks);
    }

    public boolean isHovered(Vec2f mouse) {
        return (mouse.x >= pos.x && mouse.y >= pos.y && mouse.x <= pos.x + width && mouse.y <= pos.y + height);
    }


}

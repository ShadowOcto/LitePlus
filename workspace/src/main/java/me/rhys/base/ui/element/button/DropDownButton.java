package me.rhys.base.ui.element.button;

import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DropDownButton extends Button {

    protected final List<String> items;

    private boolean expanded;
    protected int current;
    private int expandHeight;

    public DropDownButton(Vec2f offset, int width, int height, String... itms) {
        super(itms[0], offset, width, height);
        this.items = new ArrayList<>();

        Arrays.stream(itms).sorted(Comparator.comparingInt(value -> (int) value.charAt(0))).forEach(items::add);

        this.expanded = false;
        this.current = 0;
        this.expandHeight = 105;
    }

    public DropDownButton(Vec2f offset, int width, int height, List<String> items) {
        super(items.get(0), offset, width, height);
        this.items = items;

        items.sort(Comparator.comparingInt(value -> (int) value.charAt(0)));

        this.expanded = false;
        this.current = 0;
        this.expandHeight = 100;
    }

    public void setCurrent(String current) {
        if (items.contains(current)) {
            this.label = current;
            this.current = items.indexOf(current);
        }
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        if ((pos.x >= this.pos.x
                && pos.y >= this.pos.y
                && pos.x <= this.pos.x + width
                && pos.y <= this.pos.y + (height + (expanded ? expandHeight + 1 : 0)))
                && pos.y >= this.pos.y + height && expanded) {
            Vec2f expandPos = this.pos.clone().add(0, height + 1);

            List<String> cleaned = new ArrayList<>();

            items.stream().filter(s -> !s.equalsIgnoreCase(label)).sorted(Comparator.comparingInt(value -> (int) value.charAt(0))).forEachOrdered(cleaned::add);

            float yOffset = 5;
            for (String item : cleaned) {
                Vec2f itemPos = expandPos.clone().add(10, yOffset);

                if (pos.x >= itemPos.x
                        && pos.y >= itemPos.y
                        && pos.x <= (itemPos.x + FontUtil.getStringWidth(item))
                        && pos.y <= (itemPos.y + FontUtil.getFontHeight())) {

                    setCurrent(item);
                    break;
                }

                yOffset += FontUtil.getFontHeight() + 5;
            }
        } else {
            if (button == 0 && items.size() > 1) {
                expanded = !expanded;
            }
        }
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        // draw the background
        if (background == ColorUtil.Colors.TRANSPARENT.getColor()) {
            RenderUtil.drawRect(pos.clone().sub(1, 1), width + 2, height + 2, ColorUtil.darken(screen.theme.buttonColors.background, 10).getRGB());
            RenderUtil.drawRect(pos, width, height, screen.theme.buttonColors.background);
            RenderUtil.drawRect(pos.clone().add(width - FontUtil.getStringWidth("☰") - 15, 0), (int) (FontUtil.getStringWidth("☰") + 15), height, screen.theme.buttonColors.background);
        }

        // draw the label
        FontUtil.drawStringWithShadow(String.valueOf(label.charAt(0)).toUpperCase() + label.substring(1).toLowerCase(), pos.clone().add(10, (height - FontUtil.getFontHeight()) / 2.0f), screen.theme.buttonColors.text);

        FontUtil.drawStringWithShadow("☰", pos.clone().add(width - FontUtil.getStringWidth("☰") - 10, (height - FontUtil.getFontHeight()) / 2.0f - 1), -1);

        expandHeight = (int) ((items.size() - 1) * (FontUtil.getFontHeight() + 5) + 5);

        if (expanded) {
            Vec2f expandPos = pos.clone().add(0, height + 1);

            RenderUtil.drawRect(expandPos, width, expandHeight, screen.theme.buttonColors.background);

            List<String> cleaned = new ArrayList<>();

            items.stream().filter(s -> !s.equalsIgnoreCase(label)).sorted(Comparator.comparingInt(value -> (int) value.charAt(0))).forEachOrdered(cleaned::add);

            float yOffset = 5;
            for (String item : cleaned) {
                FontUtil.drawStringWithShadow(String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1).toLowerCase(), expandPos.clone().add(10, yOffset), -1);

                yOffset += FontUtil.getFontHeight() + 5;
            }

        }
    }

    @Override
    public boolean isHovered(Vec2f mouse) {
        return (mouse.x >= pos.x && mouse.y >= pos.y && mouse.x <= pos.x + width && mouse.y <= pos.y + (height + (expanded ? expandHeight + 1 : 0)));
    }

    @Override
    public int getHeight() {
        return height + (expanded ? expandHeight + 1 : 0);
    }

}

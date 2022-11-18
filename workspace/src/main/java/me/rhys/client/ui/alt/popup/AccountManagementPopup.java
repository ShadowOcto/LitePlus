package me.rhys.client.ui.alt.popup;

import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class AccountManagementPopup extends Popup {

    private static final int MARGIN = 5;

    private Button addBtn;
    private Button clearBtn;
    private Button closeBtn;

    public AccountManagementPopup(int width, int height) {
        super("Manage Accounts", width, height);
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        super.clickMouse(pos, button);

        if (button == 0) {
            if (addBtn.isHovered(pos)) {
                getScreen().displayPopup(new AddAccountPopup(220, 115));
                addBtn.playSound();
            } else if (clearBtn.isHovered(pos)) {
                clearBtn.playSound();
            } else if (closeBtn.isHovered(pos)) {
                getScreen().displayPopup(null);
                closeBtn.playSound();
            }
        }
    }

    @Override
    public void onShow() {
        super.onShow();

        addToBody(addBtn = new Button("Add", new Vec2f(MARGIN, MARGIN), width - MARGIN * 2, 20));
        addBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();

        addToBody(clearBtn = new Button("Clear", new Vec2f(MARGIN, MARGIN + (20 + MARGIN)), width - MARGIN * 2, 20));
        clearBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();

        addToBody(closeBtn = new Button("Close", new Vec2f(MARGIN, MARGIN + (20 + MARGIN) * 2), width - MARGIN * 2, 20));
        closeBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    }

    @Override
    public void onDraw() {
        GlStateManager.pushMatrix();

        GL11.glLineWidth(2.5f);

        RenderUtil.drawOutlineRect(addBtn.pos.clone().sub(0, 1), addBtn.pos.clone().add(addBtn.getWidth(), addBtn.getHeight()), SHADOW_COLOR);
        RenderUtil.drawOutlineRect(clearBtn.pos.clone().sub(0, 1), clearBtn.pos.clone().add(clearBtn.getWidth(), clearBtn.getHeight()), SHADOW_COLOR);
        RenderUtil.drawOutlineRect(closeBtn.pos.clone().sub(0, 1), closeBtn.pos.clone().add(closeBtn.getWidth(), closeBtn.getHeight()), SHADOW_COLOR);

        GL11.glLineWidth(1f);

        GlStateManager.popMatrix();
    }

}

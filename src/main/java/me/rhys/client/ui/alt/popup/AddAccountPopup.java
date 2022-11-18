package me.rhys.client.ui.alt.popup;

import me.rhys.client.Manager;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.element.input.TextInputField;
import me.rhys.base.ui.element.label.Label;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class AddAccountPopup extends Popup {

    private TextInputField emailField;
    private TextInputField passwordField;

    private Button closeBtn;
    private Button addBtn;

    public AddAccountPopup(int width, int height) {
        super("Add Account", width, height);
    }

    @Override
    public void onShow() {
        super.onShow();

        addToBody(new Label("Email", new Vec2f(5, 5)));
        addToBody(emailField = new TextInputField(new Vec2f(5, FontUtil.getFontHeight() + 10), width - 11, 15));

        addToBody(new Label("Password", new Vec2f(5, FontUtil.getFontHeight() * 2 + 20)));
        addToBody(passwordField = new TextInputField(new Vec2f(5, FontUtil.getFontHeight() * 3 + 25), width - 11, 15));
        passwordField.setIsPassword(true);

        addToBody(closeBtn = new Button("Close", new Vec2f(5, FontUtil.getFontHeight() * 3 + 46), (int) ((width - 15) / 2.0f), 17));
        closeBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();

        addToBody(addBtn = new Button("Add", new Vec2f(((width - 15) / 2.0f) + 10, FontUtil.getFontHeight() * 3 + 46), (int) ((width - 15) / 2.0f), 17));
        addBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        emailField.clickMouse(pos, button);
        passwordField.clickMouse(pos, button);

        if (addBtn.isHovered(pos)) {
            if (!emailField.getText().isEmpty()
                    && !passwordField.getText().isEmpty()) {
                Manager.UI.ALT.addAccount(emailField.getText(), passwordField.getText());
                getScreen().displayPopup(null);
            }
        } else if (closeBtn.isHovered(pos)) {
            getScreen().displayPopup(null);
            closeBtn.playSound();
        }
    }

    @Override
    public void onDraw() {
        GlStateManager.pushMatrix();

        GL11.glLineWidth(2.5f);

        RenderUtil.drawOutlineRect(closeBtn.pos, closeBtn.pos.clone().add(closeBtn.getWidth(), closeBtn.getHeight()), SHADOW_COLOR);
        RenderUtil.drawOutlineRect(addBtn.pos, addBtn.pos.clone().add(addBtn.getWidth(), addBtn.getHeight()), SHADOW_COLOR);

        GL11.glLineWidth(1f);

        GlStateManager.popMatrix();
    }

}

package me.rhys.base.ui.element.input;

import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.mc.TextField;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class TextInputField extends Element {

    private static final int BACKGROUND_COLOR = ColorUtil.darken(new Color(27, 34, 44, 255).getRGB(), 10).getRGB();
    private static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 25).getRGB();

    private final CustomTextField textField;

    public TextInputField(Vec2f offset, int width, int height) {
        super(offset, width, height);
        this.textField = new CustomTextField(0, 0, width, height);
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        textField.mouseClicked((int) pos.x, (int) pos.y, button);
    }

    @Override
    public void typeKey(char keyChar, int keyCode) {
        textField.textboxKeyTyped(keyChar, keyCode);
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        // update the position
        textField.xPosition = (int) pos.x;
        textField.yPosition = (int) pos.y;

        // update the size
        textField.setWidth(width);
        textField.setHeight(height);

        RenderUtil.drawRect(pos.clone().sub(1, 1), width + 2, height + 2, SHADOW_COLOR);
        RenderUtil.drawRect(pos, width, height, BACKGROUND_COLOR);

        // draw it
        textField.drawTextBox();
    }

    public void setIsPassword(boolean isPassword) {
        textField.setPassword(isPassword);
    }

    public String getText() {
        return textField.getText();
    }

    private final class CustomTextField extends TextField {

        public CustomTextField(int x, int y, int width, int height) {
            super(0, Minecraft.getMinecraft().fontRendererObj, x, y, width, height);
        }

        /**
         * Draws the textbox
         */
        public void drawTextBox() {
            if (this.getVisible()) {
                StringBuilder stringBuilder = new StringBuilder();

                if (this.isPassword) {
                    for (int i = 0; i < this.text.length(); i++) {
                        stringBuilder.append("*");
                    }
                } else {
                    for (int i = 0; i < this.text.length(); i++) {
                        stringBuilder.append(this.text.charAt(i));
                    }
                }

                int i = this.isEnabled ? this.enabledColor : this.disabledColor;
                int j = this.cursorPosition - this.lineScrollOffset;
                int k = this.selectionEnd - this.lineScrollOffset;
                String s = this.fontRendererInstance.trimStringToWidth(stringBuilder.substring(this.lineScrollOffset), this.getWidth());
                boolean flag = j >= 0 && j <= s.length();
                boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
                int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
                int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
                int j1 = l;

                if (k > s.length()) {
                    k = s.length();
                }

                if (s.length() > 0) {
                    String s1 = flag ? s.substring(0, j) : s;
                    j1 = this.fontRendererInstance.drawStringWithShadow(s1, (float) l, (float) i1, i);
                }

                boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
                int k1 = j1;

                if (!flag) {
                    k1 = j > 0 ? l + this.width : l;
                } else if (flag2) {
                    k1 = j1 - 1;
                    --j1;
                }

                if (s.length() > 0 && flag && j < s.length()) {
                    j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
                }

                if (flag1) {
                    if (flag2) {
                        Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                    } else {
                        this.fontRendererInstance.drawStringWithShadow("_", (float) k1, (float) i1, i);
                    }
                }

                if (k != j) {
                    int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
                    this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
                }
            }
        }
    }
}

package me.rhys.base.ui.element.button;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

@Setter
@Getter
public class Button extends Element {

    protected String label;

    public Button(String label, Vec2f offset, int width, int height) {
        super(offset, width, height);
        this.label = label;
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        // draw the background
        if (background == ColorUtil.Colors.TRANSPARENT.getColor()) {
            RenderUtil.drawRect(pos, width, height, screen.theme.buttonColors.background);
        }

        // draw the label
        FontUtil.drawCenteredStringWithShadow(label, pos.clone().add(width / 2.0f, height / 2.0f), screen.theme.buttonColors.text);
    }

    public void playSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }


}

package me.rhys.client.module.render;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.render.Render2DEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class Infomation extends Module {
    public Infomation(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        setHidden(true);
    }

    @Name("Mode")
    public Mode mode = Mode.NORMAL;

    @Name("FPS")
    public boolean fps = true;

    @Name("Coords")
    public boolean coords = true;

    @Name("BPS")
    public boolean bps = true;

    @EventTarget
    void onRender(Render2DEvent event) {
        HUD hud = (HUD) Lite.MODULE_FACTORY.findByClass(HUD.class);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int distance = 0;
        if (hud.font == HUD.Fonts.MINECRAFT) {
            distance = 3;
        } else {
            distance = 2;
        }
        switch (mode) {
            case NORMAL:
                float y = 0;
                if (fps) {
                    FontUtil.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), new Vec2f(scaledResolution.getScaledWidth() - FontUtil.getStringWidth("FPS: " + Minecraft.getDebugFPS()) - 2, scaledResolution.getScaledHeight() - FontUtil.getFontHeight() - distance), hud.getColor(0, 1));
                }
                if (coords) {
                    FontUtil.drawStringWithShadow("XYZ: " + mc.thePlayer.getPosition().getX() + " " + mc.thePlayer.getPosition().getY() + " " + mc.thePlayer.getPosition().getZ(), new Vec2f(2, scaledResolution.getScaledHeight() - FontUtil.getFontHeight() - distance), hud.getColor(0, 1));
                }
                if (bps) {
                    if (coords) {
                        y = (scaledResolution.getScaledHeight() - FontUtil.getFontHeight() * 2) - distance;
                    } else {
                        y = (scaledResolution.getScaledHeight() - FontUtil.getFontHeight());
                    }
                }
                FontUtil.drawStringWithShadow("BPS: " + Math.round((StrictMath.hypot(this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX, this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * this.mc.timer.timerSpeed * 20.0D)), new Vec2f(2, y - distance), hud.getColor(0, 1));
                break;
            case GRAY:
                float y2 = 0;
                if (fps) {
                    FontUtil.drawStringWithShadow("FPS: " + EnumChatFormatting.GRAY + Minecraft.getDebugFPS(), new Vec2f(scaledResolution.getScaledWidth() - FontUtil.getStringWidth("FPS: " + Minecraft.getDebugFPS()) - 2, scaledResolution.getScaledHeight() - FontUtil.getFontHeight() - distance), hud.getColor(0, 1));
                }
                if (coords) {
                    FontUtil.drawStringWithShadow("XYZ: "  + EnumChatFormatting.GRAY + mc.thePlayer.getPosition().getX() + " " + mc.thePlayer.getPosition().getY() + " " + mc.thePlayer.getPosition().getZ(), new Vec2f(2, scaledResolution.getScaledHeight() - FontUtil.getFontHeight() - distance), hud.getColor(0, 1));
                }
                if (bps) {
                    if (coords) {
                        y2 = (scaledResolution.getScaledHeight() - FontUtil.getFontHeight() * 2) - distance;
                    } else {
                        y2 = (scaledResolution.getScaledHeight() - FontUtil.getFontHeight());
                    }
                }
                FontUtil.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + Math.round((StrictMath.hypot(this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX, this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * this.mc.timer.timerSpeed * 20.0D)), new Vec2f(2, y2 - distance), hud.getColor(0, 1));
                break;
        }
    }
    public enum Mode {
        NORMAL,
        GRAY
    }
}
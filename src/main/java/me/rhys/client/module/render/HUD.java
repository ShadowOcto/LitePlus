package me.rhys.client.module.render;

import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.base.util.vec.Vec4f;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class HUD extends Module {

    @Name("Color Type")
    public ColorMode colorMode = ColorMode.MAIN;

    @Name("Rainbow type")
    public RainbowType rainbowType = RainbowType.NORMAL;

    @Name("Watermark Mode")
    public WatermarkMode wm = WatermarkMode.TEXT;

    @Name("Font")
    public Fonts font = Fonts.APPLE;

    @Name("Fill")
    public boolean fill = true;

    @Name("Line")
    public boolean line = true;

    @Name("Watermark")
    public boolean waterMark = true;

    @Name("ArrayList")
    public boolean arrayList = true;

    @Name("R")
    @Clamp(min = 0, max = 255)
    public int r = 52;

    @Name("G")
    @Clamp(min = 0, max = 255)
    public int g = 152;

    @Name("B")
    @Clamp(min = 0, max = 255)
    public int b = 219;

    @Name("Scoreboard")
    public boolean scoreboard = false;

    @Name("Scoreboard Y")
    @Clamp(min = 0, max = 300)
    public int scoreboardY = 75;

    @Name("Fade To R")
    @Clamp(min = 0, max = 255)
    public int fadeToR = 52;

    @Name("Fade To G")
    @Clamp(min = 0, max = 255)
    public int fadeToG = 152;

    @Name("Fade To B")
    @Clamp(min = 0, max = 255)
    public int fadeToB = 219;

    @Name("Fade Speed")
    @Clamp(min = 0, max = 90)
    public int fadeSpeed = 3;

    public int rCopy = r, gCopy = g, bCopy = b;
    private boolean rBack, gBack, bBack;
    private long lastR, lastG, lastB, nextRandomR, nextRandomG, nextRandomB;

    public HUD(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        setHidden(true);
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {

        if (this.colorMode == ColorMode.RAINBOW && rainbowType == RainbowType.NORMAL) {
            //R

            if ((System.currentTimeMillis() - lastR) > nextRandomR) {
                lastR = System.currentTimeMillis();
                nextRandomR = (long) MathUtil.randFloat(1, 100);
                if (!rBack) {
                    if (rCopy < 255) {
                        rCopy++;
                    } else {
                        rBack = true;
                    }
                }

                if (rBack) {
                    if (rCopy > 0) {
                        rCopy--;
                    } else {
                        rCopy = 1;
                        rBack = false;
                    }
                }
            }

            //G
            if ((System.currentTimeMillis() - lastG) > nextRandomG) {
                lastG = System.currentTimeMillis();
                nextRandomG = (long) MathUtil.randFloat(1, 100);
                if (!gBack) {
                    if (gCopy < 255) {
                        gCopy++;
                    } else {
                        gBack = true;
                    }
                }

                if (gBack) {
                    if (gCopy > 0) {
                        gCopy--;
                    } else {
                        gCopy = 1;
                        gBack = false;
                    }
                }
            }

            //B
            if ((System.currentTimeMillis() - lastB) > nextRandomB) {
                lastB = System.currentTimeMillis();
                nextRandomB = (long) MathUtil.randFloat(1, 100);
                if (!bBack) {
                    if (bCopy < 255) {
                        bCopy++;
                    } else {
                        bBack = true;
                    }
                }

                if (bBack) {
                    if (bCopy > 0) {
                        bCopy--;
                    } else {
                        bCopy = 1;
                        bBack = false;
                    }
                }
            }


            return;
        }

        rCopy = r;
        gCopy = g;
        bCopy = b;
    }

    @EventTarget
    void renderGameOverlay(RenderGameOverlayEvent event) {
        String custom = Lite.MANIFEST.getName();
        if (mc.gameSettings.showDebugInfo) return;

        // draw watermark
        if (waterMark) {
            switch (wm) {
                case TEXT:
                    FontUtil.drawStringWithShadow(Lite.MANIFEST.getName() + EnumChatFormatting.GRAY + " v" + Lite.MANIFEST.getVersion(), new Vec2f(4, 5), this.getColor(0, 1));
                    FontUtil.drawStringWithShadow(EnumChatFormatting.WHITE + "Lite", new Vec2f(4, 5), this.getColor(0, 1));
                    break;
                case IMAGE:
                    RenderUtil.drawImage(new ResourceLocation("Lite/Logo.png"), new Vec2f(4, 5), 70, 24);
                    break;
            }
        }

        // draw the arraylist
        if (arrayList) {
            drawArrayList(event);
        }
    }

    void drawArrayList(RenderGameOverlayEvent event) {
        // default margin between items & the screen border
        int margin = 5;

        // start position
        Vec2f pos =
                new Vec2f(event.getWidth() - 4, margin / 2f - (FontUtil.getFontHeight() + margin) - 1);
        Vec2f bgpos =
                new Vec2f(event.getWidth() - 8, margin / 2f - (FontUtil.getFontHeight() + margin) - 2);
        Vec2f linepos =
                new Vec2f(event.getWidth(), margin / 2f - (FontUtil.getFontHeight() + margin) - 2);

        // saving the last location
        AtomicReference<Vec2f> lastPosition =
                new AtomicReference<>(null);

        // loop index
        AtomicInteger index =
                new AtomicInteger(0);

        Lite.MODULE_FACTORY.
                getActiveModules().filter(module -> !module.isHidden())
                .sorted((o1, o2) -> (int) (FontUtil.getStringWidth(o2.getDisplayName()) - FontUtil.getStringWidth(o1.getDisplayName())))
                .forEachOrdered(module -> {
                    float width = 0;
                    if (line) {
                        width = FontUtil.getStringWidth(module.getDisplayName());
                    }
                    else {
                        width = FontUtil.getStringWidth(module.getDisplayName()) - 2;
                    }
                    float height = FontUtil.getFontHeight();
                    float bgwidth = 0;
                    if (line) {
                        bgwidth = FontUtil.getStringWidth(module.getDisplayName());
                    }
                    else {
                        bgwidth = FontUtil.getStringWidth(module.getDisplayName()) - 2;
                    }
                    float bgheight = FontUtil.getFontHeight();
                    int color = getColor(index.get(), 25);

                    Vec2f currentPos = pos
                            .add(0, height + margin)
                            .clone()
                            .add(-width, 0);
                    Vec2f currentBGPos = bgpos
                            .add(0, bgheight + margin)
                            .clone()
                            .add(-bgwidth, 0);
                    Vec2f currentLinePos = linepos
                            .add(0, bgheight + margin)
                            .clone()
                            .add(-1, 0);


                    // text
                    if (fill) {
                        RenderUtil.drawRect(currentBGPos, (int) width + 8 , (int) height + margin, ColorUtil.Colors.BLACK_TRANSPARENT.getColor());
                    }
                    if (line) {
                        RenderUtil.drawRect(currentLinePos, 1 , (int) height + margin, this.getColor(5, 1));
                    }
                    FontUtil.drawStringWithShadow(module.getDisplayName(), currentPos, color);

                    lastPosition.set(currentPos);
                    index.addAndGet(1);
                });
    }

    public int getColor(int offset, int rainbowOffset) {
        if (this.colorMode == ColorMode.RAINBOW && rainbowType == RainbowType.NORMAL) {
            return Color.HSBtoRGB(((Minecraft.getSystemTime() + (10 * (Minecraft.getMinecraft().thePlayer
                    .ticksExisted + offset * rainbowOffset))) % 5000F) / 5000F, 1, 1);
        } else if (this.colorMode == ColorMode.RAINBOW && rainbowType == RainbowType.PASTEL) {
            return Color.HSBtoRGB(((Minecraft.getSystemTime() + (10 * (Minecraft.getMinecraft().thePlayer
                    .ticksExisted + offset * rainbowOffset))) % 5000F) / 5000F, 0.5f, 1);
        } else if (this.colorMode == ColorMode.FADE) {
            int speed = (1000 * (this.fadeSpeed > 0 ? this.fadeSpeed : 1));
            return fade((System.currentTimeMillis() + (offset * (150 - 3))) % speed / (speed / 2.0f));
        } else if (this.colorMode == ColorMode.MAIN) {
            return new Color(52, 152, 252).getRGB();
        } else {
            return new Color(rCopy, gCopy, bCopy).getRGB();
        }
    }

    public int fade(float time) {
        if (time > 1) time = 1 - time % 1;

        Vec4f from = ColorUtil.getColor(new Color(this.r, this.g, this.b).getRGB());
        Vec4f to = ColorUtil.getColor(new Color(this.fadeToR, this.fadeToG, this.fadeToB).getRGB());

        Vec4f diff = to.clone().sub(from);
        Vec4f newColor = from.clone().add(diff.clone().mul(time));

        return ColorUtil.getColor(newColor);
    }

    public enum RainbowType {
        NORMAL,
        PASTEL,
    }

    public enum WatermarkMode {
        TEXT,
        IMAGE
    }

    public enum ColorMode {
        STATIC,
        RAINBOW,
        MAIN,
        FADE
    }

    public enum Fonts {
        MINECRAFT,
        APPLE
    }
}

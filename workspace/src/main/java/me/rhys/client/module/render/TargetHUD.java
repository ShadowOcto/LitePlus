package me.rhys.client.module.render;

import javafx.animation.Interpolatable;
import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.render.Render2DEvent;
import me.rhys.base.event.impl.render.RenderHUDEvent;
import me.rhys.base.font.Fonts;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.combat.aura.Aura;
import me.rhys.client.module.render.ClickGui;
import me.rhys.client.module.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TargetHUD extends Module {
    public TargetHUD(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        setHidden(true);
    }

    @Name("X Position")
    @Clamp(min = 0, max = 845)
    public double xp;

    @Name("Y Position")
    @Clamp(min = 0, max = 506.5)
    public double yp;

    public double tx;

    @Name("Mobs/Animals")
    public boolean animals = false;

    private double healthAnimation;
    public boolean mousedown = false;

    @EventTarget
    void onRender(RenderHUDEvent event) {
        Aura aura = (Aura) Lite.MODULE_FACTORY.findByClass(Aura.class);
        ClickGui clickGUI = (ClickGui) Lite.MODULE_FACTORY.findByClass(ClickGui.class);
        HUD hud = (HUD) Lite.MODULE_FACTORY.findByClass(HUD.class);

        if ((aura.getData().isEnabled() && aura.target instanceof EntityPlayer) || clickGUI.getData().isEnabled() || mc.ingameGUI.getChatGUI().getChatOpen()) {

//            if (tx > xp / 5) {
//                tx--;
//            }

            ScaledResolution scaledResolution = new ScaledResolution(mc);

            EntityPlayer target = (EntityPlayer) aura.target;
                    float healthPercentage1 = 1.0f;
                    if (aura.getData().isEnabled() && aura.target instanceof EntityPlayer) {
                        healthPercentage1 = MathHelper.clamp_float( target.getHealth()
                                / target.getMaxHealth(), 0, 1);
                    }
                    else {
                        healthPercentage1 = MathHelper.clamp_float( mc.thePlayer.getHealth()
                                / mc.thePlayer.getMaxHealth(), 0, 1);
                    }

                    GL11.glPushMatrix();
                    int width1 = 150;
                    int height1 = 53;

                    GL11.glTranslated(xp, yp, 0);

                    if (aura.getData().isEnabled() && aura.target instanceof EntityPlayer) {
                        double nameLength = Fonts.INSTANCE.getApple().getStringWidth(target.getName());
                        if (nameLength > 94) {
                            width1 = (int) (55 + nameLength);
                        }
                    }
                    else {
                        double nameLength = Fonts.INSTANCE.getApple().getStringWidth(mc.thePlayer.getName());
                        if (nameLength > 94) {
                            width1 = (int) (55 + nameLength);
                        }
                    }

                    RenderUtil.drawImage(new ResourceLocation("Lite/THBG.png"), new Vec2f(0, 0), (int) (width1), (int) (height1));
//                    RenderUtil.drawRect(0, 0, width1, height1 -1, ColorUtil.Colors.BLACK_TRANSPARENT.getColor());

                    if (aura.getData().isEnabled() && aura.target instanceof EntityPlayer) {
                        Fonts.INSTANCE.getBigapple().drawStringWithShadow(target.getName(),
                                height1, 5, -1);

                        Fonts.INSTANCE.getApple().drawStringWithShadow("§fHealth: §7" +
                                        MathUtil.round(target.getHealth(), 2),
                                height1, 42, -1);
                    }
                    else {
                        Fonts.INSTANCE.getBigapple().drawStringWithShadow(mc.thePlayer.getName(),
                                height1, 5, -1);

                        Fonts.INSTANCE.getApple().drawStringWithShadow("§fHealth: §7" +
                                        MathUtil.round(mc.thePlayer.getHealth(), 2),
                                height1, 42, -1);
                    }

                    int ping = -1;

                    if (aura.getData().isEnabled() && aura.target instanceof EntityPlayer) {
                        if (mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null) {
                            ping = mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getResponseTime();
                        }
                    }
                    else {
                        ping = (int) mc.getCurrentServerData().pingToServer;
                    }

                    if (target == null) {
                        Fonts.INSTANCE.getApple().drawStringWithShadow("§fNot in combat", height1, 18, -1);
                    }
                    else {
                        if (target.getHealth() < mc.thePlayer.getHealth()) {
                            Fonts.INSTANCE.getApple().drawStringWithShadow("§aWinning", height1, 18, -1);
                        }
                        else {
                            Fonts.INSTANCE.getApple().drawStringWithShadow("§cLosing", height1, 18, -1);
                        }
                    }

                    RenderUtil.drawRect(3, 3, (int) (height1 - 6), (int) (height1 - 6), ColorUtil.Colors.DARK_GRAY.getColor());
//                    RenderUtil.drawRect(3, 3, height1 - 14, height1 - 14, hud.getColor(0, 1));

                    if (aura.getData().isEnabled() && aura.target instanceof EntityPlayer) {
                        this.renderSkinHead(target, 4, 4, (int) (height1 - 8), new Color(255, 255, 255, 255));
                    }
                    else {
                        this.renderSkinHead(mc.thePlayer, 4, 4, (int) (height1 - 8), new Color(255, 255, 255, 255));
                    }

                    RenderUtil.drawRect((float) (53), height1 - 25, (int) ((width1 - 56) * MathHelper.clamp_double(this.healthAnimation, 0.02, 1)), 10, hud.getColor(0, 1));

                    RenderUtil.drawRect(new Vec2f(3, 5), 1, 1, 1);

                    this.healthAnimation += ((healthPercentage1 - this.healthAnimation) / 5.5f);
                    GL11.glPopMatrix();
            }
//            else {
//            ScaledResolution scaledResolution = new ScaledResolution(mc);
//            tx = scaledResolution.getScaledWidth() / 5;
//            }
        }

    public void renderSkinHead(EntityLivingBase player, double x, double y, int size, Color color) {
        if (!(player instanceof EntityPlayer))
            return;

        try {
            GL11.glPushMatrix();
            mc.getTextureManager().bindTexture(((AbstractClientPlayer) player).getLocationSkin());
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

            Gui.drawScaledCustomSizeModalRect((int) x, (int) y, 8, 8, 8, 8, size, size, 64, 64);
            GL11.glPopMatrix();
        } catch (Exception ignored) {
        }
    }
}
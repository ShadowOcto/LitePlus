package me.rhys.client.ui.alt.element;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.rhys.client.Manager;
import me.rhys.client.ui.alt.AltUI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import lombok.Getter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.Validate;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
public class AltServerButton extends Button {

    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
    private static final ThreadPoolExecutor THREAD_EXECUTOR = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final int ICON_SIZE = 32;

    private final ServerData serverData;
    private final ResourceLocation resourceLocation;
    private DynamicTexture dynamicTexture;

    private String imgData;

    public AltServerButton(ServerData serverData, Vec2f offset, int width, int height) {
        super(serverData.serverName, offset, width, height);
        this.serverData = serverData;
        this.resourceLocation = new ResourceLocation("servers/" + serverData.serverIP + "/icon");
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        UIScreen screen = getScreen();

        // draw the background
        if (background == ColorUtil.Colors.TRANSPARENT.getColor()) {
            RenderUtil.drawRect(pos, width, height, screen.theme.buttonColors.background);
        }

        if (!serverData.field_78841_f) {
            serverData.field_78841_f = true;
            serverData.pingToServer = -2L;
            serverData.serverMOTD = "";
            serverData.populationInfo = "";
            THREAD_EXECUTOR.submit(() -> {
                try {
                    AltUI.getServerPinger().ping(serverData);
                } catch (UnknownHostException var2) {
                    serverData.pingToServer = -1L;
                    serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                } catch (Exception var3) {
                    serverData.pingToServer = -1L;
                    serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                }
            });
        }

        // draw the label
        FontUtil.drawString(label, pos.clone().add(32 + 5, 3), screen.theme.buttonColors.text);

        boolean bigFlag = serverData.version > 47;
        boolean smallFlag = serverData.version < 47;
        boolean flag = bigFlag || smallFlag;

        // drawing the status display for player count & ping

        String statusDisplay = flag ? EnumChatFormatting.DARK_RED + serverData.gameVersion : serverData.populationInfo;
        int j = (int) FontUtil.getStringWidth(statusDisplay);

        FontUtil.drawString(statusDisplay, pos.clone().add(width, 0).x - j - 15 - 2, pos.y + 3, 8421504);

        int k = 0;
        String s = null;
        int l;
        String s1;

        if (flag) {
            l = 5;
            s1 = bigFlag ? "Client out of date!" : "Server out of date!";
            s = serverData.playerList;
        } else if (serverData.field_78841_f && serverData.pingToServer != -2L) {
            if (serverData.pingToServer < 0L) {
                l = 5;
            } else if (serverData.pingToServer < 150L) {
                l = 0;
            } else if (serverData.pingToServer < 300L) {
                l = 1;
            } else if (serverData.pingToServer < 600L) {
                l = 2;
            } else if (serverData.pingToServer < 1000L) {
                l = 3;
            } else {
                l = 4;
            }

            if (serverData.pingToServer < 0L) {
                s1 = "(no connection)";
            } else {
                s1 = serverData.pingToServer + "ms";
                s = serverData.playerList;
            }
        } else {
            k = 1;
            l = (int) (Minecraft.getSystemTime() / 100L + (long) (2 * 2) & 7L);

            if (l > 4) {
                l = 8 - l;
            }

            s1 = "Pinging...";
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        Gui.drawModalRectWithCustomSizedTexture((int) pos.x + width - 15, (int) pos.y + 1, (float) (k * 10), (float) (176 + l * 8), 10, 8, 256.0F, 256.0F);

        // draw the server MOTD
        if (serverData.serverMOTD != null) {
            Minecraft.getMinecraft().fontRendererObj.drawSplitString(serverData.serverMOTD, (int) pos.clone().add(32 + 5, 4 + FontUtil.getFontHeight()).x, (int) pos.clone().add(ICON_SIZE + 5, 4 + FontUtil.getFontHeight()).y, (width - (ICON_SIZE + 5)), -1);
        } else {
            FontUtil.drawString("...", pos.clone().add(ICON_SIZE + 5, 4 + FontUtil.getFontHeight()), -1);
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, 1);

        if (serverData.getBase64EncodedIconData() != null && !serverData.getBase64EncodedIconData().equals(this.imgData)) {
            this.imgData = serverData.getBase64EncodedIconData();
            this.prepareServerIcon();
        }

        if (dynamicTexture != null) {
            this.drawServerIcon(pos, resourceLocation);
        } else {
            this.drawServerIcon(pos, UNKNOWN_SERVER);
        }

        int i1 = (int) (mouse.x - pos.x);
        int j1 = (int) (mouse.y - pos.y);

        if (i1 >= width - 15 && i1 <= width - 5 && j1 >= 0 && j1 <= 8) {
            Manager.UI.ALT.setHoveringText(s1);
        } else if (i1 >= width - j - 15 - 2 && i1 <= width - 15 - 2 && j1 >= 0 && j1 <= 8) {
            Manager.UI.ALT.setHoveringText(s);
        }

        GlStateManager.popMatrix();
    }

    private void prepareServerIcon() {
        Minecraft mc = Minecraft.getMinecraft();
        if (serverData.getBase64EncodedIconData() == null) {
            mc.getTextureManager().deleteTexture(resourceLocation);
            dynamicTexture = null;
        } else {
            ByteBuf byteBuf0 = Unpooled.copiedBuffer(serverData.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf byteBuf1 = Base64.decode(byteBuf0);
            BufferedImage bufferedimage;
            label101:
            {
                try {
                    bufferedimage = TextureUtil.readBufferedImage(new ByteBufInputStream(byteBuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                    break label101;
                } catch (Throwable throwable) {
                    serverData.setBase64EncodedIconData((String) null);
                } finally {
                    byteBuf0.release();
                    byteBuf1.release();
                }

                return;
            }

            if (this.dynamicTexture == null) {
                this.dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                mc.getTextureManager().loadTexture(this.resourceLocation, this.dynamicTexture);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
            dynamicTexture.updateDynamicTexture();
        }
    }

    protected void drawServerIcon(Vec2f pos, ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture((int) pos.x, (int) pos.y, 0.0F, 0.0F, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        GlStateManager.disableBlend();
    }

}

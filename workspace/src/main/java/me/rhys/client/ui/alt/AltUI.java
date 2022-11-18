package me.rhys.client.ui.alt;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.rhys.base.Lite;
import me.rhys.client.files.AccountsFile;
import me.rhys.client.ui.alt.element.AccountItem;
import me.rhys.client.ui.alt.element.AltServerButton;
import me.rhys.client.ui.alt.popup.AccountManagementPopup;
import me.rhys.client.ui.alt.popup.DirectLoginPopup;
import lombok.Getter;
import lombok.Setter;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.element.panel.ScrollPanel;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltUI extends UIScreen {

    @Getter
    private static final OldServerPinger serverPinger = new OldServerPinger();

    public final Map<String, String> queue = new HashMap<>();

    private static final int BACKGROUND_COLOR = new Color(27, 34, 44, 255).getRGB();
    private static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 15).getRGB();
    private static final int TEXT_SHADOW_COLOR = ColorUtil.rgba(190, 190, 190, 1.0f);

    private final int ALT_PANEL_WIDTH = 135;

    private final int SCREEN_MARGIN = 5;
    private final int PANEL_BORDER_SIZE = 1;

    private int currentServer = -1;

    private Panel displayPanel;

    private Button actionButton;
    private Button backBtn;
    private Button importBtn;
    private Button directLoginBtn;
    private Button refreshBtn;

    public ScrollPanel altPanel;
    private ScrollPanel serversPanel;

    private ServerList savedServerList;

    @Setter
    private String hoveringText;

    @Override
    public void initGui() {
        super.initGui();

        // reset the current server
        currentServer = -1;

        reloadServers();

        if (!queue.isEmpty()) {
            queue.forEach(this::addAccount);
            queue.clear();
        }
    }

    @Override
    public void typeKey(char keyChar, int keyCode) {
        if (keyCode == Keyboard.KEY_F5) {
            reloadServers();
        }
    }

    @Override
    public void updateScreen() {
        serverPinger.pingPendingNetworks();
    }

    @Override
    protected void init() {
        ScaledResolution resolution = getResolution();

        displayPanel = new ScrollPanel(new Vec2f(SCREEN_MARGIN, SCREEN_MARGIN), ALT_PANEL_WIDTH, (int) (FontUtil.getFontHeight() * 3 + SCREEN_MARGIN));
        displayPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();

        altPanel = new ScrollPanel(displayPanel.offset.clone().add(0, displayPanel.getHeight() - SCREEN_MARGIN), displayPanel.getWidth(), resolution.getScaledHeight() - (displayPanel.getHeight() + (SCREEN_MARGIN + PANEL_BORDER_SIZE)));
        altPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();

        serversPanel = new ScrollPanel(displayPanel.offset.clone().add(displayPanel.getWidth() + SCREEN_MARGIN + PANEL_BORDER_SIZE, FontUtil.getFontHeight() + SCREEN_MARGIN + 13), resolution.getScaledWidth() - (displayPanel.getWidth() + SCREEN_MARGIN * 2 + PANEL_BORDER_SIZE) - (SCREEN_MARGIN + PANEL_BORDER_SIZE), altPanel.getHeight() - (20 + SCREEN_MARGIN));
        serversPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();

        add(displayPanel);
        add(altPanel);
        add(serversPanel);

        actionButton = new Button("☰", displayPanel.offset.clone().add((displayPanel.getWidth() - ((int) (FontUtil.getFontHeight() * 2 + SCREEN_MARGIN))), (displayPanel.getHeight() - ((int) (FontUtil.getFontHeight() * 3 + SCREEN_MARGIN))) / 2.0f), (int) (FontUtil.getFontHeight() * 2 + SCREEN_MARGIN), (int) (FontUtil.getFontHeight() * 2 + SCREEN_MARGIN));
        actionButton.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
        add(actionButton);

        backBtn = new Button("Back", new Vec2f(resolution.getScaledWidth() - (100 + SCREEN_MARGIN + PANEL_BORDER_SIZE), (displayPanel.getHeight() - 20) / 2.0f), 100, 20);
        backBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
        add(backBtn);

        refreshBtn = new Button("Refresh", backBtn.offset.clone().sub(5 + backBtn.getWidth(), 0), 100, 20);
        refreshBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
        add(refreshBtn);

        importBtn = new Button("Import", serversPanel.offset.clone().add(0, serversPanel.getHeight() + SCREEN_MARGIN), (serversPanel.getWidth() - SCREEN_MARGIN) / 2, 20);
        importBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
        add(importBtn);

        directLoginBtn = new Button("Direct Login", importBtn.offset.clone().add(importBtn.getWidth() + SCREEN_MARGIN, 0), importBtn.getWidth(), 20);
        directLoginBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
        add(directLoginBtn);
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        if (button == 0) {
            if (actionButton.isHovered(pos)) {
                displayPopup(new AccountManagementPopup(100, 100));
                actionButton.playSound();
            } else if (importBtn.isHovered(pos)) {
                importBtn.playSound();
            } else if (directLoginBtn.isHovered(pos)) {
                displayPopup(new DirectLoginPopup(200, 115));
                directLoginBtn.playSound();
            } else if (refreshBtn.isHovered(pos)) {
                reloadServers();
                directLoginBtn.playSound();
            } else if (backBtn.isHovered(pos)) {
                mc.displayGuiScreen(null);

                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }

                backBtn.playSound();
            }

            if (pos.x >= serversPanel.pos.x && pos.y >= serversPanel.pos.y && pos.y <= (serversPanel.pos.y + serversPanel.getHeight()) && pos.x <= (serversPanel.pos.x + serversPanel.getWidth())) {
                AltServerButton altServerButton = (AltServerButton) serversPanel.getContainer().find(element -> element.isHovered(pos.clone().add(0, serversPanel.getScrollAmount())));
                if (altServerButton != null) {
                    int index = serversPanel.getContainer().indexOf(altServerButton);

                    if (currentServer != -1 && index == currentServer) {
                        mc.displayGuiScreen(new GuiConnecting(this, mc, altServerButton.getServerData()));
                    }

                    currentServer = index;
                }
            }
        }
    }

    @Override
    protected void preDraw(Vec2f mouse, float partialTicks) {
        hoveringText = null;

        List<Element> items = altPanel.getContainer().getItems();
        for (int i = 0; i < items.size(); i++) {
            Element item = items.get(i);

            item.background = i % 2 == 0 ? ColorUtil.darken(altPanel.background, 15).getRGB() : altPanel.background;
        }

        ScaledResolution resolution = getResolution();

        displayPanel.setHeight((int) (FontUtil.getFontHeight() * 2 + SCREEN_MARGIN));
        altPanel.setHeight(resolution.getScaledHeight() - (displayPanel.getHeight() + SCREEN_MARGIN * 2 + PANEL_BORDER_SIZE * 2 + SCREEN_MARGIN - PANEL_BORDER_SIZE));
        serversPanel.setWidth(resolution.getScaledWidth() - (displayPanel.getWidth() + SCREEN_MARGIN * 2 + PANEL_BORDER_SIZE) - (SCREEN_MARGIN + PANEL_BORDER_SIZE));
        serversPanel.setHeight(altPanel.getHeight() - (20 + SCREEN_MARGIN));

        importBtn.setWidth((serversPanel.getWidth() - SCREEN_MARGIN) / 2);
        directLoginBtn.setWidth(importBtn.getWidth());

        serversPanel.getContainer().forEach(element -> element.setWidth(serversPanel.getWidth()));

        backBtn.offset = new Vec2f(resolution.getScaledWidth() - (100 + SCREEN_MARGIN + PANEL_BORDER_SIZE), (displayPanel.getHeight() - 10) / 2.0f);
        refreshBtn.offset = backBtn.offset.clone().sub(5 + backBtn.getWidth(), 0);
        importBtn.offset = serversPanel.offset.clone().add(0, serversPanel.getHeight() + SCREEN_MARGIN);
        directLoginBtn.offset = importBtn.offset.clone().add(importBtn.getWidth() + SCREEN_MARGIN, 0);

        RenderUtil.drawRect(new Vec2f(), width, height, BACKGROUND_COLOR);

        drawBorder(displayPanel);
        drawBorder(altPanel);
        drawBorder(serversPanel);
        drawBorder(backBtn);
        drawBorder(refreshBtn);
        drawBorder(importBtn);
        drawBorder(directLoginBtn);

        FontUtil.drawString("Servers", serversPanel.pos.clone().sub(0, FontUtil.getFontHeight() * 2.2f), -1);
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        GlStateManager.pushMatrix();

        float scale = 0.6f;

        GlStateManager.scale(scale, scale, scale);

        FontUtil.drawString("Account", displayPanel.pos.clone().add(5, 3).div(scale, scale), TEXT_SHADOW_COLOR);

        GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);

        GlStateManager.popMatrix();

        Session session = mc.session;
        if (session != null
                && session.getUsername() != null
                && !session.getUsername().isEmpty()) {
            FontUtil.drawString(session.getUsername(), displayPanel.pos.clone().add(5, (displayPanel.getHeight() - FontUtil.getFontHeight()) / 2f + 3.5f), -1);
        } else {
            FontUtil.drawString("UNKNOWN_USER", displayPanel.pos.clone().add(5, (displayPanel.getHeight() - FontUtil.getFontHeight()) / 2f + 3.5f), -1);
        }

        RenderUtil.drawRect(actionButton.pos.clone().sub(1, 0), 1, actionButton.getHeight(), SHADOW_COLOR);

        if (serversPanel.getContainer().isEmpty()) {
            FontUtil.drawCenteredString("No Servers found...", serversPanel.pos.clone().add(serversPanel.getWidth() / 2.0f, serversPanel.getHeight() / 2.0f), -1);
        }
        FontUtil.drawCenteredString("No Alts found...", altPanel.pos.clone().add(altPanel.getWidth() / 2.0f, altPanel.getHeight() / 2.0f), -1);

        if (hoveringText != null) {
            drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(hoveringText)), (int) mouse.x + 5, (int) mouse.y);
        }

        if (currentServer != -1) {
            GlStateManager.pushMatrix();

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glLineWidth(2f);

            RenderUtil.clipRect(serversPanel.pos, serversPanel.pos.clone().add(serversPanel.getWidth(), serversPanel.getHeight()), getScale());

            AltServerButton altServerButton = (AltServerButton) serversPanel.getContainer().get(currentServer);

            Vec2f pos = altServerButton.pos.clone().sub(0, serversPanel.getScrollAmount());
            RenderUtil.drawOutlineRect(pos, pos.clone().add(altServerButton.getWidth(), altServerButton.getHeight()), -1);

            GL11.glLineWidth(1f);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }
    }

    public void addAccount(String email, String password) {
        altPanel.add(new AccountItem(email, password, altPanel.getWidth(), 20));
    }

    private void drawBorder(Element element) {
        RenderUtil.drawRect(element.pos.clone().sub(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE), element.getWidth() + PANEL_BORDER_SIZE * 2, element.getHeight() + PANEL_BORDER_SIZE * 2, SHADOW_COLOR);
    }

    private void reloadServers() {
        serversPanel.getContainer().getItems().clear();

        savedServerList = new ServerList(this.mc);
        savedServerList.loadServerList();

        int length = savedServerList.countServers();
        for (int i = 0; i < length; i++) {
            ServerData serverData = savedServerList.getServerData(i);
            if (serverData != null) {
                AltServerButton altServerButton = new AltServerButton(serverData, new Vec2f(), serversPanel.getWidth(), 32);
                altServerButton.background = i % 2 == 0 ? ColorUtil.darken(serversPanel.background, 15).getRGB() : serversPanel.background;
                serversPanel.add(altServerButton);
            }
        }
    }


    @Override
    public void onGuiClosed() {
        serverPinger.clearPendingNetworks();
        Lite.FILE_FACTORY.saveFile(AccountsFile.class);
    }

}

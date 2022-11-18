package me.rhys.client.ui.click;

import me.rhys.base.Lite;
import me.rhys.client.module.render.ClickGui;
import me.rhys.client.module.render.HUD;
import me.rhys.client.ui.click.element.button.ModuleButton;
import me.rhys.client.ui.click.element.dropdown.EnumDropDown;
import me.rhys.client.ui.click.element.dropdown.ModeDropDown;
import me.rhys.client.ui.click.element.slider.DoubleSettingSlider;
import me.rhys.client.ui.click.element.slider.FloatSettingSlider;
import me.rhys.client.ui.click.element.slider.IntSettingSlider;
import me.rhys.client.ui.click.element.slider.ShortSettingSlider;
import lombok.Getter;
import lombok.var;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.DoubleNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.FloatNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.IntNumberSetting;
import me.rhys.base.module.setting.impl.number.impl.ShortNumberSetting;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.button.CheckBox;
import me.rhys.base.ui.element.button.ImageButton;
import me.rhys.base.ui.element.label.Label;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.element.panel.ScrollPanel;
import me.rhys.base.ui.element.window.Window;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ClickUI extends UIScreen {

    private Window window;

    private Panel selectionPanel;
    private Panel displayPanel;

    public ScrollPanel modulesPanel;
    private ScrollPanel settingsPanel;

    private Label displayLabel;

    public int moduleCurrent;
    private int current;

    @Override
    protected void init() {
        moduleCurrent = 0;

        // setup the initial values
        int windowWidth = 325,
                windowHeight = 252;

        // set current selection to the first button
        current = 0;

        // add the main window
        add(window = new Window(
                        new Vec2f(
                                width / 2f,
                                height / 2f
                        ),
                        windowWidth,
                        windowHeight
                )
        );

        // update the screen
        window.getPanel().setScreen(this);

        // center the window to the screen
        window.offset.sub(
                windowWidth / 2.0f,
                windowHeight / 2.0f
        );

        // add all the panels
        addSelectionPanel(windowHeight);
        addDisplayPanel();
        addModulesPanel();
        addSettingsPanel();
    }

    private void addModulesPanel() {
        // create the modules panel
        modulesPanel = new ScrollPanel(
                new Vec2f(selectionPanel.getWidth(), displayPanel.getHeight()),
                125,
                window.getHeight() - displayPanel.getHeight()
        );

        // update the panel color
        modulesPanel.background =
                ColorUtil.darken(theme.windowColors.background, 25).getRGB();

        // populate the panel with module buttons
        addModules();

        // add the panel to the window
        window.getPanel().add(modulesPanel);
    }

    private void addDisplayPanel() {
        // create the display panel
        displayPanel = new Panel(
                new Vec2f(selectionPanel.getWidth(), 0),
                125,
                20
        );

        // update the panel color
        displayPanel.background =
                ColorUtil.darken(theme.windowColors.background, 35).getRGB();

        // create the label
        displayLabel = new Label(
                Category.values()[current].name(),
                new Vec2f(displayPanel.getWidth() / 2f, displayPanel.getHeight() / 2f)
        );

        // make it center the text
        displayLabel.setCentered(true);

        // make the label bigger
        displayLabel.setScale(1.0f);

        // add the display label to the panel
        displayPanel.add(displayLabel);

        // add the panel to the window
        window.getPanel().add(displayPanel);
    }

    private void addSelectionPanel(int windowHeight) {
        // create the new selector panel
        selectionPanel = new Panel(
                new Vec2f(0, 0),
                50,
                windowHeight
        );

        // update the selection panel background
        selectionPanel.background =
                ColorUtil.darken(theme.windowColors.background, 25).getRGB();

        // get all the categories
        var categories = Category.values();

        // calculate the height of each section
        var numOfElements = categories.length;
        var buttonHeight = selectionPanel.getHeight() / numOfElements;

        // add all the category buttons
        for (int i = 0; i < categories.length; i++) {
            var category = categories[i];

            // add the button
            selectionPanel.add(
                    new ImageButton(
                            new ResourceLocation("Lite/" + category.name().toLowerCase() + ".png"),
                            new Vec2f(0, i * buttonHeight),
                            selectionPanel.getWidth(),
                            buttonHeight,
                            16,
                            16
                    )
            );
        }

        // update the first buttons color
        selectionPanel.getContainer().get(0).background =
                ColorUtil.darken(theme.windowColors.background, 45).getRGB();

        // apply movement processor flag so it cant move on the selection items
        window.callback = this::canMoveWindow;

        // add the panel to the window
        window.getPanel().add(selectionPanel);
    }

    private void addSettingsPanel() {
        float offset = (selectionPanel.getWidth() + modulesPanel.getWidth());

        settingsPanel = new ScrollPanel(
                new Vec2f(offset + 10, 10),
                (int) (window.getWidth() - offset) - 20,
                window.getHeight() - 20
        );

        settingsPanel.setItemMargin(8);

        addSettings(((ModuleButton) modulesPanel.getContainer().get(moduleCurrent)).getModule());

        window.getPanel().add(settingsPanel);
    }

    private void addModules() {
        // clear it to make sure its empty before adding anything
        modulesPanel.getContainer().getItems().clear();

        Category[] categories = Category.values();

        if (current < categories.length) {
            // get the category
            Category category = categories[current];

            // make sure its valid
            if (category != null) {
                AtomicInteger index = new AtomicInteger(1);

                // loop through all modules and add them to the list
                Lite.MODULE_FACTORY.filter(module -> module.getData().getCategory().equals(category)).forEach(module -> {
                    ModuleButton moduleButton;
                    modulesPanel.add(moduleButton = new ModuleButton(module, new Vec2f(0, 0), 125, 20));

                    if (index.getAndAdd(1) % 2 == 0)
                        moduleButton.background = ColorUtil.darken(theme.windowColors.background, 30).getRGB();
                });
            }
        }
    }

    public void addSettings(Module module) {
        settingsPanel.setScrollAmount(0.0f);

        settingsPanel.getContainer().getItems().clear();

        if (module.getItems().size() > 0) {
            settingsPanel.add(new Label("Mode", new Vec2f()));

            settingsPanel.add(
                    new ModeDropDown(
                            module,
                            new Vec2f(0, 0),
                            125, 20
                    )
            );
        }

        Lite.SETTING_FACTORY.get(module).settings.forEach(this::addSetting);

        if (module.getItems().size() > 0 && module.getCurrentMode() != null) {
            Map<ModuleMode<?>, List<Setting>> settingContainer = Lite.SETTING_FACTORY.get(module).settingManager;
            if (settingContainer != null) {
                List<Setting> settings = settingContainer.get(module.getCurrentMode());
                if (settings != null && !settings.isEmpty()) {
                    settings.forEach(this::addSetting);
                }
            }
        }
    }

    @Override
    public void clickMouse(Vec2f pos, int button) {
        if (selectionPanel.isHovered(pos)) {
            // unselect all
            selectionPanel.getContainer().forEach(element -> element.background = ColorUtil.Colors.TRANSPARENT.getColor());

            // then select the currently clicked one
            selectionPanel.getContainer().filter(element -> element.isHovered(pos)).findFirst().ifPresent(target -> {
                // update the color
                target.background = ColorUtil.darken(theme.windowColors.background, 45).getRGB();

                // update the current index
                current = selectionPanel.getContainer().indexOf(target);

                Category[] categories = Category.values();

                if (current < categories.length) {

                    // get the category
                    Category category = categories[current];

                    // make sure its valid
                    if (category != null) {

                        // update the display name
                        displayLabel.setLabel(category.name());

                        // add modules
                        addModules();

                        // reset the modules scroll amount
                        modulesPanel.setScrollAmount(0.0f);

                        // restarting the current
                        moduleCurrent = 0;

                        // add settings for current module
                        addSettings(((ModuleButton) modulesPanel.getContainer().get(moduleCurrent)).getModule());
                    }
                }
            });
        } else if (modulesPanel.isHovered(pos) && button == 1) {
            modulesPanel.getContainer().filter(element -> element.isHovered(pos.clone().add(0, modulesPanel.getScrollAmount()))).findFirst().ifPresent(element -> {
                if (element instanceof ModuleButton) {
                    ModuleButton moduleButton = (ModuleButton) element;

                    moduleCurrent = modulesPanel.getContainer().indexOf(element);

                    addSettings(moduleButton.getModule());
                }
            });
        }
    }

    private void addSetting(Setting setting) {

        if (setting instanceof IntNumberSetting) {
            settingsPanel.add(new Label(setting.getName(), new Vec2f()));
            settingsPanel.add(new IntSettingSlider((IntNumberSetting) setting, new Vec2f(), 100, 10));
        }

        if (setting instanceof DoubleNumberSetting) {
            settingsPanel.add(new Label(setting.getName(), new Vec2f()));
            settingsPanel.add(new DoubleSettingSlider((DoubleNumberSetting) setting, new Vec2f(), 100, 10));
        }

        if (setting instanceof FloatNumberSetting) {
            settingsPanel.add(new Label(setting.getName(), new Vec2f()));
            settingsPanel.add(new FloatSettingSlider((FloatNumberSetting) setting, new Vec2f(), 100, 10));
        }

        if (setting instanceof ShortNumberSetting) {
            settingsPanel.add(new Label(setting.getName(), new Vec2f()));
            settingsPanel.add(new ShortSettingSlider((ShortNumberSetting) setting, new Vec2f(), 100, 10));
        }

        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting) setting;

            CheckBox box = new CheckBox(setting.getName(), new Vec2f(), 8, 8);

            box.setChecked(booleanSetting.get());

            box.setCallback(mouse -> {
                booleanSetting.set(!booleanSetting.get());
                return booleanSetting.get();
            });

            settingsPanel.add(box);
        }

        if (setting instanceof EnumSetting) {
            EnumSetting enumSetting = (EnumSetting) setting;

            settingsPanel.add(new Label(enumSetting.getName(), new Vec2f()));
            settingsPanel.add(
                    new EnumDropDown(
                            enumSetting,
                            new Vec2f(),
                            125,
                            20
                    )
            );
        }
    }

    private boolean canMoveWindow(Vec2f mouse) {
        if (settingsPanel.getContainer().filter(element -> element.isHovered(mouse.clone().add(0, settingsPanel.getScrollAmount()))).findFirst().orElse(null) != null
                || modulesPanel.getContainer().filter(element -> element.isHovered(mouse)).findFirst().orElse(null) != null) {
            return false;
        }

        return !selectionPanel.isHovered(mouse);
    }

    @Override
    public void draw(Vec2f mouse, float partialTicks) {
        HUD hud = (HUD) Lite.MODULE_FACTORY.findByClass(HUD.class);

        RenderUtil.drawRect(selectionPanel.pos.clone().sub(2, 0), 2, selectionPanel.getHeight(),
                hud.getColor(0 ,1));

        RenderUtil.drawRect(displayPanel.pos.clone().add(0, displayPanel.getHeight() - 1), displayPanel.getWidth(),
                1, ColorUtil.darken(displayPanel.background, 15).getRGB());
    }

    @Override
    public void onGuiClosed() {
        Lite.FILE_FACTORY.save();
        Lite.MODULE_FACTORY.findByClass(ClickGui.class).toggle(false);
    }

}

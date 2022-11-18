package me.rhys.client.module.ghost.autoclicker.modes;

import me.rhys.client.module.ghost.autoclicker.AutoClicker;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Normal extends ModuleMode<AutoClicker> {

    @Name("Clicks Per Second")
    @Clamp(min = 1.0, max = 20.0)
    public double speed = 10;
    public Robot bot;
    private Timer timer = new Timer();

    public Normal(String name, AutoClicker parent) {
        super(name, parent);
    }

    @EventTarget
    public void playerMove(PlayerMoveEvent event) {
        EntityPlayerSP player = event.getPlayer();
        if (player == null) {
            return;
        }
        try {
            bot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Mouse.isButtonDown(0)) {
            mc.leftClickCounter = 0;
            if (mc.currentScreen == null) {
                if (this.timer.hasReached((1000L / MathUtil.randDouble(speed - 3, speed + 3)))) {
                    try {
                        bot.mouseRelease(16);
                        bot.mousePress(16);
                    } catch (Exception ignored) {
                    }
                    this.timer.reset();
                }
            }
        }
    }
}
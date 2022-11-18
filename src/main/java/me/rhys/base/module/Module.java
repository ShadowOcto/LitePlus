package me.rhys.base.module;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.rhys.base.Lite;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.data.ModuleData;
import me.rhys.base.module.data.Toggleable;
import me.rhys.base.util.container.Container;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

@Getter
@Setter
public abstract class Module extends Container<ModuleMode<? extends Module>> implements Toggleable {

    @Setter
    private ModuleData data;
    protected Minecraft mc = Minecraft.getMinecraft();
    private boolean hidden;


    public Module(String name, String description, Category category, int keyCode) {
        this.data = new ModuleData(name, description, category, keyCode, -1, false);
    }

    public Module(String name, String description, Category category) {
        this(name, description, category, Keyboard.KEY_NONE);
    }

    @Override
    public void add(ModuleMode<? extends Module> item) {
        super.add(item);

        if (data.getCurrentMode() == -1) {
            setCurrentMode(0);
        }
    }

    public void toggle(boolean enabled) {
        // update the state
        data.setEnabled(enabled);

        // getting the required objects
        val minecraft = Minecraft.getMinecraft();
        val player = minecraft.thePlayer;
        val world = minecraft.theWorld;

        // a basic check that checks if the player is in a loaded world
        boolean isWorldLoaded = player != null && world != null;

        // get the current mode
        val currentMode = getCurrentMode();

        // basic check to see if a mode is valid or not
        boolean isModeValid = currentMode != null;

        if (data.isEnabled()) {

            Lite.EVENT_BUS.register(this);

            // if mode is valid register it
            if (isModeValid) {
                Lite.EVENT_BUS.register(currentMode);
            }

            if (isWorldLoaded) {
                onEnable();

                // is mode is valid trigger the enable method
                if (isModeValid) {
                    currentMode.onEnable();
                }
            }
        } else {
            Lite.EVENT_BUS.unRegister(this);

            // if mode is valid register it
            if (isModeValid) {
                Lite.EVENT_BUS.unRegister(currentMode);
            }

            if (isWorldLoaded) {
                onDisable();

                // is mode is valid trigger the enable method
                if (isModeValid) {
                    currentMode.onDisable();
                }
            }
        }

        if (isWorldLoaded) {
            onToggle();
        }
    }

    public void toggle() {
        toggle(!data.isEnabled());
    }

    public void setCurrentMode(String mode) {
        if (mode == null) {
            return;
        }

        setCurrentMode(indexOf(find(moduleMode -> moduleMode.getName().equalsIgnoreCase(mode))));
    }

    public void setCurrentMode(int modeIndex) {
        if (data.getCurrentMode() == modeIndex
                || modeIndex == -1) {
            return;
        }

        // getting the required objects
        val minecraft = Minecraft.getMinecraft();
        val player = minecraft.thePlayer;
        val world = minecraft.theWorld;

        // a basic check that checks if the player is in a loaded world
        boolean isWorldLoaded = player != null && world != null;

        // make sure to unload the previous mode if there was any
        if (data.getCurrentMode() != -1) {
            // get the current mode
            val currentMode = getCurrentMode();

            // check if the world is loaded if so call the disable method
            if (isWorldLoaded) {
                currentMode.onDisable();
            }

            // un-register the old mode
            Lite.EVENT_BUS.unRegister(currentMode);
        }

        // update the new current mode
        data.setCurrentMode(modeIndex);

        // get the new mode
        val currentMode = getCurrentMode();

        // before registering the new mode check that the module is enabled
        if (data.isEnabled()) {
            // check if the world is loaded if so call the enable method
            if (isWorldLoaded) {
                currentMode.onEnable();
            }

            // register the new mode
            Lite.EVENT_BUS.register(currentMode);
        }
    }

    public ModuleMode<? extends Module> getCurrentMode() {
        if (data.getCurrentMode() == -1 || isEmpty()) {
            return null;
        }

        ModuleMode<? extends Module> moduleMode = get(data.getCurrentMode());

        if (moduleMode != null) {
            return moduleMode;
        } else {
            return get(0);
        }
    }

    public String getDisplayName() {
        return data.getName() + (getCurrentMode() != null ? (EnumChatFormatting.GRAY + " " + getCurrentMode().getName()) : "");
    }

    public List<String> getModeNames() {
        return null;
    }


    public EntityPlayerSP player() {
        return mc.thePlayer;
    }

}
package me.rhys.client.module.render;

import me.rhys.client.Manager;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.renderer.EntityRenderer;

public class ClickGui extends Module {

    @Name("Blur")
    public boolean blur = true;
    private boolean loadedShader;

    public ClickGui(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
        setHidden(true);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Manager.UI.CLICK);

        if (blur) {
            if (!loadedShader) {
                loadedShader = true;
                mc.entityRenderer.loadShader(EntityRenderer.shaderResourceLocations[18]);
            }
        }
    }

    @Override
    public void onDisable() {
        if (loadedShader) {
            loadedShader = false;
            mc.entityRenderer.theShaderGroup = null;
        }
    }
}

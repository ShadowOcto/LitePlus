package me.rhys.base.module;

import lombok.Getter;
import me.rhys.base.module.data.Toggleable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

@Getter
public class ModuleMode<T extends Module> implements Toggleable {

    protected Minecraft mc = Minecraft.getMinecraft();

    protected final String name;
    protected final T parent;

    public ModuleMode(String name, T parent) {
        this.name = name;
        this.parent = parent;
    }

    public EntityPlayerSP player() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public WorldClient world() {
        return mc.theWorld;
    }
}
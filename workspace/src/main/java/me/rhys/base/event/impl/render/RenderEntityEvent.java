package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class RenderEntityEvent extends Event {

    private EntityLivingBase entity;
    private boolean isLayers;

    private double x, y, z;

    public boolean esp = false;

    public RenderEntityEvent(double x, double y, double z, EntityLivingBase entity, boolean isLayers) {
        super();
        this.entity = entity;
        this.isLayers = isLayers;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public boolean isLayers() {
        return isLayers;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}


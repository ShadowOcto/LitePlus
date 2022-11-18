package me.rhys.base.event.impl.render;

import me.rhys.base.event.Event;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created on 07/09/2020 Package me.rhys.lite.event.impl.render
 */
public class RenderNameTagEvent extends Event {
    private EntityLivingBase entity;
    private String name;
    private double x;
    private double y;
    private double z;
    private int maxDistance;

    public RenderNameTagEvent(EntityLivingBase entity, String name, double x, double y, double z, int maxDistance) {
        super();
        this.entity = entity;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxDistance = maxDistance;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public String getName() {
        return name;
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

    public int getMaxDistance() {
        return maxDistance;
    }
}

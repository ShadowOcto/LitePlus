package me.rhys.base.module.setting.impl.number;

import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.manifest.Clamp;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;

public abstract class NumberSetting<T> extends Setting {

    public NumberSetting(Object object, Field field) {
        super(object, field);

        if (!field.isAnnotationPresent(Clamp.class)) {
            Minecraft.logger.error(field.getName() + " does not have a clamp annotation but its a number setting");
        }
    }

    public T get() {
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(T value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public double getMin() {
        return field.getAnnotation(Clamp.class).min();
    }

    public double getMax() {
        return field.getAnnotation(Clamp.class).max();
    }
}
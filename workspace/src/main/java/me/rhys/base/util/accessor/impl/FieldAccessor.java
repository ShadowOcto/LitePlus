package me.rhys.base.util.accessor.impl;

import lombok.Getter;
import me.rhys.base.util.accessor.Accessor;

import java.lang.reflect.Field;

@Getter
public class FieldAccessor<T> extends Accessor {

    private Field field;

    public FieldAccessor(Class<?> target, String name) {
        super(target);
        try {
            this.field = target.getDeclaredField(name);
            if (!this.field.isAccessible()) {
                this.field.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public FieldAccessor(Class<?> target, int index) {
        super(target);
        this.field = target.getDeclaredFields()[index];
        if (!this.field.isAccessible()) {
            this.field.setAccessible(true);
        }
    }

    /**
     * Directly set the value of the field
     * by passing the handle to the class
     * and the value that you want to set it to
     *
     * @param handle handle to the class
     * @param value  target value
     */

    public void set(Object handle, T value) {
        try {
            field.set(handle, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Access the value of the field
     * by passing the handle of the class
     *
     * @param handle class handleO
     * @return the value of the field
     */

    public T get(Object handle) {
        try {
            return (T) field.get(handle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
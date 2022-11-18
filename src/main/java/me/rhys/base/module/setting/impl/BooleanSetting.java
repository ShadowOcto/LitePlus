package me.rhys.base.module.setting.impl;


import me.rhys.base.module.setting.Setting;

import java.lang.reflect.Field;

public class BooleanSetting extends Setting {

    public BooleanSetting(Object object, Field field) {
        super(object, field);
    }

    public void toggle() {
        set(!get());
    }

    public void set(boolean value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean get() {
        try {
            return field.getBoolean(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
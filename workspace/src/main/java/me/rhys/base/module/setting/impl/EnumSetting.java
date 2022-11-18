package me.rhys.base.module.setting.impl;


import me.rhys.base.module.setting.Setting;
import me.rhys.base.util.accessor.impl.MethodAccessor;

import java.lang.reflect.Field;

public class EnumSetting extends Setting {

    public EnumSetting(Object object, Field field) {
        super(object, field);
    }

    public void toggle() {
        set(get());
    }

    public boolean set(String choice) {
        Object value;
        try {
            value = Enum.valueOf((Class<? extends Enum>) field.getType(), choice);
            field.set(object, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String[] values() {
        try {
            Enum<?> value = (Enum<?>) field.get(object);
            MethodAccessor methodAccessor = new MethodAccessor(value.getClass(), "values");
            Object[] objs = methodAccessor.invoke(value.getClass());
            String[] strings = new String[objs.length];
            for (int i = 0; i < objs.length; i++) {
                strings[i] = objs[i].toString();
            }
            return strings;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int ordinal() {
        try {
            Enum<?> value = (Enum<?>) field.get(object);
            return value.ordinal();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String get() {
        try {
            return field.get(object).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }
}
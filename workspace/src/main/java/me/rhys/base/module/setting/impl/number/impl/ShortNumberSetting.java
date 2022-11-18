package me.rhys.base.module.setting.impl.number.impl;

import me.rhys.base.module.setting.impl.number.NumberSetting;

import java.lang.reflect.Field;

public class ShortNumberSetting extends NumberSetting<Short> {

    public ShortNumberSetting(Object object, Field field) {
        super(object, field);
    }

}

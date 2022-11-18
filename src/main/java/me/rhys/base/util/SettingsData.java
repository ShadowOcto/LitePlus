package me.rhys.base.util;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.module.setting.Setting;

/**
 * Created on 07/09/2020 Package me.rhys.lite.util
 */
@Getter
@Setter
public class SettingsData {
    private String name;
    private Setting setting;

    public SettingsData(String name, Setting setting) {
        this.name = name;
        this.setting = setting;
    }
}

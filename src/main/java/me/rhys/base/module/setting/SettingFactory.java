package me.rhys.base.module.setting;

import me.rhys.base.Lite;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.*;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.container.MapContainer;

import java.lang.reflect.Field;
import java.util.*;

public class SettingFactory extends MapContainer<Module, SettingFactory.SettingContainer> {

    public void fetchSettings() {
        Lite.MODULE_FACTORY.forEach(module -> {
            if (!getMap().containsKey(module)) {
                put(module, new SettingContainer(module));
            }

            for (Field field : module.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Name.class)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    Setting setting = makeSetting(module, field);
                    get(module).settings.add(setting);
                }
            }

            if (!module.isEmpty()) {
                module.forEach(moduleMode -> {
                    for (Field field : moduleMode.getClass().getDeclaredFields()) {
                        if (field.isAnnotationPresent(Name.class)) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }

                            Setting setting = makeSetting(moduleMode, field);
                            SettingContainer settingContainer = get(module);
                            if (!settingContainer.settingManager.containsKey(moduleMode)) {
                                settingContainer.settingManager.put(moduleMode, new ArrayList<>());
                            }
                            settingContainer.settingManager.get(moduleMode).add(setting);
                        }
                    }
                });
            }
        });
    }

    public Setting getSetting(Module module, String name) {
        return get(module).settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private Setting makeSetting(Object parent, Field field) {
        Setting setting;
        if (field.getType().equals(double.class)) {
            setting = new DoubleNumberSetting(parent, field);
        } else if (field.getType().equals(float.class)) {
            setting = new FloatNumberSetting(parent, field);
        } else if (field.getType().equals(int.class)) {
            setting = new IntNumberSetting(parent, field);
        } else if (field.getType().equals(byte.class)) {
            setting = new ByteNumberSetting(parent, field);
        } else if (field.getType().equals(short.class)) {
            setting = new ShortNumberSetting(parent, field);
        } else if (field.getType().equals(boolean.class)) {
            setting = new BooleanSetting(parent, field);
        } else if ((field.getType().equals(Enum.class)
                || field.getType().getSuperclass() != null
                && field.getType().getSuperclass().equals(Enum.class))) {
            setting = new EnumSetting(parent, field);
        } else {
            setting = new Setting(parent, field);
        }
        return setting;
    }

    public static final class SettingContainer {

        public Module module;
        public List<Setting> settings;
        public Map<ModuleMode<?>, List<Setting>> settingManager;

        public SettingContainer(Module module) {
            this.module = module;
            this.settings = new ArrayList<>();
            this.settingManager = new HashMap<>();
        }

        public Setting getModeSetting(String mode, String setting) {
            try {
                return Objects.requireNonNull(getModeSettings(mode)).stream().filter(s -> s.getName().equalsIgnoreCase(setting)).findFirst().orElse(null);
            } catch (Exception ignored) {
                return null;
            }
        }

        public List<Setting> getModeSettings(String mode) {
            ModuleMode<?> moduleMode = module.find(mm -> mm.getName().equalsIgnoreCase(mode));

            if (moduleMode == null) {
                return null;
            }

            return new ArrayList<>(settingManager.get(moduleMode));
        }
    }
}

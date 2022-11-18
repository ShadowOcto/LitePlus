package me.rhys.base.file.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import me.rhys.client.module.render.ClickGui;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import me.rhys.base.Lite;
import me.rhys.base.file.IFile;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.data.ModuleData;
import me.rhys.base.module.setting.Setting;
import me.rhys.base.module.setting.impl.BooleanSetting;
import me.rhys.base.module.setting.impl.EnumSetting;
import me.rhys.base.module.setting.impl.number.impl.*;

import java.io.File;
import java.util.Arrays;

public class ModulesFile implements IFile {

    private File file;

    @Override
    public void save(Gson gson) {
        val array = new JsonArray();

        Lite.MODULE_FACTORY.forEach(module -> {
            val object = new JsonObject();
            val data = module.getData();

            object.addProperty("name", data.getName());
            object.addProperty("description", data.getDescription());
            object.addProperty("category", data.getCategory().toString());
            object.addProperty("keyCode", data.getKeyCode());

            if (module.getClass().equals(ClickGui.class)) {
                object.addProperty("enabled", false);
            } else {
                object.addProperty("enabled", data.isEnabled());
            }
            object.addProperty("mode", data.getCurrentMode());

            val container = Lite.SETTING_FACTORY.get(module);

            if (!container.settings.isEmpty()) {
                JsonArray settingsArray = new JsonArray();

                container.settings.forEach(setting -> {
                    JsonObject settingObject = new JsonObject();

                    settingObject.addProperty("name", setting.getName());

                    if (setting instanceof BooleanSetting) {

                        BooleanSetting booleanSetting = (BooleanSetting) setting;
                        settingObject.addProperty("value", booleanSetting.get());
                    } else if (setting instanceof ByteNumberSetting) {

                        ByteNumberSetting byteNumberSetting = (ByteNumberSetting) setting;
                        settingObject.addProperty("value", byteNumberSetting.get());
                    } else if (setting instanceof DoubleNumberSetting) {

                        DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting) setting;
                        settingObject.addProperty("value", doubleNumberSetting.get());
                    } else if (setting instanceof FloatNumberSetting) {

                        FloatNumberSetting floatNumberSetting = (FloatNumberSetting) setting;
                        settingObject.addProperty("value", floatNumberSetting.get());
                    } else if (setting instanceof IntNumberSetting) {

                        IntNumberSetting intNumberSetting = (IntNumberSetting) setting;
                        settingObject.addProperty("value", intNumberSetting.get());
                    } else if (setting instanceof ShortNumberSetting) {

                        ShortNumberSetting shortNumberSetting = (ShortNumberSetting) setting;
                        settingObject.addProperty("value", shortNumberSetting.get());
                    } else if (setting instanceof EnumSetting) {

                        EnumSetting enumSetting = (EnumSetting) setting;
                        settingObject.addProperty("value", enumSetting.get());
                    }

                    settingsArray.add(settingObject);
                });

                object.add("settings", settingsArray);
            }

            if (!container.settingManager.isEmpty()) {
                JsonArray settingsArray = new JsonArray();

                container.settingManager.forEach((moduleMode, settings) -> {
                    if (!settings.isEmpty()) {
                        JsonObject modeObject = new JsonObject();

                        modeObject.addProperty("name", moduleMode.getName());

                        JsonArray modeSettings = new JsonArray();

                        settings.forEach(setting -> {
                            JsonObject settingObj = new JsonObject();

                            settingObj.addProperty("name", setting.getName());

                            if (setting instanceof BooleanSetting) {

                                BooleanSetting booleanSetting = (BooleanSetting) setting;
                                settingObj.addProperty("value", booleanSetting.get());
                            } else if (setting instanceof ByteNumberSetting) {

                                ByteNumberSetting byteNumberSetting = (ByteNumberSetting) setting;
                                settingObj.addProperty("value", byteNumberSetting.get());
                            } else if (setting instanceof DoubleNumberSetting) {

                                DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting) setting;
                                settingObj.addProperty("value", doubleNumberSetting.get());
                            } else if (setting instanceof FloatNumberSetting) {

                                FloatNumberSetting floatNumberSetting = (FloatNumberSetting) setting;
                                settingObj.addProperty("value", floatNumberSetting.get());
                            } else if (setting instanceof IntNumberSetting) {

                                IntNumberSetting intNumberSetting = (IntNumberSetting) setting;
                                settingObj.addProperty("value", intNumberSetting.get());
                            } else if (setting instanceof ShortNumberSetting) {

                                ShortNumberSetting shortNumberSetting = (ShortNumberSetting) setting;
                                settingObj.addProperty("value", shortNumberSetting.get());
                            } else if (setting instanceof EnumSetting) {

                                EnumSetting enumSetting = (EnumSetting) setting;
                                settingObj.addProperty("value", enumSetting.get());
                            }

                            modeSettings.add(settingObj);
                        });

                        modeObject.add("settings", modeSettings);

                        settingsArray.add(modeObject);
                    }
                });

                object.add("modeSettings", settingsArray);
            }

            array.add(object);
        });

        writeFile(gson.toJson(array), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        val dataEntries = gson.fromJson(readFile(file), DataEntry[].class);

        for (DataEntry entry : dataEntries) {
            val module = Lite.MODULE_FACTORY.findByName(entry.getName());

            if (module != null) {

                val data = module.getData();
                data.setKeyCode(entry.getKeyCode());

                int currentMode;
                if ((currentMode = entry.getCurrentMode()) != -1) {
                    module.setCurrentMode(currentMode);
                }

                if (entry.settings != null) {
                    for (SettingEntry settingEntry : entry.settings) {

                        Setting setting = Lite.SETTING_FACTORY.getSetting(module, settingEntry.name);
                        if (setting != null) {

                            if (setting instanceof BooleanSetting) {

                                BooleanSetting booleanSetting = (BooleanSetting) setting;
                                booleanSetting.set(Boolean.parseBoolean(settingEntry.value.toString()));
                            } else if (setting instanceof ByteNumberSetting) {

                                ByteNumberSetting byteNumberSetting = (ByteNumberSetting) setting;
                                byteNumberSetting.set((byte) Double.parseDouble(settingEntry.value.toString()));
                            } else if (setting instanceof DoubleNumberSetting) {

                                DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting) setting;
                                doubleNumberSetting.set(Double.parseDouble(settingEntry.value.toString()));
                            } else if (setting instanceof FloatNumberSetting) {

                                FloatNumberSetting floatNumberSetting = (FloatNumberSetting) setting;
                                floatNumberSetting.set(Float.parseFloat(settingEntry.value.toString()));
                            } else if (setting instanceof IntNumberSetting) {
                                IntNumberSetting intNumberSetting = (IntNumberSetting) setting;
                                intNumberSetting.set((int) Double.parseDouble(settingEntry.value.toString()));
                            } else if (setting instanceof ShortNumberSetting) {

                                ShortNumberSetting shortNumberSetting = (ShortNumberSetting) setting;
                                shortNumberSetting.set((short) Double.parseDouble(settingEntry.value.toString()));
                            } else if (setting instanceof EnumSetting) {

                                EnumSetting enumSetting = (EnumSetting) setting;
                                enumSetting.set(settingEntry.value.toString());
                            }
                        }
                    }
                }

                if (entry.modeSettings != null) {
                    for (ModeSettings modeSettings : entry.modeSettings) {
                        for (SettingEntry settingEntry : modeSettings.settings) {

                            Setting setting = Lite.SETTING_FACTORY.get(module).getModeSetting(modeSettings.name, settingEntry.name);
                            if (setting != null) {

                                if (setting instanceof BooleanSetting) {

                                    BooleanSetting booleanSetting = (BooleanSetting) setting;
                                    booleanSetting.set(Boolean.parseBoolean(settingEntry.value.toString()));
                                } else if (setting instanceof ByteNumberSetting) {

                                    ByteNumberSetting byteNumberSetting = (ByteNumberSetting) setting;
                                    byteNumberSetting.set((byte) Double.parseDouble(settingEntry.value.toString()));
                                } else if (setting instanceof DoubleNumberSetting) {

                                    DoubleNumberSetting doubleNumberSetting = (DoubleNumberSetting) setting;
                                    doubleNumberSetting.set(Double.parseDouble(settingEntry.value.toString()));
                                } else if (setting instanceof FloatNumberSetting) {

                                    FloatNumberSetting floatNumberSetting = (FloatNumberSetting) setting;
                                    floatNumberSetting.set(Float.parseFloat(settingEntry.value.toString()));
                                } else if (setting instanceof IntNumberSetting) {
                                    IntNumberSetting intNumberSetting = (IntNumberSetting) setting;
                                    intNumberSetting.set((int) Double.parseDouble(settingEntry.value.toString()));
                                } else if (setting instanceof ShortNumberSetting) {

                                    ShortNumberSetting shortNumberSetting = (ShortNumberSetting) setting;
                                    shortNumberSetting.set((short) Double.parseDouble(settingEntry.value.toString()));
                                } else if (setting instanceof EnumSetting) {

                                    EnumSetting enumSetting = (EnumSetting) setting;
                                    enumSetting.set(settingEntry.value.toString());
                                }
                            }
                        }
                    }
                }

                module.toggle(entry.isEnabled());
            }
        }
    }

    @Override
    public void setFile(File root) {
        file = new File(root, "/modules.json");
    }

    @Getter
    private static class DataEntry extends ModuleData {

        @SerializedName("settings")
        private final SettingEntry[] settings;

        @SerializedName("modeSettings")
        private final ModeSettings[] modeSettings;

        public DataEntry(String name, String description, Category category, int keyCode, int currentMode, boolean enabled, SettingEntry[] settings, ModeSettings[] modeSettings) {
            super(name, description, category, keyCode, currentMode, enabled);
            this.settings = settings;
            this.modeSettings = modeSettings;
        }

        @Override
        public String toString() {
            return "DataEntry{" +
                    "settings=" + Arrays.toString(settings) +
                    ", modeSettings=" + Arrays.toString(modeSettings) +
                    '}';
        }
    }

    @Getter
    @AllArgsConstructor
    private static class SettingEntry {

        @SerializedName("name")
        private final String name;

        @SerializedName("value")
        private final Object value;

        @Override
        public String toString() {
            return "SettingEntry{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @AllArgsConstructor
    private static class ModeSettings {

        @SerializedName("name")
        private final String name;

        @SerializedName("settings")
        private final SettingEntry[] settings;

        @Override
        public String toString() {
            return "ModeSettings{" +
                    "name='" + name + '\'' +
                    ", settings=" + Arrays.toString(settings) +
                    '}';
        }
    }

}
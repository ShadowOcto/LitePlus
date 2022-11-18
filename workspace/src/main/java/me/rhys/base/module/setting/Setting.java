package me.rhys.base.module.setting;

import lombok.Getter;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;

@Getter
public class Setting {

    protected final Object object;
    protected final Field field;

    public Setting(Object object, Field field) {
        this.object = object;
        this.field = field;

        if (!this.field.isAnnotationPresent(Name.class)) {
            Minecraft.logger.error(field.getName() + " does not have a name annotation");
        }
    }

    public String getName() {
        return field.getAnnotation(Name.class).value();
    }

}
package me.rhys.base.module;


import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.util.container.Container;

import java.util.stream.Stream;

public class ModuleFactory extends Container<Module> {

    public long lastToggleTime = System.currentTimeMillis();

    @EventTarget
    void keyInput(KeyboardInputEvent event) {
        // toggle needed modules
        Lite.MODULE_FACTORY
                .filter(module -> module.getData().getKeyCode() == event.getKeyCode())
                .forEach(module -> {
                    module.toggle(!module.getData().isEnabled());
                    lastToggleTime = System.currentTimeMillis();
                });
    }

    public Module findByName(String name) {
        return find(module -> module.getData().getName().equalsIgnoreCase(name));
    }

    public Module findByClass(Class clazz) {
        return find(module -> module.getClass() == clazz);
    }

    public Stream<Module> getActiveModules() {
        return filter(module -> module.getData().isEnabled());
    }

    public Stream<Module> getDisabledModules() {
        return filter(module -> !module.getData().isEnabled());
    }

}
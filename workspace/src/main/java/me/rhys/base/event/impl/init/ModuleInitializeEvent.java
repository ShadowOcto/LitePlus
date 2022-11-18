package me.rhys.base.event.impl.init;

import lombok.AllArgsConstructor;
import me.rhys.base.event.Event;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleFactory;

import java.util.Arrays;

@AllArgsConstructor
public class ModuleInitializeEvent extends Event {

    private final ModuleFactory factory;

    public void register(Module module) {
        factory.add(module);
    }

    public void register(Module... modules) {
        Arrays.stream(modules).forEach(this::register);
    }
}

package me.rhys.base.event.impl.init;

import lombok.AllArgsConstructor;
import me.rhys.base.command.Command;
import me.rhys.base.command.CommandFactory;
import me.rhys.base.event.Event;

import java.util.Arrays;

@AllArgsConstructor
public class CommandInitializeEvent extends Event {

    private final CommandFactory factory;

    public void register(Command command) {
        factory.add(command);
    }

    public void register(Command... commands) {
        Arrays.stream(commands).forEach(this::register);
    }

}

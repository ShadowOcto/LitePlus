package me.rhys.base.event.impl;

import lombok.Getter;
import lombok.Setter;
import me.rhys.base.event.Event;

@Getter
@Setter
public class InitializeEvent extends Event {

    private String name;
    private String version;

}

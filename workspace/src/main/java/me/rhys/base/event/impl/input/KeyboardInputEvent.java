package me.rhys.base.event.impl.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rhys.base.event.Event;

@Getter
@AllArgsConstructor
public class KeyboardInputEvent extends Event {

    private final int keyCode;

}

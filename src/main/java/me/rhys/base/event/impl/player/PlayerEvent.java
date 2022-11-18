package me.rhys.base.event.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.rhys.base.event.Event;
import net.minecraft.client.entity.EntityPlayerSP;

@Getter
@AllArgsConstructor
public class PlayerEvent extends Event {

    private final EntityPlayerSP player;

}
package me.rhys.base.event.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerUpdateEvent extends PlayerEvent {

    public PlayerUpdateEvent(EntityPlayerSP player) {
        super(player);
    }

}

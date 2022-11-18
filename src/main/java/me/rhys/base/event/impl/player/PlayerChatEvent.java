package me.rhys.base.event.impl.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.EntityPlayerSP;

@Setter
@Getter
public class PlayerChatEvent extends PlayerEvent {

    private String message;

    public PlayerChatEvent(EntityPlayerSP player, String message) {
        super(player);
        this.message = message;
    }

}

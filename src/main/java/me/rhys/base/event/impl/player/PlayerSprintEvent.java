package me.rhys.base.event.impl.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.EntityPlayerSP;

@Setter
@Getter
public class PlayerSprintEvent extends PlayerEvent {

    private boolean sprinting;

    public PlayerSprintEvent(EntityPlayerSP playerSP, boolean sprinting) {
        super(playerSP);
    }
}

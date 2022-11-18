package me.rhys.base.event.impl.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.rhys.base.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@Getter
@AllArgsConstructor
public class PlayerCollisionEvent extends Event {

    private final Entity entity;

    @Setter
    private AxisAlignedBB box;

    private final BlockPos pos;

}
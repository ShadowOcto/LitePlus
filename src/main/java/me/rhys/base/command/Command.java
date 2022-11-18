package me.rhys.base.command;

import lombok.Getter;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.Arrays;

@Getter
public abstract class Command {

    private final String label;
    private final String usage;
    private final String description;
    private final String[] aliases;

    public Command(String label, String usage, String description, String... aliases) {
        this.label = label;
        this.usage = usage;
        this.description = description;
        this.aliases = aliases;
    }

    public Command(String label, String description, String... aliases) {
        this(label, "", description, aliases);
    }

    public abstract boolean handle(EntityPlayerSP player, String label, String[] args);

    public boolean isAlias(String label) {
        if (this.label.equalsIgnoreCase(label)
                || Arrays.stream(aliases).filter(s -> s.equalsIgnoreCase(label)).findFirst().orElse(null) != null) {
            return true;
        }
        return false;
    }

}

package me.rhys.client.command;

import me.rhys.base.Lite;
import me.rhys.base.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

public class HelpCommand extends Command {

    public HelpCommand(String label, String description, String... aliases) {
        super(label, description, aliases);
    }

    @Override
    public boolean handle(EntityPlayerSP player, String label, String[] args) {
        if (args != null) {
            Command command = Lite.COMMAND_FACTORY.find(c -> c.isAlias(args[0]));
            if (command == null) {
                player.sendMessage("Please provide a valid command");
                return true;
            }
            if (!command.getUsage().isEmpty()) {
                player.sendMessage(command.getUsage());
            } else {
                player.sendMessage("Command does not have a specified usage");
            }
        } else {
            player.sendMessage("Commands: ");
            Lite.COMMAND_FACTORY.forEach(command -> player.sendMessage(EnumChatFormatting.GOLD + command.getLabel() + ": " + EnumChatFormatting.RESET + command.getDescription()));
        }
        return true;
    }

}

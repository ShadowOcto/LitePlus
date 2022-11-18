package me.rhys.client.command;

import me.rhys.base.Lite;
import me.rhys.base.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created on 13/09/2020 Package me.rhys.client.command
 */
public class FriendCommand extends Command {
    public FriendCommand(String label, String usage, String description, String... aliases) {
        super(label, usage, description, aliases);
    }

    @Override
    public boolean handle(EntityPlayerSP player, String label, String[] args) {
        if (args != null) {

            if (args.length > 0 && args[0].equalsIgnoreCase("list")) {

                if (Lite.FRIEND_MANAGER.getFriendList().size() > 0) {
                    player.sendMessage("Friends list:");

                    Lite.FRIEND_MANAGER.getFriendList().forEach(friend -> {
                        if (friend.getAlias() != null) {
                            player.sendMessage(" - " + EnumChatFormatting.GREEN + friend.getName() + EnumChatFormatting.GRAY + " (" + friend.getAlias() + ")");
                        } else {
                            player.sendMessage(" - " + EnumChatFormatting.GREEN + friend.getName());
                        }
                    });
                } else {
                    player.sendMessage("you have no fucking friends lmfaooo");
                }

                return true;
            }

            if (args.length >= 2) {

                if (args[1].length() > 1) {
                    switch (args[0].toLowerCase()) {
                        case "add": {

                            if (Lite.FRIEND_MANAGER.getFriend(args[1]) == null) {
                                Lite.FRIEND_MANAGER.addFriend(args[1]);
                                player.sendMessage("Added " + args[1] + " as a friend");
                            } else {
                                player.sendMessage("You already have " + args[1] + " as a friend");
                            }
                            return true;
                        }

                        case "remove": {
                            if (Lite.FRIEND_MANAGER.getFriend(args[1]) != null) {
                                Lite.FRIEND_MANAGER.removeFriend(args[1]);
                                player.sendMessage("Removed " + args[1] + " as a friend");
                            } else {
                                player.sendMessage(args[1] + " is not added!");
                            }
                            return true;
                        }
                    }
                } else {
                    player.sendMessage("Supply a name to add, remove, list");
                }
            }
        }
        return false;
    }
}

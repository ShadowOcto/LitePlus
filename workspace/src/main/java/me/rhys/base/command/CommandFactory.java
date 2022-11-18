package me.rhys.base.command;

import lombok.AllArgsConstructor;
import lombok.var;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerChatEvent;
import me.rhys.base.util.ArrayUtil;
import me.rhys.base.util.container.Container;

@AllArgsConstructor
public class CommandFactory extends Container<Command> {

    private final char prefix;

    @EventTarget
    void playerChat(PlayerChatEvent event) {
        var player = event.getPlayer();
        if (player == null) {
            return;
        }

        var message = event.getMessage();
        if (message == null) {
            return;
        }

        // check that message starts with wanted prefix
        if (message.charAt(0) != prefix) {
            return;
        }

        // stop the message from going through
        event.setCancelled(true);

        // offset the message to remove the prefix
        message = message.substring(1);

        // command data
        var label = message.split(" ")[0];
        var args = ArrayUtil.offset(message.split(" "), 1);

        if (label.isEmpty()) {
            player.sendMessage("Please provide a valid command");
            return;
        }

        // find the command
        var command = find(c -> c.isAlias(label));
        if (command == null) {
            player.sendMessage("Could not find a \"" + label + "\" command.");
            return;
        }

        if (!command.handle(player, label, args)) {
            player.sendMessage(command.getUsage());
        }
    }

}

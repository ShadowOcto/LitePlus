package me.rhys.client.command;

import me.rhys.base.Lite;
import me.rhys.base.command.Command;
import me.rhys.base.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand(String label, String usage, String description, String... aliases) {
        super(label, usage, description, aliases);
    }

    @Override
    public boolean handle(EntityPlayerSP player, String label, String[] args) {
        if (args != null) {
            if (args.length >= 2) {
                Module module = Lite.MODULE_FACTORY.findByName(args[1]);
                if (module == null) {
                    player.sendMessage("\"" + args[1] + "\" not recognized as a module");
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "add":
                        if (args.length < 3) {
                            return false;
                        }
                        module.getData().setKeyCode(Keyboard.getKeyIndex(args[2].toUpperCase()));
                        player.sendMessage("\"" + module.getData().getName() + "\" now has a new keybind");
                        break;
                    case "remove":
                        module.getData().setKeyCode(Keyboard.KEY_NONE);
                        player.sendMessage("Keybind has been removed from \"" + module.getData().getName() + "\"");
                        break;
                    default:
                        return false;
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

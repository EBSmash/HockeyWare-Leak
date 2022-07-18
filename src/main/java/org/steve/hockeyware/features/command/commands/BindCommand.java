package org.steve.hockeyware.features.command.commands;

import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.HockeyWare;
import org.steve.hockeyware.features.command.Command;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.util.client.ClientMessage;

import java.util.List;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "Allows you to bind modules", "bind" + " " + "[module]" + " " + "[key]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 2) {
            for (Module module : HockeyWare.INSTANCE.moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(args.get(0))) {
                    if (args.get(0).isEmpty()) {
                        ClientMessage.sendMessage("Please only enter one character");
                        return;
                    }
                    String bind = args.get(1);
                    int key = Keyboard.getKeyIndex(bind.toUpperCase());
                    if (key == 0) {
                        ClientMessage.sendMessage("Unknown Keybind");
                        return;
                    }
                    module.setKeybind(key);
                    ClientMessage.sendMessage(module.getName() + " has been bound to " + Keyboard.getKeyName(module.getKeybind()) + ".");
                }
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}

package org.steve.hockeyware.features.command.commands;

import org.steve.hockeyware.features.command.Command;
import org.steve.hockeyware.manager.CommandManager;
import org.steve.hockeyware.util.client.ClientMessage;

import java.util.List;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "Allows you to change prefix", "prefix [prefix]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            CommandManager.setPrefix(args.get(0));
            ClientMessage.sendMessage("Set the prefix to " + args.get(0) + ".");
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}

package org.steve.hockeyware.features.command.commands;

import net.minecraft.util.text.TextFormatting;
import org.steve.hockeyware.features.command.Command;
import org.steve.hockeyware.util.client.ClientMessage;

import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "shows all commands", "help");
    }

    @Override
    public void runCommand(List<String> args) {
        try {
            for (Command command : getHockey().commandManager.getCommands()) {
                ClientMessage.sendMessage(TextFormatting.WHITE + command.getName() + TextFormatting.GRAY + " " + command.getDescription() + " syntax: " + command.getSyntax());
            }
        } catch (Exception ignored) {
        }
    }
}

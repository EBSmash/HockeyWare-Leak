package org.steve.hockeyware.manager;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.steve.hockeyware.features.command.Command;
import org.steve.hockeyware.features.command.commands.BindCommand;
import org.steve.hockeyware.features.command.commands.FriendCommand;
import org.steve.hockeyware.features.command.commands.HelpCommand;
import org.steve.hockeyware.features.command.commands.PrefixCommand;
import org.steve.hockeyware.util.client.ClientMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public static String prefix = "-";
    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new BindCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new PrefixCommand());
    }

    public static void setPrefix(String prefix) {
        CommandManager.prefix = prefix;
    }

    @SubscribeEvent
    public void chatEvent(ClientChatEvent event) {
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);

            List<String> args = Arrays.asList(event.getMessage().substring(prefix.length()).trim().split(" "));
            if (args.isEmpty()) {
                return;
            }

            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(args.get(0))) {
                    command.runCommand(args.subList(1, args.size()));
                    return;
                }
            }

            ClientMessage.sendMessage("Invalid command, try " + prefix + "help");
        }
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}

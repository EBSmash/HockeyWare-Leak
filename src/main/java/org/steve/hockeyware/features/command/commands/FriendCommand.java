package org.steve.hockeyware.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.steve.hockeyware.HockeyWare;
import org.steve.hockeyware.features.command.Command;
import org.steve.hockeyware.manager.friend.Friend;
import org.steve.hockeyware.util.client.ClientMessage;
import org.steve.hockeyware.util.client.PlayerUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "Adds friends to the system", "friend" + " " + "[add/del/list]" + " " + "[playername]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if (args.size() >= 2) {
                if (args.get(0).equalsIgnoreCase("add") && !getHockey().friendManager.isFriendByName(args.get(1))) {
                    UUID frienduuid = PlayerUtil.getUUIDFromName(args.get(1));
                    HockeyWare.INSTANCE.friendManager.add(new Friend(frienduuid, args.get(1)));
                    assert frienduuid != null;
                    ClientMessage.sendMessage(Objects.requireNonNull(mc.world.getPlayerEntityByUUID(frienduuid)).getName() + " has been " + ChatFormatting.GREEN + "friended!");
                }
                if (args.get(0).equalsIgnoreCase("del") && getHockey().friendManager.isFriendByName(args.get(1))) {
                    UUID frienduuid = PlayerUtil.getUUIDFromName(args.get(1));
                    Friend friend = HockeyWare.INSTANCE.friendManager.getFriend(frienduuid);
                    HockeyWare.INSTANCE.friendManager.remove(friend);
                    ClientMessage.sendMessage(friend.getAlias() + " has been " + ChatFormatting.RED + "unfriended!");
                }
            }
            if (args.get(0).equalsIgnoreCase("list")) {
                if (HockeyWare.INSTANCE.friendManager.getFriends().isEmpty()) {
                    ClientMessage.sendMessage("You have no friends.");
                } else {
                    for (Friend friend : HockeyWare.INSTANCE.friendManager.getFriends()) {
                        ClientMessage.sendMessage(friend.getAlias());
                    }
                }
            }
        } else {
            ClientMessage.sendErrorMessage(getSyntax());
        }
    }
}

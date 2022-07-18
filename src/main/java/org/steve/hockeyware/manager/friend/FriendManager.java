package org.steve.hockeyware.manager.friend;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class FriendManager {
    EntityPlayer friendedPlayer;
    UUID frienduuid;
    private ArrayList<Friend> friends = new ArrayList<>();

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friendsToAdd) {
        friends = friendsToAdd;
    }

    public void add(Friend friend) {
        this.friends.add(friend);
    }

    public void remove(Friend friend) {
        this.friends.remove(friend);
    }

    public Friend getFriend(UUID uuid) {
        return this.friends.stream().filter((friend) -> friend.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public EntityPlayer getPlayer() {
        for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
            if (player.getUniqueID().equals(getAllFriends())) {
                friendedPlayer = player;
            }
        }
        return friendedPlayer;
    }

    public UUID getAllFriends() {
        for (Friend friend : getFriends()) {
            frienduuid = friend.getUuid();
        }
        return frienduuid;
    }

    public boolean isFriendByName(String alias) {
        for (Friend friend : getFriends()) {
            if (friend.getAlias().equals(alias)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFriend(UUID uuid) {
        return this.friends.stream().anyMatch((friend) -> friend.getUuid().equals(uuid));
    }
}

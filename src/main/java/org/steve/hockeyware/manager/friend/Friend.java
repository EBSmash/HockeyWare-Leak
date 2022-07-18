package org.steve.hockeyware.manager.friend;

import java.util.UUID;

public class Friend {
    private final UUID uuid;
    private final String alias;

    public Friend(UUID uuid, String alias) {
        this.uuid = uuid;
        this.alias = alias;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAlias() {
        return this.alias == null ? ("Friend" + this.hashCode()) : this.alias;
    }

}

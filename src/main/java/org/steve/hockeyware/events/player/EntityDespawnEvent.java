package org.steve.hockeyware.events.player;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.steve.hockeyware.features.Globals;

public class EntityDespawnEvent extends Event implements Globals {
    private final Entity entity;

    public EntityDespawnEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}

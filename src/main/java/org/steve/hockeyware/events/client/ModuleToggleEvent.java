package org.steve.hockeyware.events.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.steve.hockeyware.features.module.Module;

public class ModuleToggleEvent extends Event {

    public final Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}

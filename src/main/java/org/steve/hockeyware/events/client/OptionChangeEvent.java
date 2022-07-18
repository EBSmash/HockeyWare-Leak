package org.steve.hockeyware.events.client;


import net.minecraftforge.fml.common.eventhandler.Event;
import org.steve.hockeyware.setting.Setting;

public class OptionChangeEvent extends Event {
    private final Setting setting;

    public OptionChangeEvent(Setting setting) {
        this.setting = setting;
    }

    public Setting getOption() {
        return setting;
    }
}
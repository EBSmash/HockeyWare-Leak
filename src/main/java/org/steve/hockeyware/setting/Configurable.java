package org.steve.hockeyware.setting;

import org.steve.hockeyware.features.Globals;

import java.util.ArrayList;
import java.util.Arrays;

public class Configurable implements Globals {
    protected final ArrayList<Setting> settings = new ArrayList<>();

    public void register() {
        Arrays.stream(getClass().getDeclaredFields())
                .filter((field) -> Setting.class.isAssignableFrom(field.getType()))
                .forEach((field) -> {
                    field.setAccessible(true);

                    try {
                        settings.add((Setting) field.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }
}

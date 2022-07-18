package org.steve.hockeyware.util.client;


import org.steve.hockeyware.features.Globals;

public class ScaleUtil implements Globals {
    public static float centerTextY(float y, float height) {
        return (y + (height / 2.0f)) - (mc.fontRenderer.FONT_HEIGHT / 2.0f);
    }
}

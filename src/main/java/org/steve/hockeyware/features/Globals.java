package org.steve.hockeyware.features;

import net.minecraft.client.Minecraft;
import org.steve.hockeyware.HockeyWare;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();

    default boolean fullNullCheck() {
        return mc.world != null || mc.player != null;
    }

    default HockeyWare getHockey() {
        return HockeyWare.INSTANCE;
    }
}

package org.steve.hockeyware.features.module.modules;

import org.steve.hockeyware.features.RPC;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.setting.Setting;

public class RPCModule extends Module {

    public static final Setting<Boolean> showIP = new Setting<>("Show IP", true);

    public static RPCModule INSTANCE = new RPCModule();

    public RPCModule() {
        super("DiscordRPC", "Makes a Discord Rich Presence", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        RPC.start();
    }

    @Override
    public void onDisable() {
        RPC.stop();
    }
}
package org.steve.hockeyware.features;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreenServerList;
import org.steve.hockeyware.features.module.modules.RPCModule;

public class RPC implements Globals {
    private static final DiscordRichPresence presence = new DiscordRichPresence();
    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;

    public static void start() {
        DiscordEventHandlers handler = new DiscordEventHandlers();
        rpc.Discord_Initialize("985665782077870110", handler, true, null);
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.largeImageKey = "large";
        presence.largeImageText = "1.0";
        String server = "In Multiplayer Menu";
        rpc.Discord_UpdatePresence(presence);
        Thread thread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                presence.details =
                        mc.currentScreen instanceof GuiMainMenu
                                ? "In the Main Menu" :

                                mc.currentScreen instanceof GuiMultiplayer
                                        ? server :
                                        mc.currentScreen instanceof GuiScreenServerList
                                                ? server
                                                : "Playing " + (mc.getCurrentServerData() != null
                                                ? (RPCModule.showIP.getValue() ? "on " + mc.getCurrentServerData().serverIP
                                                : " Multiplayer") : "In Singleplayer");
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler");
        thread.start();
        handler.disconnected = ((errorCode, message) -> System.out.println("Discord RPC disconnected, errorCode: " + errorCode + ", message: " + message));
    }


    public static void stop() {
        rpc.Discord_Shutdown();
        rpc.Discord_ClearPresence();
    }
}



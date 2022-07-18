package org.steve.hockeyware;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.steve.hockeyware.features.module.modules.HUD;
import org.steve.hockeyware.features.module.modules.Notifier;
import org.steve.hockeyware.features.module.modules.RPCModule;
import org.steve.hockeyware.manager.*;
import org.steve.hockeyware.manager.friend.FriendManager;

import java.io.IOException;

@Mod(modid = HockeyWare.ID, name = HockeyWare.NAME, version = HockeyWare.VERSION)
public class HockeyWare {
    public static final String NAME = "HockeyWare";
    public static final String ID = "hockeyware";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger("hockeyware");
    @Mod.Instance
    public static HockeyWare INSTANCE;
    // client
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public FriendManager friendManager;
    public CapeManager capeManager;
    public EventManager eventManager;

    public TotemPopManager popManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        HWIDManager.hwidCheck();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(EventManager.getInstance());

        moduleManager = new ModuleManager();
        MinecraftForge.EVENT_BUS.register(moduleManager);

        commandManager = new CommandManager();
        MinecraftForge.EVENT_BUS.register(commandManager);

        eventManager = new EventManager();
        MinecraftForge.EVENT_BUS.register(eventManager);

        friendManager = new FriendManager();

        configManager = new ConfigManager();

        capeManager = new CapeManager();

        configManager.prepare();

        popManager = new TotemPopManager();

        Display.setTitle(NAME + " v" + VERSION);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws IOException {
        if (!configManager.hasRan()) {
            HUD.INSTANCE.toggle(true);
            Notifier.INSTANCE.toggle(true);
            RPCModule.INSTANCE.toggle(true);
        }
    }
}

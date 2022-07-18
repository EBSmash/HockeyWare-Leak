package org.steve.hockeyware.manager;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.features.module.modules.*;

import java.util.ArrayList;

public class ModuleManager implements Globals {
    private final ArrayList<Module> modules;

    {
        modules = Lists.newArrayList(
                //new AutoCrystal(),
                new ClickGUI(),
                new RPCModule(),
                new HUD(),
                new LightningEffect(),
                new Notifier(),
                new StrictTotem(),
                //new Weather(),
                new SelfWeb(),
                new GroundStrafe(),
                new ChestSwap()
        );
    }

    public ModuleManager() {
        modules.forEach(Module::register);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int code = Keyboard.getEventKey();
        if (mc.currentScreen == null && code != Keyboard.KEY_NONE && !Keyboard.getEventKeyState()) {
            for (Module module : modules) {
                if (module.getKeybind() == code) {
                    module.toggle(false);
                }
            }
        }
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}

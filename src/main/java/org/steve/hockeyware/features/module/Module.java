package org.steve.hockeyware.features.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.events.client.ModuleToggleEvent;
import org.steve.hockeyware.setting.Configurable;
import org.steve.hockeyware.setting.Keybind;
import org.steve.hockeyware.setting.Setting;

public class Module extends Configurable {
    private final String name;
    private final String description;
    private final Category category;
    private final Keybind keybind = new Keybind("Keybind", Keyboard.KEY_NONE);
    private final Setting<Boolean> drawn = new Setting<>("Drawn", true);
    private boolean toggled = false;

    public Module(String name, String description, Category category) {
        this(name, description, category, Keyboard.KEY_NONE);
    }

    public Module(String name, String description, Category category, int code) {
        this.name = name;
        this.category = category;
        this.description = description;

        keybind.setValue(code);

        settings.add(keybind);
        settings.add(drawn);
    }

    public String getDisplayInfo() {
        return null;
    }

    public String getFullDisplay() {
        String display = getName();
        if (getDisplayInfo() != null) {
            display += (" " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + getDisplayInfo() + ChatFormatting.GRAY + "]");
        }
        return display;
    }

    public void onUpdate() {

    }

    public void onRender3D() {

    }

    public void onRender2D() {

    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }


    public boolean isOn() {
        return this.toggled;
    }


    public void toggle(boolean silent) {
        toggled = !toggled;
        if (toggled) {
            MinecraftForge.EVENT_BUS.register(this);
            onEnable();
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
            onDisable();
        }
        if (!silent) {
            MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent(this));
        }
    }

    public boolean isDrawn() {
        return drawn.getValue();
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        if (toggled && !this.isOn()) {
            this.toggle(false);
        }
        if (!toggled && this.isOn()) {
            this.toggle(false);
        }
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }


    public int getKeybind() {
        return keybind.getValue();
    }

    public void setKeybind(int code) {
        keybind.setValue(code);
    }
}

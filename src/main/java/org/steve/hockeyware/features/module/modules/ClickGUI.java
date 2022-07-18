package org.steve.hockeyware.features.module.modules;

import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.gui.ClickGUIScreen;
import org.steve.hockeyware.setting.Setting;

public class ClickGUI extends Module {

    //old color 245, 34, 34
    //purple color 108, 0, 255
    //light blue 57, 236, 255
    public static final Setting<Integer> getred = new Setting<>("OutlineRed", 255, 0, 255);
    public static final Setting<Integer> getgreen = new Setting<>("OutlineGreen", 0, 0, 255);
    public static final Setting<Integer> getblue = new Setting<>("OutlineBlue", 0, 0, 255);
    public static final Setting<Integer> insidered = new Setting<>("InsideRed", 255, 0, 255);
    public static final Setting<Integer> insidegreen = new Setting<>("InsideGreen", 0, 0, 255);
    public static final Setting<Integer> insideblue = new Setting<>("InsideBlue", 0, 0, 255);
    public static final Setting<Integer> backgroundred = new Setting<>("BackgroundRed", 0, 0, 255);
    public static final Setting<Integer> backgroundgreen = new Setting<>("BackgroundGreen", 0, 0, 255);
    public static final Setting<Integer> backgroundblue = new Setting<>("BackgroundBlue", 0, 0, 255);
    public static final Setting<Boolean> brackets = new Setting<>("Brackets", false);
    public static final Setting<Boolean> moduleCount = new Setting<>("ModuleCount", true);
    public static final Setting<Float> lineWidth = new Setting<>("LineWidth", 3.7f, 0.1f, 10f);
    public static final Setting<Boolean> rainbow = new Setting<>("Rainbow", false);
    //public static final Setting<Integer> rainbowSpeed = new Setting<>("RainbowSpeed", 3, 0, 10);
    public static final Setting<Float> saturation = new Setting<>("Rainbow Saturation", 255f, 1, 255);
    public static final Setting<Float> brightness = new Setting<>("Rainbow Brightness", 255f, 0, 255);
    public static final Setting<Boolean> blur = new Setting<>("Blur", false);
    public static final Setting<Integer> blurAmount = new Setting<>("Blur Amount", 8, 1, 20);
    public static final Setting<Integer> blurSize = new Setting<>("Blur Size", 3, 1, 20);


    public static ClickGUI INSTANCE;

    public ClickGUI() {
        super("ClickGUI", "Shows this screen", Category.Client, Keyboard.KEY_MINUS);
        INSTANCE = this;
    }

    @Override
    protected void onEnable() {
        if (!fullNullCheck()) {
            toggle(true);
            return;
        }

        Globals.mc.displayGuiScreen(ClickGUIScreen.getInstance());
    }

    @Override
    protected void onDisable() {
        if (fullNullCheck()) {
            Globals.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void onUpdate() {
        if (Globals.mc.currentScreen == null) {
            toggle(true);
        }
    }
}

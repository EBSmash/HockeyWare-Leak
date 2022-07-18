package org.steve.hockeyware.gui.components.button;

import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.setting.Keybind;
import org.steve.hockeyware.util.client.ScaleUtil;

public class KeybindButton extends Button {
    private final Keybind setting;
    private boolean listening = false;

    public KeybindButton(Keybind setting) {
        super(setting.getName(), 0.0, 0.0, 0.0, 14.0);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        String text = listening ? "Waiting..." : (id + ": " + Keyboard.getKeyName(setting.getValue()));
        mc.fontRenderer.drawStringWithShadow(text, (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        listening = !listening;
    }

    @Override
    public void keyTyped(char character, int code) {
        if (listening) {
            listening = false;

            if (code == Keyboard.KEY_DELETE || code == Keyboard.KEY_BACK) {
                setting.setValue(Keyboard.KEY_NONE);
                return;
            }

            setting.setValue(code);
        }
    }
}

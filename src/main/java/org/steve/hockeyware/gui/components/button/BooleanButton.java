package org.steve.hockeyware.gui.components.button;

import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.modules.ClickGUI;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.client.ColorUtil;
import org.steve.hockeyware.util.client.RenderUtil;
import org.steve.hockeyware.util.client.ScaleUtil;

import java.awt.*;

public class BooleanButton extends Button {
    private final Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName(), 0.0, 0.0, 0.0, 13.0);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = -1;
        if (setting.getValue()) {
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = ColorUtil.toHex(ClickGUI.backgroundred.getValue(), ClickGUI.backgroundgreen.getValue(), ClickGUI.backgroundblue.getValue());
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }

        Globals.mc.fontRenderer.drawStringWithShadow(setting.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        setting.setValue(!setting.getValue());
    }
}

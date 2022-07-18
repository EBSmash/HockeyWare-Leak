package org.steve.hockeyware.gui.components.button;

import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.features.module.modules.ClickGUI;
import org.steve.hockeyware.gui.components.Component;
import org.steve.hockeyware.gui.components.other.Slider;
import org.steve.hockeyware.setting.Keybind;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.client.ColorUtil;
import org.steve.hockeyware.util.client.RenderUtil;
import org.steve.hockeyware.util.client.ScaleUtil;

import java.awt.*;
import java.util.ArrayList;

public class ModuleButton extends Button {
    private final Module module;
    private final ArrayList<org.steve.hockeyware.gui.components.Component> components = new ArrayList<>();

    private boolean opened = false;

    public ModuleButton(Module module) {
        super(module.getName(), 0.0, 0.0, 0.0, 13.0);
        this.module = module;

        init();
    }

    private void init() {
        for (Setting setting : module.getSettings()) {
            if (setting instanceof Keybind) {
                components.add(new KeybindButton((Keybind) setting));
            } else {
                if (setting.getValue() instanceof Boolean) {
                    components.add(new BooleanButton(setting));
                } else if (setting.getValue() instanceof Enum) {
                    components.add(new EnumButton(setting));
                } else if (setting.getValue() instanceof Number) {
                    components.add(new Slider(setting));
                }
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        int color = -1;
        if (module.isToggled()) {
            color = new Color(ClickGUI.getred.getValue(), ClickGUI.getgreen.getValue(), ClickGUI.getblue.getValue()).getRGB();
        } else if (isMouseInBounds(mouseX, mouseY)) {
            color = ColorUtil.toHex(ClickGUI.backgroundred.getValue(), ClickGUI.backgroundgreen.getValue(), ClickGUI.backgroundblue.getValue());
        }

        if (color != -1) {
            RenderUtil.drawRect(x, y, width, height, color);
        }

        Globals.mc.fontRenderer.drawStringWithShadow(module.getName(), (float) (x + 2.3), ScaleUtil.centerTextY((float) y, (float) height), -1);
        if (ClickGUI.brackets.getValue())
            Globals.mc.fontRenderer.drawStringWithShadow(opened ? "﹀" : "[]", (float) ((x + width) - 7.3), ScaleUtil.centerTextY((float) y, (float) height), -1);

        if (opened) {
            double start = 15.0;
            for (org.steve.hockeyware.gui.components.Component component : components) {
                if (!component.isVisible()) {
                    continue;
                }

                component.setX(x + 2.0);
                component.setY(y + start);
                component.setWidth(width - 4.0);

                component.drawComponent(mouseX, mouseY, partialTicks);

                start += component.getHeight() + 1.0;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (opened) {
            for (org.steve.hockeyware.gui.components.Component component : components) {
                if (component.isMouseInBounds(mouseX, mouseY)) {
                    component.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (opened) {
            components.forEach((component) -> component.mouseReleased(mouseX, mouseY, state));
        }
    }

    @Override
    public void keyTyped(char character, int code) {
        super.keyTyped(character, code);
        if (opened) {
            components.forEach((component) -> component.keyTyped(character, code));
        }
    }

    @Override
    protected void mouseClickedInFrame(int button) {
        if (button == 0) {
            module.toggle(false);
        } else if (button == 1) {
            opened = !opened;
        }
    }

    @Override
    public double getHeight() {
        double origin = 14.0;
        if (opened) {
            for (Component component : components) {
                if (component.isVisible()) {
                    origin += component.getHeight() + 1.0;
                }
            }

            origin += 1.0;
        }

        return origin;
    }
}

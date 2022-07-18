package org.steve.hockeyware.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.features.module.modules.ClickGUI;
import org.steve.hockeyware.gui.components.Panel;
import org.steve.hockeyware.gui.components.button.ModuleButton;
import org.steve.hockeyware.util.client.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClickGUIScreen extends GuiScreen implements Globals {
    private static ClickGUIScreen INSTANCE;

    private final ArrayList<Panel> panels = new ArrayList<>();

    private ClickGUIScreen() {
        double x = 4.0;
        for (Category category : Category.values()) {
            List<Module> modules = getHockey().moduleManager.getModules()
                    .stream().filter((module) -> module.getCategory().equals(category))
                    .collect(Collectors.toList());

            if (modules.isEmpty()) {
                continue;
            }

            panels.add(new Panel(category.name(), x, 4.0, 88.0, 15) {
                @Override
                public void init() {
                    modules.forEach((module) -> buttons.add(new ModuleButton(module)));
                }
            });

            x += 92.0; //amount of space in between each category panel
        }
    }

    public static ClickGUIScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGUIScreen();
        }

        return INSTANCE;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();

        int scroll = Mouse.getDWheel();
        if (scroll > 0) {
            panels.forEach((panel) -> panel.setY(panel.getY() + 10.0));
        } else if (scroll < 0) {
            panels.forEach((panel) -> panel.setY(panel.getY() - 10.0));
        }

        panels.forEach((panel) -> panel.drawComponent(mouseX, mouseY, partialTicks));
        if (ClickGUI.blur.getValue()) {
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());//
            RenderUtil.drawBlurryRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ClickGUI.blurAmount.getValue(), ClickGUI.blurSize.getValue());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        panels.forEach((panel) -> panel.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        panels.forEach((panel) -> panel.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        panels.forEach((panel) -> panel.keyTyped(typedChar, keyCode));
    }
}

package org.steve.hockeyware.features.module;

import org.steve.hockeyware.HockeyWare;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    Combat, Movement, Player, Misc, Render, Chat, Client, Exploit;

    public static int amountPerCategory(Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module module : HockeyWare.INSTANCE.moduleManager.getModules()) {
            if (module.getCategory().equals(category)) {
                categoryModules.add(module);
            }
        }
        return categoryModules.size();
    }

    public static Category getCategoryFromString(String id) {
        Category finalCategory = null;
        for (Category category : Category.values()) {
            if (category.toString().equalsIgnoreCase(id)) {
                finalCategory = category;
                break;
            }
        }
        return finalCategory;
    }
}

package org.steve.hockeyware.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lwjgl.input.Keyboard;
import org.steve.hockeyware.HockeyWare;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.manager.friend.Friend;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.client.EnumConverter;
import org.steve.hockeyware.util.client.PlayerUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConfigManager {
    public final File Hockeyware;
    public final File Settings;
    public final File Other;
    public final File Friends;


    public ConfigManager() {
        this.Hockeyware = new File("HockeyWare");
        if (!this.Hockeyware.exists()) {
            this.Hockeyware.mkdirs();
        }

        this.Settings = new File("HockeyWare" + File.separator + "Settings");
        if (!this.Settings.exists()) {
            this.Settings.mkdirs();
        }
        this.Other = new File("HockeyWare" + File.separator + "Other");
        if (Other.exists()) {
            this.Other.mkdirs();
        }
        this.Friends = new File("HockeyWare" + File.separator + "Other" + File.separator + "Friends");
        if (Friends.exists()) {
            this.Friends.mkdirs();
        }
        load();
        loadFriends();
        loadPrefix();
    }

    public void saveFriends() {
        try {
            File friendFile = new File(Hockeyware.getAbsolutePath(), "Other/" + "Friends" + ".json");
            friendFile.mkdirs();
            if (!friendFile.exists()) {
                friendFile.createNewFile();
            }
            for (Friend friend : HockeyWare.INSTANCE.friendManager.getFriends()) {
                JsonObject object = new JsonObject();
                object.addProperty("name", friend.getAlias());
                FileWriter fileWriter = new FileWriter(friendFile);
                fileWriter.write(object.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (Exception e) {
        }
    }

    public void loadFriends() {
        try {
            File friendFile = new File(Hockeyware.getAbsolutePath(), "Other/" + "Friends" + ".json");
            friendFile.getParentFile().mkdirs();
            if (!friendFile.exists())
                friendFile.createNewFile();
            for (Friend friend : HockeyWare.INSTANCE.friendManager.getFriends()) {
                String content = Files.readAllLines(friendFile.toPath()).stream().collect(Collectors.joining());
                JsonObject object = new JsonParser().parse(content).getAsJsonObject();
                String name = object.get("name").getAsString();
                UUID frienduuid = PlayerUtil.getUUIDFromName(name);
                Friend friend2 = HockeyWare.INSTANCE.friendManager.getFriend(frienduuid);
                HockeyWare.INSTANCE.friendManager.add(friend2);
            }
        } catch (Exception e) {
        }
    }

    public void prepare() {
        Runtime.getRuntime().addShutdownHook(new SaveThread());
    }

    public void savePrefix() {
        try {
            File friendFile = new File(Hockeyware.getAbsolutePath(), "Other/" + "Prefix" + ".json");
            friendFile.mkdirs();
            if (!friendFile.exists()) {
                friendFile.createNewFile();
            }
            JsonObject object = new JsonObject();
            object.addProperty("prefix", CommandManager.prefix);
            FileWriter fileWriter = new FileWriter(friendFile);
            fileWriter.write(object.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
        }
    }

    public void loadPrefix() {
        try {
            File prefixFile = new File(Hockeyware.getAbsolutePath(), "Other/" + "Prefix" + ".json");
            prefixFile.getParentFile().mkdirs();
            if (!prefixFile.exists()) prefixFile.createNewFile();
            String content = Files.readAllLines(prefixFile.toPath()).stream().collect(Collectors.joining());
            JsonObject object = new JsonParser().parse(content).getAsJsonObject();
            String prefix = object.get("prefix").getAsString();
            CommandManager.setPrefix(prefix);

        } catch (Exception e) {
        }

    }

    public void save() {
        try {
            for (Module module : HockeyWare.INSTANCE.moduleManager.getModules()) {
                File moduleFile = new File(Hockeyware.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists()) moduleFile.createNewFile();
                JsonObject object = new JsonObject();
                object.addProperty("bind", Keyboard.getKeyName(module.getKeybind()));
                object.addProperty("enabled", module.isOn());
                for (Setting value : module.getSettings()) {
                    if (value.isBoolean()) object.addProperty(value.getName(), (Boolean) value.getValue());
                    if (value.isNumber()) object.addProperty(value.getName(), (Number) value.getValue());
                    if (value.isEnum()) object.addProperty(value.getName(), ((Enum) value.getValue()).name());
                }
                FileWriter fileWriter = new FileWriter(moduleFile);
                fileWriter.write(object.toString());
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (Exception e) {
        }
    }

    public void load() {
        for (Module module : HockeyWare.INSTANCE.moduleManager.getModules()) {
            try {
                File moduleFile = new File(Hockeyware.getAbsolutePath(), "Settings/" + module.getCategory() + "/" + module.getName() + ".json");
                moduleFile.getParentFile().mkdirs();
                if (!moduleFile.exists()) moduleFile.createNewFile();
                String content = Files.readAllLines(moduleFile.toPath()).stream().collect(Collectors.joining());
                JsonObject object = new JsonParser().parse(content).getAsJsonObject();
                int bind = Keyboard.getKeyIndex(object.get("bind").getAsString());
                module.setKeybind(bind);
                module.setToggled(object.get("enabled").getAsBoolean());
                for (Setting value : module.getSettings()) {
                    JsonElement element = object.get(value.getName());
                    if (value.isBoolean()) value.setValue(element.getAsBoolean());
                    if (value.isNumber()) {
                        if (value.getValue() instanceof Integer)
                            value.setValue(element.getAsNumber().intValue());
                        if (value.getValue() instanceof Double)
                            value.setValue(element.getAsNumber().doubleValue());
                        if (value.getValue() instanceof Float)
                            value.setValue(element.getAsNumber().floatValue());
                        if (value.getValue() instanceof Long)
                            value.setValue(element.getAsNumber().longValue());
                        if (value.getValue() instanceof Byte)
                            value.setValue(element.getAsNumber().byteValue());
                        if (value.getValue() instanceof Short)
                            value.setValue(element.getAsNumber().shortValue());
                    }
                    if (value.isEnum()) {
                        EnumConverter converter = new EnumConverter(((Enum) value.getValue()).getClass());
                        value.setValue(converter.doBackward(element));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean hasRan() throws IOException {
        String FILE_NAME = "HockeyWare/Other/HasRan.txt";
        if (new File(FILE_NAME).isFile()) {
            return true;
        } else {
            Path newFilePath = Paths.get(FILE_NAME);
            Files.createFile(newFilePath);
            return false;
        }

    }

    public static class SaveThread extends Thread {
        @Override
        public void run() {
            HockeyWare.INSTANCE.configManager.save();
            HockeyWare.INSTANCE.configManager.savePrefix();
            HockeyWare.INSTANCE.configManager.saveFriends();
        }
    }
}

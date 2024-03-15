package net.hauntedstudio.filemanager;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerSettingsManager {

    private final File settingsFile = new File("config/ServerBackendMod/player-settings.json");
    private final Map<UUID, JsonObject> playerSettings = new HashMap<>();
    private final Gson gson = new Gson();

    public PlayerSettingsManager() {
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadSettings();
        saveSettings();
    }

    private void loadSettings() {
        if (settingsFile.length() != 0) {
            try (FileReader reader = new FileReader(settingsFile)) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    UUID uuid = UUID.fromString(entry.getKey());
                    JsonObject settingsObject = (JsonObject) entry.getValue();
                    playerSettings.put(uuid, settingsObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
                System.err.println("Invalid JSON format in player-settings.json");
            }
        }
    }


    public void saveSettings() {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<UUID, JsonObject> entry : playerSettings.entrySet()) {
                jsonObject.add(entry.getKey().toString(), entry.getValue());
            }
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerSetting(UUID uuid, String key, String value) {
        JsonObject settingsObject = playerSettings.get(uuid);
        if (settingsObject == null) {
            settingsObject = new JsonObject();
            playerSettings.put(uuid, settingsObject);
        }
        settingsObject.addProperty(key, value);
        saveSettings();
    }


    public String getPlayerSetting(UUID uuid, String key) {
        JsonObject settingsObject = playerSettings.get(uuid);
        if (settingsObject != null && settingsObject.has(key)) {
            return settingsObject.get(key).getAsString();
        }
        return ""; // Return a default value or null depending on your requirements
    }

    public void removePlayerSetting(UUID uuid, String key) {
        JsonObject settingsObject = playerSettings.get(uuid);
        if (settingsObject != null && settingsObject.has(key)) {
            settingsObject.remove(key);
            saveSettings();
        }
    }

}

package net.hauntedstudio.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final File configFile;
    private final Gson gson;
    private Map<String, String> configMap;

    public static boolean isDefaultConfig = false;

    public ConfigManager() {
        this.configFile = new File("config/ServerBackendMod/config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configMap = new HashMap<>();
        // Check if links.json file exists
        if (configFile.exists()) {
            loadConfigFromFile();
        } else {
            createEmptyConfigFile();
            addOrUpdateConfig("discord-token", "YOUR_DISCORD_BOT_TOKEN");
            addOrUpdateConfig("discord-guild-id", "YOUR_DISCORD_GUILD_ID");
            addOrUpdateConfig("discord-channel-id", "YOUR_DISCORD_CHANNEL_ID");
            addOrUpdateConfig("discord-webhook-url", "YOUR_DISCORD_WEBHOOK_URL");
            addOrUpdateConfig("discord-support-links", "false");
            isDefaultConfig = true;
        }
    }

    // Load contents of links.json file into linksMap
    private void loadConfigFromFile() {
        try (Reader reader = new FileReader(configFile)) {
            configMap = gson.fromJson(reader, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create an empty links.json file
    private void createEmptyConfigFile() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(new HashMap<String, String>(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter method to get the linksMap
    public String getString(String key) {
        return configMap.get(key);
    }

    // Method to add or update a link
    public void addOrUpdateConfig(String key, String value) {
        configMap.put(key, value);
        saveConfigsToFile();
    }

    // Method to delete a link
    public void deleteConfig(String discordUID) {
        configMap.remove(discordUID);
        saveConfigsToFile();
    }

    // Method to save linksMap to links.json file
    private void saveConfigsToFile() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

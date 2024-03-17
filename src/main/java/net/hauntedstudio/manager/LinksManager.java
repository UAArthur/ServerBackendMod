package net.hauntedstudio.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LinksManager {
    private final File linksFile;
    private final Gson gson;
    private Map<String, String> linksMap;

    public LinksManager() {
        this.linksFile = new File("config/ServerBackendMod/links.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.linksMap = new HashMap<>();
        // Check if links.json file exists
        if (linksFile.exists()) {
            loadLinksFromFile();
        } else {
            createEmptyLinksFile();
        }
    }

    // Load contents of links.json file into linksMap
    private void loadLinksFromFile() {
        try (Reader reader = new FileReader(linksFile)) {
            linksMap = gson.fromJson(reader, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create an empty links.json file
    private void createEmptyLinksFile() {
        try (FileWriter writer = new FileWriter(linksFile)) {
            gson.toJson(new HashMap<String, String>(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter method to get the linksMap
    public Map<String, String> getLinksMap() {
        return linksMap;
    }

    // Method to add or update a link
    public void addOrUpdateLink(String discordUID, String minecraftUUID) {
        linksMap.put(discordUID, minecraftUUID);
        saveLinksToFile();
    }

    // Method to delete a link
    public void deleteLink(String discordUID) {
        linksMap.remove(discordUID);
        saveLinksToFile();
    }

    // Method to save linksMap to links.json file
    private void saveLinksToFile() {
        try (FileWriter writer = new FileWriter(linksFile)) {
            gson.toJson(linksMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

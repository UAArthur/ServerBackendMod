package net.hauntedstudio.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final File langDir = new File("config/ServerBackendMod/lang");
    private final Map<String, JsonObject> languageData = new HashMap<>();

    public LanguageManager() {
        if (!langDir.exists()) {
            if (!langDir.mkdirs()) {
                System.err.println("Failed to create lang directory");
            } else {
                System.out.println("Lang directory created successfully");
            }
            loadLanguages();
        } else {
            loadLanguages();
        }
    }


    private void loadLanguages() {
        File[] langFiles = langDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (langFiles != null) {
            for (File langFile : langFiles) {
                try (FileInputStream inputStream = new FileInputStream(langFile);
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {

                    String langName = langFile.getName().replace(".json", "");
                    JsonObject langObject = JsonParser.parseReader(reader).getAsJsonObject();
                    languageData.put(langName, langObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getString(String lang, String key) {
        JsonObject langData = languageData.get("lang_" + lang);
        if (langData != null && langData.has(key)) {
            return langData.get(key).getAsString();
        }
        return "Key not found in language file. Search for: " + key + " in " + lang + ".json";
    }
}


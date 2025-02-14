package ch.yoinc.manager;

import ch.yoinc.model.Assistant;
import ch.yoinc.model.Settings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SettingsManager {
    private static final String SETTINGS_PATH = "C:\\ProgramData\\TwitchAssistant";
    private static final String SETTINGS_FILE_NAME = "settings.json";
    private static SettingsManager instance;

    private Settings settings;

    private SettingsManager() {
        settings = loadSettings();
    }

    public static SettingsManager getInstance() {
        if(instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    private Settings loadSettings() {
        Path settingsPath = Paths.get(SETTINGS_PATH + "\\" + SETTINGS_FILE_NAME);
        File settingsFile = new File(SETTINGS_PATH + "\\" + SETTINGS_FILE_NAME);

        try {
            if (Files.exists(settingsPath)) {
                if (!settingsFile.exists()) {
                    System.out.println("No settings found. Creating new settings file...");
                    createSettings(false);
                } else {
                    System.out.println("Settings found.");
                    JsonReader reader = new JsonReader(new FileReader(settingsFile));
                    settings = new Gson().fromJson(reader, Settings.class);
                }
            } else {
                System.out.println("No settings folder found. Creating new settings folder...");
                createSettings(true);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Fata error: " + exception.getMessage());
            settings = new Settings("","","",false,new ArrayList<Assistant>());
        }

        return settings;
    }

    private void createSettings(boolean createFolder) {
        settings = new Settings("", "", "", false, new ArrayList<>());
        saveSettings(settings, createFolder);
    }

    private static void saveSettings(Settings settings, boolean createFolder) {
        if(createFolder) {
            File folder = new File(SETTINGS_PATH);
            folder.mkdir();
        }
        try (FileWriter file = new FileWriter(SETTINGS_PATH + "\\" + SETTINGS_FILE_NAME)) {
            file.write(new Gson().toJson(settings));
            System.out.println("Default settings saved.");
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    public void saveSettingsOnClose() {
        //TODO save on close
    }

    public Settings getSettings() {
        return settings;
    }
}

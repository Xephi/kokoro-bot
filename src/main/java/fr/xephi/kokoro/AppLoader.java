package fr.xephi.kokoro;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
//
import fr.xephi.kokoro.config.MainConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class AppLoader {

    private SettingsManager settings;
    private File configFile;

    AppLoader() {
        this.settings = initSettings();
    }

    /**
     * Initializes the settings manager.
     *
     * @return the settings manager
     */
    private SettingsManager initSettings() {
        configFile = new File("configs/config.yml");
        return SettingsManagerBuilder.withYamlFile(configFile).configurationData(MainConfig.class).useDefaultMigrationService().create();
    }

    public SettingsManager getSettings() {
        return settings;
    }
}

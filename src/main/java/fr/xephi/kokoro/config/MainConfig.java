package fr.xephi.kokoro.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class MainConfig implements SettingsHolder {

    public static final Property<String> BOT_ID =
            newProperty("bot.id", "-PUT-BOT-ID-HERE-");

    public static final Property<String> BOT_TOKEN =
            newProperty("bot.token", "-PUT-BOT-TOKEN-HERE-");

    public static final Property<String> CREATE_COMMAND =
            newProperty("bot.createcommand", "createcommand");

    public static final Property<String> DATA_FOLDER =
            newProperty("bot.dataFolder", "data");

    public static final Property<String> ADMIN_PERMISSION =
            newProperty("permission.role", "Bot Manager");

    private MainConfig() {
        // Prevent instantiation
    }
}

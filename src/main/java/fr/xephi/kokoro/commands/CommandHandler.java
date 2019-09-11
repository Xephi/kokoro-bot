package fr.xephi.kokoro.commands;

import ch.jalu.configme.SettingsManager;
import fr.xephi.kokoro.commands.model.Command;
import fr.xephi.kokoro.commands.model.TwitterFeed;
import fr.xephi.kokoro.commands.utils.CustomCommandsUtils;
import fr.xephi.kokoro.config.MainConfig;
import fr.xephi.kokoro.utils.EmojiUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommandHandler {

    private Map<String, Map<String, String>> commands;
    public Map<String, Map<String, String>> twitterFeeds;
    private final SettingsManager settings;
    private final JDA jda;
    private final CreateCommands creator;
    private final TwitterCommand twitterCommand;

    public CommandHandler(SettingsManager settings, JDA jda) {
        this.settings = settings;
        this.jda = jda;
        this.commands = CustomCommandsUtils.loadFolder(settings.getProperty(MainConfig.DATA_FOLDER), "commands");
        this.twitterFeeds = CustomCommandsUtils.loadFolder(settings.getProperty(MainConfig.DATA_FOLDER), "twitter");
        this.creator = new CreateCommands(settings.getProperty(MainConfig.CREATE_COMMAND));
        this.twitterCommand = new TwitterCommand();
    }

    public boolean commandHandled(String guildId, String command) {
        if (command.equalsIgnoreCase(settings.getProperty(MainConfig.CREATE_COMMAND)))
            return true;
        if (command.equalsIgnoreCase("twitter"))
            return true;
        if (!commands.containsKey(guildId))
            return false;
        return commands.get(guildId).containsKey(command);
    }

    public boolean hasPermissionForCommand(String command, List<Role> roles) {
        if (!command.equalsIgnoreCase(settings.getProperty(MainConfig.CREATE_COMMAND)))
            return true;
        for (final Role role : roles) {
            if (role.getName().equals(settings.getProperty(MainConfig.ADMIN_PERMISSION)))
                return true;
        }
        return false;
    }

    public void handleCommand(String guildId, String channelId, String command, String args) throws NullPointerException {
        if (command.equalsIgnoreCase(settings.getProperty(MainConfig.CREATE_COMMAND))) {
            Command c = creator.execute(jda, settings.getProperty(MainConfig.DATA_FOLDER), guildId, channelId, command, args);
            if (c != null) {
                if (!commands.containsKey(guildId))
                    commands.put(guildId, new HashMap<>());
                commands.get(guildId).put(c.command, c.reply);
            }
            return;
        }
        if (command.equalsIgnoreCase("twitter")) {
            TwitterFeed tf = twitterCommand.execute(jda, settings.getProperty(MainConfig.DATA_FOLDER), guildId, channelId, command, args);
            if (tf != null) {
                if (!twitterFeeds.containsKey(guildId))
                    twitterFeeds.put(guildId, new HashMap<>());
                twitterFeeds.get(guildId).put(tf.twitterName, tf.channelId);
            }
            return;
        }
        if (!commands.containsKey(guildId))
            return;
        if (!commands.get(guildId).containsKey(command))
            return;
        final String text = commands.get(guildId).get(command);
        Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(EmojiUtils.buildMessageWithEmotes(jda, guildId, text)).queue();
    }
}

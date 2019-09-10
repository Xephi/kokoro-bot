package fr.xephi.kokoro.commands;

import ch.jalu.configme.SettingsManager;
import fr.xephi.kokoro.Messages;
import fr.xephi.kokoro.config.MainConfig;
import fr.xephi.kokoro.utils.EmojiUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CommandHandler {

    private Map<String, Map<String, String>> commands;
    private final SettingsManager settings;
    private final JDA jda;

    public CommandHandler(SettingsManager settings, JDA jda) {
        this.settings = settings;
        this.jda = jda;
        this.commands = CustomCommands.loadCommands(settings.getProperty(MainConfig.DATA_FOLDER));
    }

    public boolean commandHandled(String guildId, String command) {
        if (command.equalsIgnoreCase(settings.getProperty(MainConfig.CREATE_COMMAND)))
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
        if (command.equalsIgnoreCase(settings.getProperty(MainConfig.CREATE_COMMAND))) { // Move to his own class
            StringTokenizer tokenizer = new StringTokenizer(args);
            if (tokenizer.countTokens() < 2) {
                jda.getTextChannelById(channelId).sendMessage(Messages.Errors.MORE_ARGUMENTS_NEEDED).queue();
                return;
            }
            final String commandToCreate = tokenizer.nextToken();
            final String argsToCommand = tokenizer.nextToken("").substring(1);
            if (CustomCommands.saveCommand(settings.getProperty(MainConfig.DATA_FOLDER), guildId, commandToCreate, argsToCommand)) {
                if (!commands.containsKey(guildId))
                    commands.put(guildId, new HashMap<>());
                commands.get(guildId).put(commandToCreate, argsToCommand);
                jda.getTextChannelById(channelId).sendMessage(Messages.Success.CUSTOM_COMMAND).queue();
            } else {
                jda.getTextChannelById(channelId).sendMessage(Messages.Errors.CUSTOM_COMMAND).queue();
            }
            return;
        }
        if (!commands.containsKey(guildId))
            return;
        if (!commands.get(guildId).containsKey(command))
            return;
        final String text = commands.get(guildId).get(command);
        jda.getTextChannelById(channelId).sendMessage(EmojiUtils.buildMessageWithEmotes(jda, guildId, text)).queue();
    }
}

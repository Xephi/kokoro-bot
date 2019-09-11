package fr.xephi.kokoro.commands;

import fr.xephi.kokoro.Messages;
import fr.xephi.kokoro.commands.model.Command;
import fr.xephi.kokoro.commands.utils.CustomCommandsUtils;
import net.dv8tion.jda.api.JDA;

import java.util.Objects;
import java.util.StringTokenizer;

public class CreateCommands implements ICommand<Command> {

    private String name;

    public CreateCommands( String name ) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelper() {
        return "." + name + " commandName commandReply";
    }

    @Override
    public Command execute(JDA jda, String dataFolder, String guildId, String channelId, String command, String args) {
        StringTokenizer tokenizer = new StringTokenizer(args);
        if (tokenizer.countTokens() < 2) {
            Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Errors.INVALID_NUMBER_OF_ARGUMENTS).queue();
            return null;
        }
        final String commandToCreate = tokenizer.nextToken();
        if (commandToCreate.equalsIgnoreCase(this.name)
                || commandToCreate.equalsIgnoreCase("twitter")) {
            // Do not let user to overwrite our hard coded commands
            Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Errors.CUSTOM_COMMAND).queue();
            return null;
        }
        final String argsToCommand = tokenizer.nextToken("").substring(1);
        if (CustomCommandsUtils.saveTo(dataFolder, "commands", guildId, commandToCreate, argsToCommand)) {
            Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Success.CUSTOM_COMMAND).queue();
        } else {
            Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Errors.CUSTOM_COMMAND).queue();
            return null;
        }
        return new Command(commandToCreate, argsToCommand);
    }
}

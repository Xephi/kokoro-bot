package fr.xephi.kokoro.commands;

import fr.xephi.kokoro.Messages;
import fr.xephi.kokoro.commands.model.TwitterFeed;
import fr.xephi.kokoro.commands.utils.CustomCommandsUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;
import java.util.StringTokenizer;

public class TwitterCommand implements ICommand<TwitterFeed> {
    @Override
    public String getName() {
        return "twitter";
    }

    @Override
    public String getHelper() {
        return ".twitter tweeterName channelName";
    }

    @Override
    public TwitterFeed execute(JDA jda, String dataFolder, String guildId, String channelId, String command, String args) {
        if (!command.equalsIgnoreCase("twitter"))
            return null;
        StringTokenizer tokens = new StringTokenizer(args);
        if (tokens.countTokens() != 2) {
            Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Errors.INVALID_NUMBER_OF_ARGUMENTS).queue();
            return null;
        }
        final String twitterUser = tokens.nextToken();
        final String channelToLink = tokens.nextToken("").substring(1);
        try {
            TextChannel textChannel = Objects.requireNonNull(jda.getGuildById(guildId)).getTextChannelById(channelToLink);
            if (textChannel == null) {
                Objects.requireNonNull(jda.getTextChannelById(channelId)).sendMessage(Messages.Errors.CHANNEL_NOT_FOUND).queue();
                return null;
            }
            CustomCommandsUtils.saveTo(dataFolder, "twitter", guildId, twitterUser, channelId);
            return new TwitterFeed(twitterUser, channelId);
        } catch (final NullPointerException ignored) {
        }
        return null;
    }
}

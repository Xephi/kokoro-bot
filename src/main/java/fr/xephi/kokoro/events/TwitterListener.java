package fr.xephi.kokoro.events;

import fr.xephi.kokoro.commands.CommandHandler;
import net.dv8tion.jda.api.JDA;
import twitter4j.Status;
import twitter4j.TwitterStream;

public class TwitterListener {

    private final JDA jda;
    private final String guildId;
    private final String channelId;
    private final String username;

    public TwitterListener(JDA jda, String guildId, String channelId, String username) {
        this.jda = jda;
        this.guildId = guildId;
        this.channelId = channelId;
        this.username = username;
    }

    public void onStatus(Status status) {

    }
}

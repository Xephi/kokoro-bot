package fr.xephi.kokoro;

import ch.jalu.configme.SettingsManager;
import fr.xephi.kokoro.config.MainConfig;
import net.dv8tion.jda.api.JDA;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;

public class TwitterLoader {

    private final SettingsManager settings;
    private Twitter twitter;
    private Configuration config;

    public TwitterLoader(SettingsManager settings) {
        this.settings = settings;
        loadTwitter();
    }

    private void loadTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(settings.getProperty(MainConfig.TWITTER_CONSUMER_KEY))
                .setOAuthConsumerSecret(settings.getProperty(MainConfig.TWITTER_CONSUMER_SECRET))
                .setOAuthAccessToken(settings.getProperty(MainConfig.TWITTER_OAUTH_ACCESS_TOKEN))
                .setOAuthAccessTokenSecret(settings.getProperty(MainConfig.TWITTER_OAUTH_ACCESS_TOKEN_SECRET));
        this.config = builder.build();
        TwitterFactory factory = new TwitterFactory(config);
        this.twitter = factory.getInstance();
    }

    public boolean registerListener( JDA jda, String guildId, String channelId, String username ) {
        try {
            User userToFollow = twitter.showUser(username);

        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

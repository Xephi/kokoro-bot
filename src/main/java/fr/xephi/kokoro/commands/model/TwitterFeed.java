package fr.xephi.kokoro.commands.model;

public class TwitterFeed {
    public String twitterName;
    public String channelId;

    public TwitterFeed(String twitterName, String channelId) {
        this.twitterName = twitterName;
        this.channelId = channelId;
    }
}

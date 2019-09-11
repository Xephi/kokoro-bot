package fr.xephi.kokoro.commands;

import net.dv8tion.jda.api.JDA;

public interface ICommand<T> {
    String getName();

    String getHelper();

    T execute(JDA jda, String dataFolder, String guildId, String channelId, String command, String args );
}

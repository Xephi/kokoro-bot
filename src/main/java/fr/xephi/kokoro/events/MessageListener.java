package fr.xephi.kokoro.events;

import fr.xephi.kokoro.Loggers;
import fr.xephi.kokoro.commands.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class MessageListener extends ListenerAdapter {

    private final CommandHandler commands;

    public MessageListener( CommandHandler commandHandler ) {
        this.commands = commandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Loggers.MESSAGE_LISTENER.info("Receive message");
        if (event.getAuthor().isBot())
            return;
        Loggers.MESSAGE_LISTENER.info("User was not a bot, continue");
        if (!event.isFromGuild())
            return;
        Loggers.MESSAGE_LISTENER.info("This event is provided by a 'guild'");
        if (event.getMember() == null)
            return;
        Loggers.MESSAGE_LISTENER.info("Event member was not null");
        final String message = event.getMessage().getContentDisplay();
        if (!message.startsWith("."))
            return;
        Loggers.MESSAGE_LISTENER.info("Message start by a point");
        final String guildId = event.getGuild().getId();
        final String channelId = event.getTextChannel().getId();
        StringTokenizer tokenizer = new StringTokenizer(message.substring(1));
        if (!tokenizer.hasMoreTokens())
            return;
        Loggers.MESSAGE_LISTENER.info("Command can be parsed");
        final String command = tokenizer.nextToken();
        Loggers.MESSAGE_LISTENER.info("Parsed command : " + command);
        if (!commands.commandHandled(guildId, command))
            return;
        Loggers.MESSAGE_LISTENER.info("We can handle this command, continue");
        if (!commands.hasPermissionForCommand( command, Objects.requireNonNull(event.getMember()).getRoles()))
            return;
        Loggers.MESSAGE_LISTENER.info("User has permission for this command");
        String args = "";
        if (tokenizer.hasMoreTokens()) {
            args = tokenizer.nextToken("").substring(1);
        }
        Loggers.MESSAGE_LISTENER.info("Handled command : " + command);
        Loggers.MESSAGE_LISTENER.info("With args : " + args);
        try {
            commands.handleCommand( guildId, channelId, command, args );
        } catch (final NullPointerException e) {
            e.printStackTrace();
        }
    }
}

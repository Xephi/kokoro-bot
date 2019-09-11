package fr.xephi.kokoro.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.StringTokenizer;

public class EmojiUtils {

    public static Message buildMessageWithEmotes(JDA api, String guildId, String message) {
        MessageBuilder builder = new MessageBuilder();
        StringTokenizer tokenizer = new StringTokenizer(message, " \t\n\r\f", true);
        while (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            if (!token.startsWith(":") && !token.endsWith(":")) {
                builder.append(token);
                continue;
            }
            try {
                final List<Emote> emotes = api.getGuildById(guildId).getEmotesByName(token.substring(1, token.length() - 1), false);
                if (emotes.isEmpty()) {
                    builder.append(token);
                    continue;
                }
                builder.append(emotes.get(0));
            } catch (final NullPointerException e) {
                builder.append(token);
            }
        }
        return builder.build();
    }
}

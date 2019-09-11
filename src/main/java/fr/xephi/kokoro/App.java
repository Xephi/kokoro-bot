package fr.xephi.kokoro;

import fr.xephi.kokoro.commands.CommandHandler;
import fr.xephi.kokoro.config.MainConfig;
import fr.xephi.kokoro.events.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Bot is starting..." );
        try {
            AppLoader appLoaded = new AppLoader();
            JDA jda = new JDABuilder(appLoaded.getSettings().getProperty(MainConfig.BOT_TOKEN)).build();
            CommandHandler commands = new CommandHandler(appLoaded.getSettings(), jda);
            jda.addEventListener(new MessageListener(commands));
        } catch (Exception e) {
            e.printStackTrace(); // TODO: Better handler for exceptions
        }
    }

    private App() {
        // Prevent instantiation
    }
}

package fr.xephi.kokoro;

public class Messages {
    public static class Errors {
        public static String CUSTOM_COMMAND = "Cannot create this command, maybe it already exist?";
        public static String INVALID_NUMBER_OF_ARGUMENTS = "Invalid number of arguments for this command";
        public static String CHANNEL_NOT_FOUND = "Cannot find a channel with that id or name";
    }

    public static class Success {
        public static String CUSTOM_COMMAND = "Successfully created a new command !";
    }
}

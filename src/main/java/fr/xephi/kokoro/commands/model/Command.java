package fr.xephi.kokoro.commands.model;

public class Command {

    public String command;
    public String reply;

    public Command(String command, String reply) {
        this.command = command;
        this.reply = reply;
    }
}

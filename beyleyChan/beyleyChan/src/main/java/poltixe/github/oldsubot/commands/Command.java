package poltixe.github.oldsubot.commands;

import poltixe.github.flanchobotlibrary.BotClient;

public class Command {
    public String sender;
    public String target;
    public String[] arguments;
    public String command;

    public Command(String sender, String target, String command, String[] arguments) {
        this.sender = sender;
        this.target = target;
        this.command = command;
        this.arguments = arguments;
    }

    public void runCommand(BotClient client) {

    }
}

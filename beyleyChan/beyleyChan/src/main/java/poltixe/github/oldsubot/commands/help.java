package poltixe.github.oldsubot.commands;

import poltixe.github.flanchobotlibrary.BotClient;

public class help extends Command {
    public help(String sender, String target, String command, String[] arguments) {
        super(sender, target, command, arguments);
    }

    @Override
    public void runCommand(BotClient client) {
        String[] HelpMessages = new String[] {
                "Hello! I'm Beyley-chan, PoltixeTheDerg's bot. (name literally comes from her real name lmao)",
                "Currently I'm in early stages of development so I can't do much yet!",
                "The currently Available commands are as following:",
                ": " + client.prefix + "top10 : Lists the top 10 players",
                ": " + client.prefix + "nextrank : Shows the required score to reach the next rank",
                ": " + client.prefix + "u : Shows basic stats about you",
                ": " + client.prefix + "help : You are currently looking at this!!" };

        for (String helpString : HelpMessages)
            client.packetSender.sendMessage(client.username, helpString, this.target);
    }
}

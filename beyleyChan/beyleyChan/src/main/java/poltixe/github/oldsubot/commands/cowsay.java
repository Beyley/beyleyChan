package poltixe.github.oldsubot.commands;

import java.util.*;

import poltixe.github.flanchobotlibrary.BotClient;

public class cowsay extends Command {
    public cowsay(String sender, String target, String command, String[] arguments) {
        super(sender, target, command, arguments);
    }

    /**
     * Split text into n number of characters.
     *
     * @param text the text to be split.
     * @param size the split size.
     * @return an array of the split text.
     */
    private static String[] splitStringEveryNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    @Override
    public void runCommand(BotClient client) {
        try {
            List<String> returnMessages = new ArrayList<String>();

            String fullMessage = String.join(" ", arguments);
            List<String> messages = Arrays.asList(splitStringEveryNChar(fullMessage, 75));

            for (String message : messages) {
                returnMessages.add(String.format("%s", message));
            }

            returnMessages.add(" ");
            returnMessages.add("        \\  ^__^");
            returnMessages.add("         \\  (oo)\\_____");
            returnMessages.add("            (__)\\         )\\/\\");
            returnMessages.add("                  ||----w |");
            returnMessages.add("                  ||       ||");

            for (String string : returnMessages) {
                client.packetSender.sendMessage(client.username, string, this.target);

                Thread.sleep(100);

            }
        } catch (Exception e) {
            System.out.println("pain");
        }
    }
}

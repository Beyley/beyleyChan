package poltixe.github.oldsubot.commands;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.BotClient;

public class u extends Command {
    public u(String sender, String target, String command, String[] arguments) {
        super(sender, target, command, arguments);
    }

    @Override
    public void runCommand(BotClient client) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

        HttpResponse<String> response = null;

        String json = "";

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            json = response.body();
        } catch (IOException | InterruptedException ex) {
            client.console.printError(String.format(
                    "Error getting players info! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s", sender,
                    target, command, arguments, ex.getMessage()));
        }

        Object paresdJson = null;
        try {
            paresdJson = new JSONParser().parse(json);
        } catch (ParseException e) {

        }

        JSONObject jo = (JSONObject) paresdJson;

        JSONArray ja = (JSONArray) jo.get("users");

        Iterator<?> allUsers = ja.iterator();

        List<String> returnMessages = new ArrayList<String>();

        String usernameToCheck = sender;

        if (arguments.length > 0) {
            usernameToCheck = String.join(" ", arguments).strip();
        }

        while (allUsers.hasNext()) {
            JSONObject user = (JSONObject) allUsers.next();

            if (user.get("username").equals(usernameToCheck)) {
                int userAboveIndex = Integer.parseInt((String) user.get("rank")) - 2;
                int userBelowIndex = Integer.parseInt((String) user.get("rank"));

                JSONObject userAbove = (JSONObject) ja.get(userAboveIndex);
                JSONObject userBelow = (JSONObject) ja.get(userBelowIndex);

                double userRank = Double.parseDouble((String) user.get("rank"));
                double userScore = Double.parseDouble((String) user.get("ranked_score"));

                double userAboveRank = Double.parseDouble((String) userAbove.get("rank"));
                double userAboveScore = Double.parseDouble((String) userAbove.get("ranked_score"));

                // double userBelowRank = Double.parseDouble((String) userBelow.get("rank"));
                double userBelowScore = Double.parseDouble((String) userBelow.get("ranked_score"));

                returnMessages
                        .add(String.format("%s (#%,.0f) has %,.0f score!", user.get("username"), userRank, userScore));

                if (userAboveScore - userScore < 5000000) {
                    returnMessages
                            .add(String.format("#%,.0f is only %,.0f score away! Set a few scores and gain a rank!",
                                    userAboveRank, userAboveScore - userScore));
                }

                if (userScore - userBelowScore < 5000000) {
                    returnMessages.add(
                            String.format("%s is only %,.0f score away from you! Set a few scores and widen the gap!",
                                    userBelow.get("username"), userScore - userBelowScore));
                }

                break;
            }
        }

        for (String thisReturnMessage : returnMessages) {
            client.packetSender.sendMessage(client.username, thisReturnMessage, target);
        }
    }
}

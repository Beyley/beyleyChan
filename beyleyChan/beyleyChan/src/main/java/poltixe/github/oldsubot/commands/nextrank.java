package poltixe.github.oldsubot.commands;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.BotClient;

public class nextrank extends Command {
    public nextrank(String sender, String target, String command, String[] arguments) {
        super(sender, target, command, arguments);
    }

    @Override
    public void runCommand(BotClient client) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

            HttpResponse<String> response = null;

            String json = "";

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            json = response.body();

            Object paresdJson = null;
            try {
                paresdJson = new JSONParser().parse(json);
            } catch (ParseException e) {

            }

            JSONObject jo = (JSONObject) paresdJson;

            JSONArray ja = (JSONArray) jo.get("users");

            Iterator<?> allUsers = ja.iterator();

            String returnMessage = "";

            while (allUsers.hasNext()) {
                JSONObject user = (JSONObject) allUsers.next();

                if (user.get("username").equals(sender)) {
                    if (Integer.parseInt((String) user.get("rank")) == 1) {
                        returnMessage = "ShadowOfDark fuck you lmaooooo (jk luv ya <3)";

                        break;
                    }

                    int userAboveIndex = Integer.parseInt((String) user.get("rank")) - 2;

                    JSONObject userAbove = (JSONObject) ja.get(userAboveIndex);

                    returnMessage = String.format("%s is %,.0f score above you!", (String) userAbove.get("username"),
                            (Double.parseDouble((String) userAbove.get("ranked_score"))
                                    - Double.parseDouble((String) user.get("ranked_score"))));
                }
            }

            client.packetSender.sendMessage(client.username, returnMessage, target);
        } catch (IOException | InterruptedException ex) {
            client.console.printError(String.format(
                    "Error getting players info! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s", sender,
                    target, command, arguments, ex.getMessage()));
        }
    }
}

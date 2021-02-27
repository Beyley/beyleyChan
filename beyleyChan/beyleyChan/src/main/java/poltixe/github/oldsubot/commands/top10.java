package poltixe.github.oldsubot.commands;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.BotClient;

public class top10 extends Command {
    public top10(String sender, String target, String command, String[] arguments) {
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
                    "Error getting top 10 players! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s", sender,
                    target, command, arguments, ex.getMessage()));
        }

        Object paresdJson = null;
        try {
            paresdJson = new JSONParser().parse(json);
        } catch (ParseException e) {
        }

        JSONObject jo = (JSONObject) paresdJson;

        JSONArray ja = (JSONArray) jo.get("users");

        long lastScore = Long.parseLong((String) ((JSONObject) ja.get(0)).get("ranked_score"));

        String returnMessage = "";

        for (int i = 0; i < 10; i++) {
            StringBuffer playerUsername = new StringBuffer((String) ((JSONObject) ja.get(i)).get("username"));

            returnMessage = String.format("#%s | %s | Score:%,.0f", (String) ((JSONObject) ja.get(i)).get("rank"),
                    playerUsername.toString(),
                    Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));

            if (i > 0) {
                returnMessage += String.format(" - %,.0f score behind",
                        (double) lastScore - Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));
            }

            lastScore = Long.parseLong((String) ((JSONObject) ja.get(i)).get("ranked_score"));

            client.packetSender.sendMessage(client.username, returnMessage, target);
        }
    }
}

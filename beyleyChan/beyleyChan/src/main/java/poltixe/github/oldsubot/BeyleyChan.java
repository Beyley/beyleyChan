package poltixe.github.oldsubot;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.net.http.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.*;
import poltixe.github.flanchobotlibrary.packets.*;

public class BeyleyChan extends BotClient {
    public BeyleyChan(String username, String plainPassword, char prefix) {
        super(username, plainPassword, prefix);
    }

    @Override
    public void onPrefixedMessage(String sender, String target, String message) {
        try {
            switch (message.split(" ")[0].substring(1)) {
                case "help":
                    String[] HelpMessages = new String[] {
                            "Hello! I'm Beyley-chan, PoltixeTheDerg's bot. (name literally comes from her real name lmao)",
                            "Currently I'm in early stages of development so I can't do much yet!",
                            "The currently Available commands are as following:",
                            prefix + "top10 : Lists the top 10 players",
                            prefix + "nextrank : Shows the required score to reach the next rank",
                            prefix + "u : Shows basic stats about you",
                            prefix + "help : You are currently looking at this!!" };
                    for (String helpString : HelpMessages) {
                        SendPacket.sendMessage(username, helpString, target, client);

                        Thread.sleep(50);
                    }

                    Thread.sleep(500);

                    break;
                case "top10a":
                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

                    HttpResponse<String> response = null;

                    String json = "";

                    try {
                        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        // print response body
                        json = response.body();

                    } catch (IOException ex) {
                    } catch (InterruptedException ex) {
                    }

                    Object paresdJson = new JSONParser().parse(json);

                    JSONObject jo = (JSONObject) paresdJson;

                    // getting phoneNumbers
                    JSONArray ja = (JSONArray) jo.get("users");

                    Iterator<?> allUsers = ja.iterator();

                    int lastScore = Integer.parseInt((String) ((JSONObject) ja.get(0)).get("ranked_score"));

                    String returnMessage = "";

                    for (int i = 0; i < 10; i++) {
                        returnMessage = String.format("#%s | %s | Score:%,.0f",
                                (String) ((JSONObject) ja.get(i)).get("rank"),
                                (String) ((JSONObject) ja.get(i)).get("username"),
                                Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));

                        if (i > 0) {
                            returnMessage += String.format(" - %,.0f score behind", (double) lastScore
                                    - Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));
                        }

                        lastScore = Integer.parseInt((String) ((JSONObject) ja.get(i)).get("ranked_score"));

                        SendPacket.sendMessage(username, returnMessage, target, client);

                        Thread.sleep(50);
                    }

                    break;
                case "u":
                    httpClient = HttpClient.newHttpClient();
                    request = HttpRequest.newBuilder()
                            .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

                    response = null;

                    json = "";

                    try {
                        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        json = response.body();

                    } catch (IOException ex) {
                    } catch (InterruptedException ex) {
                    }

                    paresdJson = new JSONParser().parse(json);

                    jo = (JSONObject) paresdJson;

                    ja = (JSONArray) jo.get("users");

                    allUsers = ja.iterator();

                    List<String> returnMessages = new ArrayList<String>();

                    while (allUsers.hasNext()) {
                        JSONObject user = (JSONObject) allUsers.next();

                        if (user.get("username").equals(sender)) {
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

                            returnMessages.add(String.format("%s (#%,.0f) has %,.0f score!", user.get("username"),
                                    userRank, userScore));

                            if (userAboveScore - userScore < 5000000) {
                                returnMessages.add(String.format(
                                        "#%,.0f is only %,.0f score away! Set a few scores and gain a rank!",
                                        userAboveRank, userAboveScore - userScore));
                            }

                            if (userScore - userBelowScore < 5000000) {
                                returnMessages.add(String.format(
                                        "%s is only %,.0f score away from you! Set a few scores and widen the gap!",
                                        userBelow.get("username"), userScore - userBelowScore));
                            }
                        }
                    }

                    for (String helpString : returnMessages) {
                        SendPacket.sendMessage(username, helpString, target, client);

                        Thread.sleep(50);
                    }

                    break;
                // case "setStatus":
                // SendPacket.updateStatus(Short.parseShort(message.split(" ")[1]), client);
                // break;
                case "nextrank":
                    httpClient = HttpClient.newHttpClient();
                    request = HttpRequest.newBuilder()
                            .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

                    response = null;

                    json = "";

                    try {
                        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        json = response.body();

                    } catch (IOException | InterruptedException ex) {
                    }

                    paresdJson = new JSONParser().parse(json);

                    jo = (JSONObject) paresdJson;

                    ja = (JSONArray) jo.get("users");

                    allUsers = ja.iterator();

                    returnMessage = "";

                    while (allUsers.hasNext()) {
                        JSONObject user = (JSONObject) allUsers.next();

                        if (user.get("username").equals(sender)) {
                            if (Integer.parseInt((String) user.get("rank")) == 1) {
                                returnMessage = "ShadowOfDark fuck you lmaooooo (jk luv ya <3)";

                                break;
                            }

                            int userAboveIndex = Integer.parseInt((String) user.get("rank")) - 2;

                            JSONObject userAbove = (JSONObject) ja.get(userAboveIndex);

                            returnMessage = String.format("%s is %,.0f score above you!",
                                    (String) userAbove.get("username"),
                                    (Double.parseDouble((String) userAbove.get("ranked_score"))
                                            - Double.parseDouble((String) user.get("ranked_score"))));
                        }
                    }

                    SendPacket.sendMessage(username, returnMessage, target, client);

                    break;
            }
        } catch (IOException | InterruptedException | ParseException e) {
        }
    }

    @Override
    public void onAuthComplete() {
        try {
            SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with Flan-chan in the fields!", client);
        } catch (IOException e) {
        }

        Timer timer = new Timer();
        // Timer starts after 25 seconds.
        int begin = 25000;
        // Timer executes every 25 seconds.
        int timeinterval = 25000;

        // Schedule the keepalive / userstatus packet
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean setNewStatus = false;

            @Override
            public void run() {
                try {
                    if (setNewStatus) {
                        int randomNumber = rand.nextInt(21);

                        switch (randomNumber) {
                            case 0:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with Flan-chan in the fields!",
                                        client);
                                break;
                            case 1:
                                long jvmUpTime = ManagementFactory.getRuntimeMXBean().getUptime();

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

                                LocalDateTime date = Instant.ofEpochMilli(jvmUpTime).atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();

                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING,
                                        String.format("Visual Studio Code for %s", dtf.format(date)), client);
                                break;
                            case 2:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "oldsu!", client);
                                break;
                            case 3:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with eve owo", client);
                                break;
                            case 4:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with thighs", client);
                                break;
                            case 5:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with thingies ;)", client);
                                break;
                            case 6:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with uleb128-chan", client);
                                break;
                            case 7:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with you >^<", client);
                                break;
                            case 8:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with a cutie", client);
                                break;
                            case 9:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with UserStatus packets",
                                        client);
                                break;
                            case 10:
                                SendPacket.updateStatus(SendUserStatusPacket.WATCHING, "eve code while blushing",
                                        client);
                                break;
                            case 11:
                                SendPacket.updateStatus(SendUserStatusPacket.WATCHING, "the haitai background", client);
                                break;
                            case 12:
                                SendPacket.updateStatus(SendUserStatusPacket.MULTIPLAYING, "with the girls", client);
                                break;
                            case 13:
                                SendPacket.updateStatus(SendUserStatusPacket.EDITING, "her github profile", client);
                                break;
                            case 14:
                                SendPacket.updateStatus(SendUserStatusPacket.WATCHING, "you", client);
                                break;
                            case 15:
                                SendPacket.updateStatus(SendUserStatusPacket.SUBMITTING, "her love confession to eve",
                                        client);
                                break;
                            case 16:
                                SendPacket.updateStatus(SendUserStatusPacket.SUBMITTING, "her love confession to eevee",
                                        client);
                                break;
                            case 17:
                                SendPacket.updateStatus(SendUserStatusPacket.EDITING, "her love confession to eve",
                                        client);
                                break;
                            case 18:
                                SendPacket.updateStatus(SendUserStatusPacket.EDITING, "her love confession to eevee",
                                        client);
                                break;
                            case 19:
                                SendPacket.updateStatus(SendUserStatusPacket.MODDING, "her body to appeal to eve",
                                        client);
                                break;
                            case 20:
                                SendPacket.updateStatus(SendUserStatusPacket.PLAYING, "with ur bits, look down",
                                        client);
                                break;
                        }

                        setNewStatus = false;
                    } else {
                        setNewStatus = true;
                    }
                } catch (IOException e) {
                }
            }
        }, begin, timeinterval);
    }
}

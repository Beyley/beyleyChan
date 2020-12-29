package poltixe.github.oldsubot;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.*;
import poltixe.github.flanchobotlibrary.packets.*;
import poltixe.github.flanchobotlibrary.shortcuts.Time;

public class BeyleyChan extends BotClient {
    User[] oldsuTopPlayers = new User[11];

    public BeyleyChan(String username, String plainPassword, char prefix) {
        super(username, plainPassword, prefix);
    }

    @Override
    public void onCommandMessage(String sender, String target, String command, String arguments) {
        try {
            switch (command) {
                case "help":
                    String[] HelpMessages = new String[] {
                            "Hello! I'm Beyley-chan, PoltixeTheDerg's bot. (name literally comes from her real name lmao)",
                            "Currently I'm in early stages of development so I can't do much yet!",
                            "The currently Available commands are as following:",
                            ": " + prefix + "top10 : Lists the top 10 players",
                            ": " + prefix + "nextrank : Shows the required score to reach the next rank",
                            ": " + prefix + "u : Shows basic stats about you",
                            ": " + prefix + "help : You are currently looking at this!!" };

                    for (String helpString : HelpMessages) {
                        packetSender.sendMessage(username, helpString, target);
                    }

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
                    } catch (IOException | InterruptedException ex) {
                    }

                    Object paresdJson = new JSONParser().parse(json);

                    JSONObject jo = (JSONObject) paresdJson;

                    JSONArray ja = (JSONArray) jo.get("users");

                    Iterator<?> allUsers = ja.iterator();

                    long lastScore = Long.parseLong((String) ((JSONObject) ja.get(0)).get("ranked_score"));

                    String returnMessage = "";

                    for (int i = 0; i < 10; i++) {
                        StringBuffer playerUsername = new StringBuffer(
                                (String) ((JSONObject) ja.get(i)).get("username"));

                        returnMessage = String.format("#%s | %s | Score:%,.0f",
                                (String) ((JSONObject) ja.get(i)).get("rank"), playerUsername.toString(),
                                Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));

                        if (i > 0) {
                            returnMessage += String.format(" - %,.0f score behind", (double) lastScore
                                    - Double.parseDouble((String) ((JSONObject) ja.get(i)).get("ranked_score")));
                        }

                        lastScore = Long.parseLong((String) ((JSONObject) ja.get(i)).get("ranked_score"));

                        packetSender.sendMessage(username, returnMessage, target);
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
                        packetSender.sendMessage(username, helpString, target);
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

                    packetSender.sendMessage(username, returnMessage, target);

                    break;
                default:
                    packetSender.sendMessage(username,
                            "That command does not exist! Type @help for a list of commands!", target);
                    break;
            }
        } catch (ParseException e) {
        }
    }

    @Override
    public void onAuthComplete() {
        this.packetSender = new PacketSender(client);

        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with Flan-chan in the fields!");

        Timer timer = new Timer();

        // Schedule the user rank check
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                    String json = response.body();
                    Object paresdJson = new JSONParser().parse(json);
                    JSONObject jo = (JSONObject) paresdJson;
                    JSONArray ja = (JSONArray) jo.get("users");

                    User[] oldUserArray = Arrays.copyOf(oldsuTopPlayers, 10);

                    for (int i = 0; i < 11; i++) {
                        JSONObject thisUser = (JSONObject) ja.get(i);

                        oldsuTopPlayers[i] = new User((String) thisUser.get("username"), (Long) thisUser.get("userId"),
                                Long.parseLong((String) thisUser.get("ranked_score")),
                                Integer.parseInt((String) thisUser.get("rank")));
                    }

                    if (oldUserArray[0] != null) {
                        boolean skip = false;

                        for (int i = 0; i < 10; i++) {
                            if (skip) {
                                skip = false;
                                continue;
                            }

                            if (!oldsuTopPlayers[i].username.equals(oldUserArray[i].username)) {
                                User oldUser = oldUserArray[i];
                                User newUser = oldsuTopPlayers[i];

                                packetSender.sendMessage(username,
                                        String.format("%s has overtaken %s for rank %,.0f with a %,.0f score play!",
                                                newUser.username, oldUser.username, (double) newUser.rank,
                                                (double) newUser.rankedScore - oldUserArray[i + 1].rankedScore),
                                        "#osu");

                                skip = true;
                            }
                        }
                    }
                } catch (IOException | ParseException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 10000);

        // Schedule the keepalive / userstatus packet
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();

                if (now.getHour() > 15 && now.getHour() < 19) {
                    packetSender.updateStatus(SendUserStatusPacket.PLAYING,
                            "nothing, but cuddling eevee till they fall asleep, cus cuties need sleep");

                    return;
                }

                int randomNumber = rand.nextInt(21);

                switch (randomNumber) {
                    case 0:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with Flan-chan in the fields!");
                        break;
                    case 1:
                        long jvmUpTime = Time.getJVMUptime();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

                        LocalDateTime date = Instant.ofEpochMilli(jvmUpTime).atZone(ZoneId.systemDefault())
                                .toLocalDateTime();

                        packetSender.updateStatus(SendUserStatusPacket.PLAYING,
                                String.format("Visual Studio Code for %s", dtf.format(date)));
                        break;
                    case 2:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "oldsu!");
                        break;
                    case 3:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with eve owo");
                        break;
                    case 4:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with thighs");
                        break;
                    case 5:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with thingies ;)");
                        break;
                    case 6:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with uleb128-chan");
                        break;
                    case 7:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with you >^<");
                        break;
                    case 8:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with a cutie");
                        break;
                    case 9:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with UserStatus packets");
                        break;
                    case 10:
                        packetSender.updateStatus(SendUserStatusPacket.WATCHING, "eve code while blushing");
                        break;
                    case 11:
                        packetSender.updateStatus(SendUserStatusPacket.WATCHING, "the haitai background");
                        break;
                    case 12:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with the girls");
                        break;
                    case 13:
                        packetSender.updateStatus(SendUserStatusPacket.EDITING, "her github profile");
                        break;
                    case 14:
                        packetSender.updateStatus(SendUserStatusPacket.WATCHING, "you");
                        break;
                    case 15:
                        packetSender.updateStatus(SendUserStatusPacket.SUBMITTING, "her love confession to eve");
                        break;
                    case 16:
                        packetSender.updateStatus(SendUserStatusPacket.SUBMITTING, "her love confession to eevee");
                        break;
                    case 17:
                        packetSender.updateStatus(SendUserStatusPacket.EDITING, "her love confession to eve");
                        break;
                    case 18:
                        packetSender.updateStatus(SendUserStatusPacket.EDITING, "her love confession to eevee");
                        break;
                    case 19:
                        packetSender.updateStatus(SendUserStatusPacket.MODDING, "her body to appeal to eve");
                        break;
                    case 20:
                        packetSender.updateStatus(SendUserStatusPacket.PLAYING, "with ur bits, look down");
                        break;
                }
            }
        }, 25000, 25000);
    }
}

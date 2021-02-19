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

    Timer getTop11Timer;
    Timer userStatusTimer;

    public BeyleyChan(String username, String plainPassword, char prefix) {
        super(username, plainPassword, prefix);

        this.getTop11Timer = new Timer();
        this.userStatusTimer = new Timer();
    }

    @Override
    public void onAuthComplete() {
        // this.packetSender = new PacketSender(client, console);

        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with Flan-chan in the fields!");

        startBeyleyTimers();
    }

    public void startBeyleyTimers() {
        this.getTop11Timer.cancel();
        this.userStatusTimer.cancel();

        this.getTop11Timer = new Timer();
        this.userStatusTimer = new Timer();

        // Schedule the user rank check
        this.getTop11Timer.scheduleAtFixedRate(new TimerTask() {
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

                    User[] oldUserArray = Arrays.copyOf(oldsuTopPlayers, 11);

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
                                        String.format("%s has overtaken %s for rank #%,.0f with a %,.0f score play!",
                                                newUser.username, oldUser.username, (double) newUser.rank,
                                                (double) newUser.rankedScore - oldUserArray[i + 1].rankedScore),
                                        "#osu");

                                skip = true;
                            }
                        }
                    }
                } catch (IOException | ParseException | InterruptedException ex) {
                    console.printError(
                            "Caught error while updating top 11 players! Error message : " + ex.getMessage());
                }
            }
        }, 0, 20000);

        // Schedule the keepalive / userstatus packet
        this.userStatusTimer.scheduleAtFixedRate(new TimerTask() {
            int newYearSent = -1;

            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();

                if (now.getDayOfYear() == 1) {
                    packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value,
                            "nothing while cuddling everyone to celebrate the new year!");

                    if (newYearSent != now.getHour()) {
                        packetSender.sendMessage(username,
                                "Happy new year everyone! Hope everyone gets lots of cuddles and hugs this year!",
                                "#osu");
                        newYearSent = now.getHour();
                    }
                    return;
                }

                // if (now.getHour() > 15 && now.getHour() < 19) {
                // packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value,
                // "nothing, but cuddling eevee till they fall asleep, cus cuties need sleep");
                //
                // return;
                // }

                int randomNumber = rand.nextInt(25);

                switch (randomNumber) {
                    case 0:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value,
                                "with Flan-chan and Statty in the fields!");
                        break;
                    case 1:
                        long jvmUpTime = Time.getJVMUptime();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

                        LocalDateTime date = Instant.ofEpochMilli(jvmUpTime).atZone(ZoneId.systemDefault())
                                .toLocalDateTime();

                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value,
                                String.format("Visual Studio Code for %s", dtf.format(date)));
                        break;
                    case 2:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "oldsu!");
                        break;
                    case 3:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with eve owo");
                        break;
                    case 4:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with thighs");
                        break;
                    case 5:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with thingies ;)");
                        break;
                    case 6:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with uleb128-chan");
                        break;
                    case 7:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with you >^<");
                        break;
                    case 8:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with a cutie");
                        break;
                    case 9:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with UserStatus packets");
                        break;
                    case 10:
                        packetSender.updateStatus(SendUserStatusPacket.Status.WATCHING.value,
                                "eve code while blushing");
                        break;
                    case 11:
                        packetSender.updateStatus(SendUserStatusPacket.Status.WATCHING.value, "the haitai background");
                        break;
                    case 12:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with the girls");
                        break;
                    case 13:
                        packetSender.updateStatus(SendUserStatusPacket.Status.EDITING.value, "her github profile");
                        break;
                    case 14:
                        packetSender.updateStatus(SendUserStatusPacket.Status.WATCHING.value, "you");
                        break;
                    case 15:
                        packetSender.updateStatus(SendUserStatusPacket.Status.SUBMITTING.value,
                                "her love confession to eve");
                        break;
                    case 16:
                        packetSender.updateStatus(SendUserStatusPacket.Status.SUBMITTING.value,
                                "her love confession to eevee");
                        break;
                    case 17:
                        packetSender.updateStatus(SendUserStatusPacket.Status.EDITING.value,
                                "her love confession to eve");
                        break;
                    case 18:
                        packetSender.updateStatus(SendUserStatusPacket.Status.EDITING.value,
                                "her love confession to eevee");
                        break;
                    case 19:
                        packetSender.updateStatus(SendUserStatusPacket.Status.MODDING.value,
                                "her body to appeal to eve");
                        break;
                    case 20:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value, "with ur bits, look down");
                        break;
                    case 21:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PLAYING.value,
                                "with Statty! she kinda cute ><");
                        break;
                    case 22:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PAUSED.value,
                                "her life thinking about eve ><");
                        break;
                    case 23:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PAUSED.value,
                                "her life thinking about eevee ><");
                        break;
                    case 24:
                        packetSender.updateStatus(SendUserStatusPacket.Status.PAUSED.value,
                                "her life thinking about statty ><");
                        break;
                }
            }
        }, 25000, 25000);
    }

    @Override
    public void onCommandMessage(String sender, String target, String command, String[] arguments) {
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
                case "top10":
                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://oldsu.ayyeve.xyz/api/global_leaderboard/")).build();

                    HttpResponse<String> response = null;

                    String json = "";

                    try {
                        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        json = response.body();
                    } catch (IOException | InterruptedException ex) {
                        this.console.printError(String.format(
                                "Error getting top 10 players! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s",
                                sender, target, command, arguments, ex.getMessage()));
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
                    } catch (IOException | InterruptedException ex) {
                        this.console.printError(String.format(
                                "Error getting players info! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s",
                                sender, target, command, arguments, ex.getMessage()));
                    }

                    paresdJson = new JSONParser().parse(json);

                    jo = (JSONObject) paresdJson;

                    ja = (JSONArray) jo.get("users");

                    allUsers = ja.iterator();

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

                            break;
                        }
                    }

                    for (String thisReturnMessage : returnMessages) {
                        packetSender.sendMessage(username, thisReturnMessage, target);
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
                        this.console.printError(String.format(
                                "Error getting players info! Sender:%s Target:%s Command:%s Arguments:%s     Error:%s",
                                sender, target, command, arguments, ex.getMessage()));
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
    public void onBotDisconnect() {
        try {
            this.getTop11Timer.cancel();
            this.userStatusTimer.cancel();
        } catch (IllegalStateException ex) {

        }

        this.getTop11Timer = new Timer();
        this.userStatusTimer = new Timer();
    }

    @Override
    public void onMessage(String sender, String target, String message) {
        if (message.toLowerCase().contains("beyley")) {
            try {
                packetSender.sendMessage(this.username, String.format("Hello, %s.", sender), target);
                Thread.sleep(2500);
                packetSender.sendMessage(this.username, String.format("We know you're there.", sender), target);
                Thread.sleep(2500);
                packetSender.sendMessage(this.username, String.format("Respond to us.", sender), target);
            } catch (InterruptedException ex) {

            }
        }
    }
}

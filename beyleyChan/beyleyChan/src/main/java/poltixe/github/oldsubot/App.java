package poltixe.github.oldsubot;

import java.io.IOException;
import java.util.*;

import org.json.simple.parser.ParseException;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Config cfg = new Config();

        BeyleyChan flanchoBot = new BeyleyChan(cfg.getProperty("userName"), cfg.getProperty("userPassword"), '@');

        Thread botThread = new Thread();

        try {
            botThread = new Thread(() -> {
                flanchoBot.showLocationData = true;
                flanchoBot.ip = cfg.getProperty("ip");

                try {
                    flanchoBot.initialize();
                } catch (InterruptedException | IOException | ParseException e) {
                    // e.printStackTrace();
                }
            });

            botThread.start();
        } catch (Exception e) {
            // e.printStackTrace();
        }

        port(81);

        WebHandler handler = new WebHandler(flanchoBot);

        get("/", (req, res) -> handler.homePage(req));
        get("/pagedata", (req, res) -> handler.homePageData(req));
        post("/pagedata", (req, res) -> handler.homePageData(req));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String nextMessage = scanner.nextLine();

            if (nextMessage.equals("stopbot")) {
                flanchoBot.packetSender.sendMessage(flanchoBot.username, "Bot is shutting down!", "#osu");

                break;
            }

            if (flanchoBot.authenticated)
                flanchoBot.packetSender.sendMessage(flanchoBot.username, nextMessage, "#osu");
        }

        flanchoBot.disconnect();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        botThread.stop();

        scanner.close();

        System.exit(0);
    }
}

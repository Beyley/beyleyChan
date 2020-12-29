package poltixe.github.oldsubot;

import java.io.IOException;
import java.util.*;

import org.json.simple.parser.ParseException;

public class App {
    public static void main(String[] args) {
        Config cfg = new Config();

        try {
            new Thread(() -> {
                BeyleyChan flanchoBot = new BeyleyChan(cfg.getProperty("userName"), cfg.getProperty("userPassword"), '@');

                flanchoBot.showLocationData = true;
                flanchoBot.ip = cfg.getProperty("ip");

                try {
                    flanchoBot.initialize();
                } catch (InterruptedException | IOException | ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        scanner.close();
    }
}

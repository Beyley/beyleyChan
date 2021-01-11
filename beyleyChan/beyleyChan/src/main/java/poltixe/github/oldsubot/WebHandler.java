package poltixe.github.oldsubot;

import java.util.*;

import poltixe.github.flanchobotlibrary.objects.*;
import spark.*;

public class WebHandler {
    BeyleyChan bot;

    public WebHandler(BeyleyChan bot) {
        this.bot = bot;
    }

    public String homePage(Request req) {
        String returnString = "<html><script>setInterval(function(){ fetch('/pagedata').then(response => response.text()).then(result => document.getElementById(\"content\").innerHTML = result); }, 3000);</script><div id=\"content\"></div></html>";

        return returnString;
    }

    public String homePageData(Request req) {
        String returnString = "";

        try {
            returnString += "Welcome to the stats page for my oldsu bot!<br><br>";

            List<Player> playersCopy = new ArrayList<Player>(bot.allOnlinePlayers);
            List<Match> matchesCopy = new ArrayList<Match>(bot.allMultiplayerMatches);

            Collections.sort(playersCopy, new Comparator<Player>() {
                public int compare(Player left, Player right) {
                    return (int) (left.rank - right.rank);
                }
            });

            returnString += String.format(
                    "<table style=\"width:100%%\"><tr><th>All online players (%d)</th><th>Current multiplayer matches (%d)</th></tr><tr><td style=\"text-align: center; vertical-align: text-top; width: 30%%;\">",
                    playersCopy.size(), matchesCopy.size());

            for (Player player : playersCopy) {
                returnString += String.format("<b>(#%d) %s, %s %s</b><br>", player.rank, player.username,
                        player.getStatus(), player.statusText);
            }

            returnString += "</td><td>";

            for (Match match : matchesCopy) {
                returnString += String.format(
                        "Match ID: <b>%d</b>, Match Name: <b>%s</b>, Game Started: <b>%s</b>, Map Name: <b>%s</b><br>",
                        match.matchId, match.gameName, match.inProgress, match.beatmapName);

                returnString += "<table style=\"border: 1px solid black; border-collapse: collapse;\">";
                returnString += "<tr>";
                returnString += "<th>Username</th>";
                returnString += "<th>Status</th>";
                returnString += "</tr>";

                for (Match.Slot slot : match.slots) {
                    Player player = match.getPlayerFromId(slot.userId, playersCopy);

                    returnString += "<tr>";

                    if (player == null) {
                        returnString += String.format("<th>%s</th>", slot.userId);
                    } else {
                        returnString += String.format("<th>%s</th>", player.username);
                    }

                    for (Match.Slot.SlotStatus status : slot.getStatuses()) {
                        returnString += String.format("<th>%s</th>", status);
                    }

                    returnString += "</tr>";
                }

                returnString += "</table><br>";
            }

            returnString += "</td></tr></table>";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnString;
    }
}

package poltixe.github.oldsubot;

public class User {
    public float accuracy;
    public int countA;
    public int countS;
    public int countSS;
    public String country;
    public String countryCode;
    public double efficiency;
    public float level;
    public int playcount;
    public int rank;
    public long rankedScore;
    public long totalScore;
    public long userId;
    public String username;

    public User(String username, long userId, long rankedScore, int rank) {
        this.username = username;
        this.userId = userId;
        this.rankedScore = rankedScore;
        this.rank = rank;
    }
}

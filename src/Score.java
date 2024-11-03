import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {
    private String playerName;
    private int playerScore;

    public Score(String name, int score) {
        this.playerName = name;
        this.playerScore = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(other.playerScore, this.playerScore); // Descending order
    }
}

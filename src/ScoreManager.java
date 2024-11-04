import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private List<Score> scores;

    public ScoreManager() {
        scores = loadScores();
    }

    public List<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
        saveScores();
    }

    private void saveScores() {
        try (FileWriter writer = new FileWriter("scores.dat")) {
            for (Score score : scores) {
                writer.write(score.getPlayerName() + "," + score.getScore() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Score> loadScores() {
        List<Score> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("scores.dat"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new Score(playerName, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }
}

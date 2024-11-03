import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private static final String FILE_NAME = "scores.dat";
    private List<Score> scores;

    public ScoreManager() {
        scores = loadScores();
    }

    public void addScore(Score score) {
        scores.add(score);
        Collections.sort(scores); // Sort scores in descending order
        saveScores();
    }

    public List<Score> getTopScores(int topN) {
        return scores.subList(0, Math.min(topN, scores.size()));
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Score> loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Score>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); // Return empty if no scores saved yet
        }
    }
}

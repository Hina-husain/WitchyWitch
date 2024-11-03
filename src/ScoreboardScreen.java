import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreboardScreen {
    private JFrame frame;
    private ScoreManager scoreManager;

    public ScoreboardScreen() {
        scoreManager = new ScoreManager();
        frame = new JFrame("Scoreboard");
        frame.setSize(360, 640);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        List<Score> topScores = scoreManager.getTopScores(10); // Display top 10
        for (Score score : topScores) {
            JLabel label = new JLabel(score.getPlayerName() + ": " + score.getPlayerScore());
            label.setFont(new Font("Arial", Font.PLAIN, 24));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}

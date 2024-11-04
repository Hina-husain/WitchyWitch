import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScoreboardScreen {
    private JFrame scoreboardFrame;
    private Image backgroundImg;
    private ScoreManager scoreManager;
    private Clip clip;

    public ScoreboardScreen() {
        scoreManager = new ScoreManager(); // Load scores from file
        loadBackgroundImage(); // Load background image
        createScoreboard();
        playEndingSound(); // Play ending sound
    }

    private void loadBackgroundImage() {
        backgroundImg = new ImageIcon(getClass().getResource("./images/background.jpg")).getImage();
    }

    private void playEndingSound() {
        try {
            // Load the sound file
            File soundFile = new File(getClass().getResource("./sounds/opening_sound.wav").getFile());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Increase the volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float newGain = 10.0f; // Increase volume by 10 decibels (you can adjust this value as needed)
            if (newGain > gainControl.getMaximum()) {
                newGain = gainControl.getMaximum(); // Ensure the gain doesn't exceed the maximum allowed value
            }
            gainControl.setValue(newGain);

            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the sound continuously
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void createScoreboard() {
        scoreboardFrame = new JFrame("Scoreboard");
        scoreboardFrame.setSize(360, 640);
        scoreboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scoreboardFrame.setLocationRelativeTo(null);

        // Custom panel with background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        // Create a panel for displaying scores
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false); // Make the panel transparent

        // Title label
        JLabel titleLabel = new JLabel("Scoreboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Algerian", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK); // Set text color to white for better visibility
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(titleLabel);

        // Add each score to the panel
        List<Score> scores = scoreManager.getScores(); // Use the getter method
        if (scores.isEmpty()) {
            JLabel noScoresLabel = new JLabel("No scores available.", SwingConstants.CENTER);
            noScoresLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noScoresLabel.setForeground(Color.BLACK); // Set text color to white for better visibility
            noScoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scorePanel.add(noScoresLabel);
        } else {
            for (Score score : scores) {
                JLabel scoreLabel = new JLabel(score.getPlayerName() + ": " + score.getScore(), SwingConstants.CENTER);
                scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                scoreLabel.setForeground(Color.BLACK); // Set text color to white for better visibility
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                scorePanel.add(scoreLabel);
            }
        }

        // Add back button
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            scoreboardFrame.dispose(); // Close the scoreboard
            new GameOverScreen(0); // Assuming you have a main menu class to return to
        });
        scorePanel.add(backButton);

        // Add score panel to background panel
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(scorePanel);
        backgroundPanel.add(Box.createVerticalGlue());

        scoreboardFrame.add(backgroundPanel);
        scoreboardFrame.setVisible(true);
    }

    // Inner class for background panel
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

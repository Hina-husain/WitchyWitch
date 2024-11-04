import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

  public class GameOverScreen {
    private JFrame gameOverFrame;
    private Image backgroundImg;
    private ScoreManager scoreManager;
    private Clip clip;
    private boolean isScoreSaved = false; // Track if the score is already saved

    public GameOverScreen(double score) {
        // Load background image
        backgroundImg = new ImageIcon(getClass().getResource("./images/background.jpg")).getImage();
        scoreManager = new ScoreManager();

        // Create Game Over screen JFrame
        gameOverFrame = new JFrame("Game Over");
        gameOverFrame.setSize(360, 640);
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setLocationRelativeTo(null);

        // Custom panel with background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        // Game Over message
        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Algerian", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score message
        JLabel scoreLabel = new JLabel("Your Score is: " + (int) score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Algerian", Font.PLAIN, 28));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label for name input field
        JLabel nameLabel = new JLabel("Enter Your Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Player name input
        JTextField nameInput = new JTextField(15);
        nameInput.setMaximumSize(new Dimension(200, 30));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button to save the player's name to a file
        JButton saveNameButton = new JButton("Save Name");
        saveNameButton.setFont(new Font("Arial", Font.PLAIN, 24));
        saveNameButton.setBackground(Color.BLACK);
        saveNameButton.setForeground(Color.WHITE);
        saveNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isScoreSaved) {
                    String playerName = nameInput.getText().trim();
                    if (!playerName.isEmpty()) {
                        scoreManager.addScore(new Score(playerName, (int) score)); // Save the score with the player's name
                        saveNameToFile(playerName, (int) score); // Save name and score to file
                        isScoreSaved = true; // Mark the score as saved
                        JOptionPane.showMessageDialog(gameOverFrame, "Name and score saved successfully!");
                    } else {
                        JOptionPane.showMessageDialog(gameOverFrame, "Please enter a name.");
                    }
                } else {
                    JOptionPane.showMessageDialog(gameOverFrame, "Score has already been saved.");
                }
            }
        });

        // Button to go to the scoreboard
        JButton scoreboardButton = new JButton("View Scoreboard");
        scoreboardButton.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreboardButton.setBackground(Color.BLACK);
        scoreboardButton.setForeground(Color.WHITE);
        scoreboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScoreboardScreen();  // Opens the scoreboard screen
                gameOverFrame.dispose(); // Close the Game Over screen
            }
        });

        // Retry button
        JButton retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Arial", Font.PLAIN, 24));
        retryButton.setBackground(Color.BLACK);
        retryButton.setForeground(Color.WHITE);
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isScoreSaved) {
                    String playerName = nameInput.getText().trim();
                    if (!playerName.isEmpty()) {
                        scoreManager.addScore(new Score(playerName, (int) score)); // Save score with name
                        saveNameToFile(playerName, (int) score); // Save name and score to file
                        isScoreSaved = true; // Mark the score as saved
                    }
                }
                gameOverFrame.dispose(); // Close Game Over screen
                SwingUtilities.invokeLater(() -> new App()); // Restart the game
            }
        });

        // Add components to the background panel
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(gameOverLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(scoreLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(nameLabel); // Name label
        backgroundPanel.add(nameInput); // Name input field
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(saveNameButton); // Add the save name button
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        backgroundPanel.add(scoreboardButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(retryButton);
        backgroundPanel.add(Box.createVerticalGlue());

        // Add background panel to the frame
        gameOverFrame.add(backgroundPanel);
        gameOverFrame.setVisible(true);

        // Add Ending Sound
        playEndingSound();
    }

    private void playEndingSound() {
        try {
            // Load the sound file
            File soundFile = new File(getClass().getResource("./sounds/Ending_sound.wav").getFile());
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

    private void saveNameToFile(String playerName, int score) {
        try (FileWriter writer = new FileWriter("scores.dat", true)) { // Append mode
            writer.write(playerName + "," + score + "\n"); // Save name and score
        } catch (IOException e) {
            JOptionPane.showMessageDialog(gameOverFrame, "Error saving name: " + e.getMessage());
        }
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

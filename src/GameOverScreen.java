import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverScreen {
    private JFrame gameOverFrame;
    private Image backgroundImg;
    private ScoreManager scoreManager;

    public GameOverScreen(double score) {
        // Load background image
        backgroundImg = new ImageIcon(getClass().getResource("./images/background.jpg  ")).getImage();
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
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score message
        JLabel scoreLabel = new JLabel("Your Score is: " + (int) score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Player name input
        JTextField nameInput = new JTextField("Enter Your Name", 15);
        nameInput.setMaximumSize(new Dimension(200, 30));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button to go to the scoreboard
        JButton scoreboardButton = new JButton("View Scoreboard");
        scoreboardButton.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameInput.getText().trim();
                if (!playerName.isEmpty()) {
                    scoreManager.addScore(new Score(playerName, (int) score)); // Save the score
                    new ScoreboardScreen();  // Opens the scoreboard screen
                    gameOverFrame.dispose(); // Close the Game Over screen
                }
            }
        });

        // Add components to the background panel
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(gameOverLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(scoreLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(nameInput);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        backgroundPanel.add(scoreboardButton);
        backgroundPanel.add(Box.createVerticalGlue());

        // Add background panel to the frame
        gameOverFrame.add(backgroundPanel);
        gameOverFrame.setVisible(true);
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

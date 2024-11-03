import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class SplashScreen {
    private JFrame splashFrame;
    private JButton startButton;
    private Image backgroundImg;

    public SplashScreen() {
        // Load background image
        backgroundImg = new ImageIcon(getClass().getResource("./bg.png")).getImage();
        
        // Create the splash screen JFrame
        splashFrame = new JFrame("Flappy Bird - Splash Screen");
        splashFrame.setSize(360, 640);
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashFrame.setLocationRelativeTo(null);
        
        // Custom panel with background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        
        // Title label
        JLabel titleLabel = new JLabel("Flappy Bird", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the title horizontally

        // Start Button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setPreferredSize(new Dimension(150, 50)); // Set button size
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Add spacing and components to the background panel
        backgroundPanel.add(Box.createVerticalGlue()); // Push content to the center
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createVerticalStrut(20)); // Add space between title and button
        backgroundPanel.add(startButton);
        backgroundPanel.add(Box.createVerticalGlue()); // Push content to the center

        splashFrame.add(backgroundPanel); // Add the background panel to the frame
        splashFrame.setVisible(true);
    }

    private void startGame() {
        splashFrame.dispose(); // Close splash screen
        new App(); // Start the game
    }

    // Custom JPanel to draw the background image
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        new SplashScreen(); // Display splash screen
    }
}

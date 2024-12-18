import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class WitchyWitch extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image witchImg;
    Image topPipeImg;
    Image bottomPipeImg;
    JFrame gameFrame; 

    //witch class
    int witchX = boardWidth/8;
    int witchY = boardWidth/2;
    int witchwidth = 65; 
    int witchHeight = 70 ;

    class Witch {
        int x = witchX;
        int y = witchY;
        int width = witchwidth;  
        int height = witchHeight;
        Image img; 

        Witch(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 60 ;  //scaled by 1/6
    int pipeHeight = 450;
    
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    Witch witch;
    int velocityX = -4; //move pipes to the left speed (simulates witch moving right)
    int velocityY = 0; //move witch up/down speed.
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    Clip jumpSound; // Sound effect for jumping

    Clip collisionSound; // Sound effect for collision (fall or touching a pipe)

    WitchyWitch(JFrame frame) {
        this.gameFrame = frame;
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./images/background.jpg")).getImage();
        witchImg = new ImageIcon(getClass().getResource("./images/witch.png ")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./images/pipedown.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./images/pipeup.png")).getImage();

        //witch
        witch = new Witch(witchImg);
        pipes = new ArrayList<Pipe>();

        // Load sounds
        loadJumpSound();
        loadCollisionSound();

        //place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              placePipes();
              
            }
        });
        placePipeTimer.start();
        
		//game timer
		gameLoop = new Timer(1000/60, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}

    // On Click Sound Effects
    void loadJumpSound() {
        try {
            // Load the sound file
            File soundFile = new File(getClass().getResource("./sounds/jump.wav").getFile());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            jumpSound = AudioSystem.getClip();
            jumpSound.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void playJumpSound() {
        if (jumpSound != null) {
            if (jumpSound.isRunning()) {
                jumpSound.stop();
            }
            jumpSound.setFramePosition(0); // Rewind to the beginning
            jumpSound.start();
        }
    }
    
    // Pipes Position
    void placePipes() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;
    
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y  + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

    // Add elements to the Frame
	public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
    
        // Witch
        g.drawImage(witchImg, witch.x, witch.y, witch.width, witch.height, null);
    
        // Pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
    
        // Set font for score/game over message
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        FontMetrics fm = g.getFontMetrics();
        
        String gameOverText = "Game Over: " + String.valueOf((int) score);
        String scoreText = String.valueOf((int) score);
        
        // Position for the text box
        int boxX = 2;
        int boxY = 10;
        int boxPadding = 10;
        
        // Check if game is over
        if (gameOver) {
            int textWidth = fm.stringWidth(gameOverText);
            int textHeight = fm.getHeight();
    
            // Draw black background box
            g.setColor(Color.BLACK);
            g.fillRect(boxX, boxY, textWidth + boxPadding * 2, textHeight + boxPadding * 2);
    
            // Draw white text on top
            g.setColor(Color.WHITE);
            g.drawString(gameOverText, boxX + boxPadding, boxY + textHeight + boxPadding - 5);
        } else {
            int textWidth = fm.stringWidth(scoreText);
            int textHeight = fm.getHeight();
    
            // Draw black background box for score
            g.setColor(Color.BLACK);
            g.fillRect(boxX, boxY, textWidth + boxPadding * 2, textHeight + boxPadding * 2);
    
            // Draw white score text on top
            g.setColor(Color.WHITE);
            g.drawString(scoreText, boxX + boxPadding, boxY + textHeight + boxPadding - 5);
        }
    }

    // Movement of the Witch
    public void move() {
        //witch
        velocityY += gravity;
        witch.y += velocityY;
        witch.y = Math.max(witch.y, 0); //apply gravity to current witch.y, limit the witch.y to top of the canvas

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && witch.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if (collision(witch, pipe)) {
                playCollisionSound(); // Play collision sound
                gameOver = true;
            }
        }

        if (witch.y > boardHeight) {
            playCollisionSound(); // Play collision sound
            gameOver = true;
        }
    }

    boolean collision(Witch a, Pipe b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    // Collision sound 
    void loadCollisionSound() {
        try {
            // Load the sound file
            File soundFile = new File(getClass().getResource("./sounds/Collision.wav").getFile());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            collisionSound = AudioSystem.getClip();
            collisionSound.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void playCollisionSound() {
        if (collisionSound != null) {
            if (collisionSound.isRunning()) {
                collisionSound.stop();
            }
            collisionSound.setFramePosition(0); // Rewind to the beginning
            collisionSound.start();
        }
    }    


    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            gameFrame.dispose(); 
            placePipeTimer.stop();
            gameLoop.stop();
            new GameOverScreen(score);
            // Display Game Over screen with score
            // SwingUtilities.invokeLater(() -> new GameOverScreen(score));
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            velocityY = -9;
            playJumpSound();    

            if (gameOver) {
                //restart game by resetting conditions
                witch.y = witchY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
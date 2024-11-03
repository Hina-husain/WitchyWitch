import javax.swing.*;

public class App {
    public App() {
        int boardwidth = 360;
        int boardheight = 640;
        JFrame frame = new JFrame("Flappy Bird");
        
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        WitchyWitch flappyBird = new WitchyWitch(frame );
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}

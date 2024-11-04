import javax.swing.*;

public class App {
    public App() {
        int boardwidth = 360;
        int boardheight = 640;
        JFrame frame = new JFrame("Witchy Witch");
        
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        WitchyWitch witchywitch = new WitchyWitch(frame );
        frame.add(witchywitch);
        frame.pack();
        witchywitch.requestFocus();
        frame.setVisible(true);
    }
}

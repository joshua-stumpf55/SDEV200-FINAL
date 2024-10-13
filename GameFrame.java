import javax.swing.JFrame;
import java.util.List;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public GameFrame(String playerName, List<PlayerScore> leaderboard) {
        GamePanel panel = new GamePanel(playerName, leaderboard);
        this.add(panel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

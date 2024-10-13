import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField playerNameField;
    private JLabel highScoreLabel;
    private static List<PlayerScore> leaderboard = new ArrayList<>();

    public MenuFrame() {
        this.setTitle("Snake Game Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:");
        playerNameField = new JTextField(15);
        highScoreLabel = new JLabel("High Score: " + getHighScore());

        JButton startButton = new JButton("Start New Game");
        startButton.addActionListener(e -> {
            String playerName = playerNameField.getText();
            if (!playerName.isEmpty()) {
                new GameFrame(playerName, leaderboard);
                dispose();
            }
        });

        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.addActionListener(e -> showLeaderboard());

        this.add(nameLabel);
        this.add(playerNameField);
        this.add(highScoreLabel);
        this.add(startButton);
        this.add(leaderboardButton);

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private int getHighScore() {
        return leaderboard.stream().mapToInt(PlayerScore::getScore).max().orElse(0);
    }

    private void showLeaderboard() {
        StringBuilder leaderboardText = new StringBuilder("<html>Leaderboard:<br>");
        leaderboard.stream()
                   .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                   .forEach(playerScore -> leaderboardText.append(playerScore.getName())
                                                          .append(": ")
                                                          .append(playerScore.getScore())
                                                          .append("<br>"));
        leaderboardText.append("</html>");
        
        JOptionPane.showMessageDialog(this, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void updateLeaderboard(String playerName, int score) {
        boolean playerFound = false;
        for (PlayerScore playerScore : leaderboard) {
            if (playerScore.getName().equalsIgnoreCase(playerName)) {
                if (score > playerScore.getScore()) {
                    playerScore.setScore(score);
                }
                playerFound = true;
                break;
            }
        }
        if (!playerFound) {
            leaderboard.add(new PlayerScore(playerName, score));
        }
    }
}

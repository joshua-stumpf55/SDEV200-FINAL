import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    final int[] x = new int[NUMBER_OF_UNITS];
    final int[] y = new int[NUMBER_OF_UNITS];

    int length = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;

    String playerName;
    List<PlayerScore> leaderboard;

    public GamePanel(String playerName, List<PlayerScore> leaderboard) {
        this.playerName = playerName;
        this.leaderboard = leaderboard;
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT + 50));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addFood();
        running = true;
        timer = new Timer(80, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'L') x[0] -= UNIT_SIZE;
        if (direction == 'R') x[0] += UNIT_SIZE;
        if (direction == 'U') y[0] -= UNIT_SIZE;
        if (direction == 'D') y[0] += UNIT_SIZE;
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.fillRect(0, HEIGHT, WIDTH, 50);
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Sans serif", Font.BOLD, 15));
            graphics.drawString("Player: " + playerName, 10, HEIGHT + 25);
            graphics.drawString("Score: " + foodEaten, WIDTH - 100, HEIGHT + 25);

            graphics.setColor(new Color(210, 115, 90));
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            graphics.setColor(Color.white);
            graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 1; i < length; i++) {
                graphics.setColor(new Color(40, 200, 150));
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkHit() {
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) running = false;
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) running = false;

        if (!running) timer.stop();
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        graphics.setFont(new Font("Sans serif", Font.PLAIN, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, HEIGHT / 2 + 50);

        graphics.drawString("Press 'R' to Restart", (WIDTH - metrics.stringWidth("Press 'R' to Restart")) / 2, HEIGHT / 2 + 100);
        graphics.drawString("Press 'M' to Main Menu", (WIDTH - metrics.stringWidth("Press 'M' to Main Menu")) / 2, HEIGHT / 2 + 130);

        MenuFrame.updateLeaderboard(playerName, foodEaten);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_R:
                    if (!running) {
                        new GameFrame(playerName, leaderboard);
                        SwingUtilities.getWindowAncestor(GamePanel.this).dispose();
                    }
                    break;
                case KeyEvent.VK_M:
                    if (!running) {
                        new MenuFrame();
                        SwingUtilities.getWindowAncestor(GamePanel.this).dispose();
                    }
                    break;
            }
        }
    }
}

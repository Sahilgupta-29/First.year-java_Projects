import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Rectangle;

public class Flappybird {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Ball Game");
        GamePanel panel = new GamePanel();
        ImageIcon icon = new ImageIcon("flappy.png");

        frame.add(panel);
        frame.setSize(800,600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int ballX = 100;
    private int ballY = 250;
    private int ballDiameter = 30;
    private int ballVelocity = 0;
    private int gravity = 1;
    private final int jump = -11;
    private boolean gameOver = false;
    private int Score = 0;

    private ArrayList<Rectangle> pipes;
    private final int pipegap = 40;
    private final int pipewidth = 80;
    private final int pipespeed = 3;
    private Random random;
    private Timer timer;
    private Image backgroundImage;
    private Image pipeImage;

    public GamePanel() {
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);
        pipes = new ArrayList<>();
        random = new Random();
        timer = new Timer(20, this);
        timer.start();

        backgroundImage = new ImageIcon("C:\\Users\\Sahil\\IdeaProjects\\patterns\\src\\Fgameback.jpg").getImage();
        pipeImage = new ImageIcon("C:\\Users\\Sahil\\IdeaProjects\\patterns\\src\\pipe.png").getImage();

        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
    }

    private void addPipe(boolean start) {
        int space = 200;
        int minPipeHeight = 100;
        int maxPipeHeight = 200;

        int height = minPipeHeight + random.nextInt(maxPipeHeight - minPipeHeight + 1);
        int x = start ? 800 : pipes.get(pipes.size() - 1).x + 300;

        pipes.add(new Rectangle(x, 0, pipewidth, height));  // top pipe
        pipes.add(new Rectangle(x, height + space, pipewidth, 600 - (height + space)));  // bottom pipe
    }

    private void movePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= pipespeed;

            if (pipe.x + pipewidth < 0) {
                pipes.remove(pipe);
                if (pipe.y == 0) {
                    addPipe(false);
                }
            }
        }
    }

    private void checkCollisions() {
        Rectangle ballRect = new Rectangle(ballX, ballY, ballDiameter, ballDiameter);
        if (ballY <= 0 || ballY >= 600 - ballDiameter) {
            gameOver = true;
        }

        for (Rectangle pipe : pipes) {
            if (ballRect.intersects(pipe)) {
                gameOver = true;
                break;
            }
        }
    }

    private void scoreCard() {
        for (Rectangle pipe : pipes) {
            if (pipe.y == 0 & ballX == pipe.x + pipewidth) {
                Score++;
                break;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the red ball with black border
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the red ball
        g2d.setColor(Color.CYAN);
        g2d.fillOval(ballX, ballY, ballDiameter, ballDiameter);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3)); // 3 pixels wide border
        g2d.drawOval(ballX, ballY, ballDiameter, ballDiameter);

        for (Rectangle pipe : pipes) {
            g.drawImage(pipeImage, pipe.x, pipe.y, pipewidth, pipe.height, this);
        }
        // Drawing Scorecard
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.BOLD, 30));
        g.drawString("Your Score:- " + Score, 20, 30);

        if (gameOver) {
            g.setFont(new Font("Impact", Font.BOLD, 50));
            g.drawString("GAME OVER", 250, 300);
            g.setFont(new Font("Monospaced", Font.BOLD, 30));
            g.drawString("Press SPACE to restart", 230, 350);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Update ball physics
            ballVelocity += gravity;
            ballY += ballVelocity;

            movePipes();
            checkCollisions();
            scoreCard();
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                ballY = 250;
                ballVelocity = 0;
                pipes.clear();
                addPipe(true);
                addPipe(true);
                addPipe(true);
                Score = 0;
                gameOver = false;
            } else {
                ballVelocity = jump;
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
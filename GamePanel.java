package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel  implements ActionListener {

    static final int screen_width = 600;
    static final int screen_heigth = 600;
    static final int unit_size = 25;
    static final int game_units = (screen_width * screen_heigth)/unit_size;
    static final int delay = 75;
    final int x[] = new int[game_units];
    final int y[] = new int[game_units];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // R = RIGHT; L = LEFT; U = UP; D = DOWN
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width,screen_heigth));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            for (int i = 0; i < screen_heigth / unit_size; i++) {
                graphics.drawLine(i * unit_size, 0, i * unit_size, screen_heigth);
                graphics.drawLine(0, i * unit_size, screen_width, i * unit_size);
            }
            graphics.setColor(Color.green);
            graphics.fillOval(appleX, appleY, unit_size, unit_size);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], unit_size, unit_size);
                } else {
                    graphics.setColor(new Color(45, 180, 0));
                    // graphics.setColor(new Color(random.nextInt(255),
                            // random.nextInt(255), random.nextInt(255)));
                    graphics.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score:" + applesEaten,
                    (screen_width - metrics.stringWidth("Score:" + applesEaten))/2,
                    graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (screen_width/unit_size)) * unit_size;
        appleY = random.nextInt((int) (screen_heigth/unit_size)) * unit_size;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks if the collided with the body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;

            }
        }
        // checks if the head touches the border
        if (x[0] < 0) { // left one
            running = false;
        }
        if (x[0] > screen_width) { // right one
            running = false;
        }
        if (y[0] < 0) { // top one
            running = false;
        }
        if (y[0] > screen_heigth) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        // Score
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score:" + applesEaten,
                (screen_width - metrics1.stringWidth("Score:" + applesEaten))/2,
                graphics.getFont().getSize());
        // Game over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over"))/2,
                screen_heigth/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

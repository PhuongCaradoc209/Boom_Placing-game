package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    private final int originalTileSize = 16;
    private final int scale = 3;
    public int tileSize = originalTileSize * scale; //58x58
    private final int maxScreenCol = 8;
    private final int maxScreenRow = 8;
    public int screenWidth = maxScreenCol * tileSize;//1160 px
    public int screenHeight = maxScreenRow * tileSize;//696px

    //WORLD SETTINGS
    public final int maxMap = 1;
    public int currentMap = 0;
    public final int maxWorldCol = 16;
    public final int maxWorldRow = 16;
    public final int worldWidth = maxWorldCol * tileSize;//2400
    public final int worldHeight = maxWorldRow * tileSize;//2400

    // SETUP FPS
    int FPS = 60;

    //SYSTEM
    Thread gameThread;

    //SET UP MAP

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
//        this.addKeyListener(keyHandler);
        this.setVisible(true);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;

        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        //display FPS
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) //as long as gameThread is existed
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
//                System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){}

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.dispose();
    }
}

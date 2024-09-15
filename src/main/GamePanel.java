package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_Interact.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    private final int originalTileSize = 16;
    private final int scale = 5;
    public int tileSize = originalTileSize * scale; //80x80
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

    //SYSTEM
    public TileManager tileMgr = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    //PLAYER
    public Player player = new Player(this, keyHandler, tileMgr);
    //ENTITY
    ArrayList<Entity> entityList = new ArrayList<>();
    //OBJECT
    public ArrayList<Entity>[] obj = new ArrayList[maxMap];
    //INTERACT TILE
    public ArrayList<InteractiveTile>[] iTile = new ArrayList[maxMap];

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
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
                update();
                drawToTempScreen(); //draw to the buffered image
                drawToScreen(); //draw the buffered image to the screen
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void setupGame() {
        //CREATE ENTITIES
        for (int i = 0; i < maxMap; i++) {
            obj[i] = new ArrayList<>();
            iTile[i] = new ArrayList<>();
        }
        //SET ON MAP
        aSetter.setObject();
        aSetter.setInteractiveTile();
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        gameState = titleState;
    }

    public void update() {
        player.update();
        for (int i = 0; i < iTile[0].size(); i++) {
            if (iTile[0].get(i) != null) {
                iTile[0].get(i).update();
            }
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void drawToTempScreen() {
        //DEBUG
        long drawStart = 0;
        if (keyHandler.checkDrawTime == true) {
        drawStart = System.nanoTime();
        }
        //TITTLE SCREEN
        if (gameState != titleState && currentMap == 0) {
            //TILE
            tileMgr.draw(g2);

            //ADD ENTITIES TO THE LIST
            entityList.add(player);

            //DRAW OBJ
            for (Entity value : obj[currentMap]) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            //INTERACTIVE TILE
            for (InteractiveTile interactiveTile : iTile[currentMap]) {
                if (interactiveTile != null) {
                    entityList.add(interactiveTile);
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare((int) e1.worldY, (int) e2.worldY);
                }
            });

            //DRAW ENTITIES
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            //REMOVE ENTITIES TO THE LIST (otherwise, the list become larger after every loop)
            entityList.clear();
        }
        //UI
        ui.draw(g2);
    }
}

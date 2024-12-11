package main;

import UI.UI;
import ai.PathFinder;
import buff.BuffManager;
import entity.Entity;
import entity.EntityManager;
import entity.Player;
import event.EventHandler;
import object.BoomManager;
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
    public final int maxMap = 2;
    public int currentMap;
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
    public EventHandler eHandler = new EventHandler(this);
    public EntityManager entityManager = new EntityManager(this);
    public BoomManager boomManager = new BoomManager(this);
    public ScreenShakeManager screenShakeManager = new ScreenShakeManager();
    public BuffManager buffManagerGame = new BuffManager(this);
    public PathFinder pathFinder = new PathFinder(this);

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
    ArrayList<Entity>[] obj = new ArrayList[maxMap];
    //PROJECTILE
    public ArrayList<Entity> projectileList = new ArrayList<>();
    //ENEMY
    public ArrayList<Entity>[] enemy = new ArrayList[maxMap];

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int mapSelectState = 2;
    public final int menuState = 3;
    public final int gameOverState = 4;
    public final int gameWinState = 5;

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
//                System.out.println("FPS" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void setupGame() {
        //CREATE ENTITIES
        for (int i = 0; i < maxMap; i++) {
            obj[i] = new ArrayList<>();
            enemy[i] = new ArrayList<>();
        }
        //SET ON MAP
        aSetter.setObject();
        aSetter.setInteractiveTile();
        aSetter.setEnemy();

        //ADD ENTITY FOR MANAGER
        for (int i = 0; i < maxMap; i++) {
            entityManager.addEntity(player, i);

            for (Entity entity : enemy[i]) {
                if (entity != null) {
                    entityManager.addEntity(entity, i);
                }
            }
        }

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        gameState = titleState;
    }

    public void update() {
        if (gameState == playState) {
            entityManager.updateEntities();
            boomManager.update();

            for (InteractiveTile tile : aSetter.getObjectMap(currentMap).values()) {
                if (tile != null) {
                    tile.update();
                }
            }

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive)
                        projectileList.get(i).update();
                    else
                        projectileList.remove(i);
                }
            }

            if (enemy[currentMap].isEmpty()){
                gameState = gameWinState;
            }
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        // Áp dụng hiệu ứng rung màn hình khi vẽ
        if (screenShakeManager.isShaking()) {
            screenShakeManager.applyShake((Graphics2D) g);
        }
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void drawToTempScreen() {
        //DEBUG
        long drawStart = 0;
        if (keyHandler.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // Create a temporary graphics context
        if (g2 == null) {
            tempScreen = new BufferedImage(screenWidth2, screenHeight2, BufferedImage.TYPE_INT_ARGB);
            g2 = tempScreen.createGraphics();
        }

        g2.clearRect(0, 0, screenWidth2, screenHeight2);


        if (gameState != mapSelectState && gameState != titleState) {
            tileMgr.draw(g2);

            //PROJECTILE
            for (Entity entity : projectileList) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityList.add(player);
            for (Entity entity : enemy[currentMap]) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            // ADD BOOMS TO THE LIST
            for (Entity entity : boomManager.booms) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }

            // INTERACTIVE TILE
            for (InteractiveTile tile : aSetter.getObjectMap(currentMap).values()) {
                if (tile != null) {
                    tile.draw(g2);
                }
            }

            // SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare((int) e1.worldY, (int) e2.worldY);
                }
            });

            // DRAW ENTITIES
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            //DRAW BUFF
            buffManagerGame.draw(g2);

            // REMOVE ENTITIES TO THE LIST
            entityList.clear();
        }

        // UI
        ui.draw(g2);

        if (screenShakeManager.isShaking()) {
            screenShakeManager.update();
        }
    }

    public void triggerScreenShake() {
        screenShakeManager.startShake(2, 5);
    }
}

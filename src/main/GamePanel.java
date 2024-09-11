package main;

import tile.TileManager;

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

    //SYSTEM
    public TileManager tileMgr = new TileManager(this);

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    boolean fullScreenOn = false;

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
        //CREATE ARRAYLIST FOR ENTITY
//        for (int i = 0; i < maxMap; i++) {
//            obj[i] = new ArrayList<>();
//            npc[i] = new ArrayList<>();
//            animal[i] = new ArrayList<>();
//            iTile[i] = new ArrayList<>();
//        }
//        //SET ON MAP
//        aSetter.setObject();
//        aSetter.setNPC();
//        aSetter.setAnimal(currentMap);
//        aSetter.setInteractiveTile();
//        enviMgr.setUp();
//
//        gameState = tittleState;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

//        //SET FULL SCREEN
//        if (fullScreenOn){
//            setFullScreen();
//        }
    }

    public void update(){}

    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }

    public void drawToTempScreen() {
        //DEBUG
        long drawStart = 0;
//        if (keyHandler.checkDrawTime == true) {
            drawStart = System.nanoTime();
//        }
        tileMgr.draw(g2);
//        //TITTLE SCREEN
//        if (gameState != tittleState && currentMap == 0) {
//            //TILE
//            tileMgr.draw(g2);
//
//            //ADD ENTITIES TO THE LIST
//            entityList.add(player);
//
//            //INTERACTIVE TILE
//            for (InteractiveTile interactiveTile : iTile[currentMap]) {
//                if (interactiveTile != null) {
//                    entityList.add(interactiveTile);
//                }
//            }
//
//            for (Entity entity : npc[currentMap]) {
//                if (entity != null) {
//                    entityList.add(entity);
//                }
//            }
//
//            for (Entity value : obj[currentMap]) {
//                if (value != null) {
//                    entityList.add(value);
//                }
//            }
//
//            for (Entity value : animal[currentMap]) {
//                if (value != null) {
//                    entityList.add(value);
//                }
//            }
//
//            //SORT
//            Collections.sort(entityList, new Comparator<Entity>() {
//                @Override
//                public int compare(Entity e1, Entity e2) {
//                    return Integer.compare((int) e1.worldY, (int) e2.worldY);
//                }
//            });
//
//            //DRAW ENTITIES
//            for (Entity entity : entityList) {
//                entity.draw(g2);
//            }
//
//            //REMOVE ENTITIES TO THE LIST (otherwise, the list become larger after every loop)
//            entityList.clear();
//
//            //ENVIRONMENT
//            enviMgr.draw(g2);
//        } else if (gameState == fishTankState) {
//            //TILE
//            tileMgr.draw(g2);
//
//            //DRAW FISH
//            for (int i = 0; i < animal[1].size(); i++) {
//                animal[1].get(i).draw(g2);
//            }
//
//            //DRAW OBJ
//            for (int i = 0; i < obj[1].size(); i++) {
//                obj[1].get(i).draw(g2);
//            }
//        }
//        //UI
//        ui.draw(g2);
//
//        if (keyHandler.checkDrawTime == true) {
//            long drawEnd = System.nanoTime();
//            long passed = drawEnd - drawStart;
//            g2.setColor(Color.WHITE);
//            g2.drawString("Draw Time: " + passed, 10, 400);
//            System.out.println("Draw Time: " + passed);
//        }

    }
}

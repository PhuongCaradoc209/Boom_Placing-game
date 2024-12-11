package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Boom extends Entity {
    private int worldX, worldY;
    private double screenX, screenY;
    private int col, row;

    private boolean exploded;
    public boolean startExplode;

    //ANIMATION
    BufferedImage[] boomAnim;
    BufferedImage[] fontExplosion, rightExplosion, leftExplosion, upExplosion, downExplosion;
    BufferedImage[] rightBodyExplosion, leftBodyExplosion, upBodyExplosion, downBodyExplosion;

    //FRAME FOR BOOM ANIMATION
    private int frameBoom;
    private int intervalBoom = 14;
    private int currentFrame = 0;
    private int countToExplode = 0;
    private int maxCountToExplode = 6; // Số lần lặp lại toàn bộ animation

    //FRAME FOR EXPLODE ANIMATION
    private int frameExplosion = 0;
    private int intervalExplosion = 14;
    private int currentAnimExplosion = 0;
    private int radiusExplosion;

    //EXPLOSION AREA
    protected HashMap<String, List<int[]>> explosionArea;

    public Boom(int col, int row, Entity entity, GamePanel gp) {
        super(gp);
        boomAnim = new BufferedImage[3];

        this.row = row;
        this.col = col;
        this.worldX = gp.tileSize * col + 10;
        this.worldY = gp.tileSize * row + 10;
        this.exploded = false;
        this.startExplode = false;

        radiusExplosion = entity.getBoomExplosionRadius();
        fontExplosion = new BufferedImage[4];
        rightExplosion = new BufferedImage[4];
        leftExplosion = new BufferedImage[4];
        upExplosion = new BufferedImage[4];
        downExplosion = new BufferedImage[4];

        rightBodyExplosion = new BufferedImage[4];
        leftBodyExplosion = new BufferedImage[4];
        upBodyExplosion = new BufferedImage[4];
        downBodyExplosion = new BufferedImage[4];


        explosionArea = new HashMap<>();
        explosionArea.put("Up", new ArrayList<>());
        explosionArea.put("Down", new ArrayList<>());
        explosionArea.put("Left", new ArrayList<>());
        explosionArea.put("Right", new ArrayList<>());

        setUpImage();
        collision = false;

        getExplodeArea("Up");
        getExplodeArea("Down");
        getExplodeArea("Right");
        getExplodeArea("Left");
    }

    private void setUpImage() {
        for (int i = 0; i < 3; i++) {
            boomAnim[i] = setup("object/boom_" + (i + 1), 3 * gp.tileSize / 4, 3 * gp.tileSize / 4);
        }
        for (int i = 0; i < 4; i++) {
            fontExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-03", gp.tileSize, gp.tileSize);
            rightExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-05", gp.tileSize, gp.tileSize);
            leftExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-07", gp.tileSize, gp.tileSize);
            upExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-01", gp.tileSize, gp.tileSize);
            downExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-09", gp.tileSize, gp.tileSize);

            rightBodyExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-04", gp.tileSize, gp.tileSize);
            leftBodyExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-06", gp.tileSize, gp.tileSize);
            upBodyExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-02", gp.tileSize, gp.tileSize);
            downBodyExplosion[i] = setup("boomExplo/explosion_" + (i + 1) + "-08", gp.tileSize, gp.tileSize);
        }
    }

    public void update() {
        if (startExplode) {
            gp.triggerScreenShake();
            frameExplosion++;
            if (frameExplosion == intervalExplosion) {
                frameExplosion = 0;
                currentAnimExplosion++;
                if (currentAnimExplosion == 4) {
                    exploded = true;
                    currentAnimExplosion = 0;
                }
            }
        } else {
            frameBoom++; // Tăng frameBoom theo thời gian
            if (frameBoom == intervalBoom) {
                frameBoom = 0;
                currentFrame++; // Tăng currentFrame để thay đổi hình ảnh
                if (currentFrame >= boomAnim.length) { // Quay lại frame đầu tiên nếu đã hết
                    currentFrame = 0;
                    countToExplode++;
                }
                if (countToExplode >= maxCountToExplode) {
                    startExplode = true;
                    countToExplode = 0;
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        if (startExplode) {
            drawExplode(g);
        } else {
            drawBoom(g);
        }
    }

    public void drawBoom(Graphics2D g2) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        // STOP MOVING THE CAMERA AT EDGE (ENTITY CAN NOT MOVE IF AT EDGE)
        // TOP
        if (gp.player.screenX >= gp.player.worldX) {
            screenX = worldX;
        }
        // LEFT
        if (gp.player.screenY >= gp.player.worldY) {
            screenY = worldY;
        }
        // RIGHT
        double rightOffSet = gp.screenWidth - gp.player.screenX;
        if (rightOffSet >= gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        // BOTTOM
        double bottomOffSet = gp.screenHeight - gp.player.screenY;
        if (bottomOffSet >= gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }
        g2.drawImage(boomAnim[currentFrame], (int) screenX, (int) screenY, null);
    }

    public void getExplodeArea(String direction){
        int tempRow = 0, tempCol = 0;

        if (Objects.equals(direction, "Up")) tempRow = -1;
        else if (Objects.equals(direction, "Down")) tempRow = 1;
        else if (Objects.equals(direction, "Left")) tempCol = -1;
        else if (Objects.equals(direction, "Right")) tempCol = 1;

        for (int i = 1; i <= radiusExplosion; i++) {
            if (row + tempRow * i >=0 && col + tempCol * i >= 0) {
                if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row + tempRow * i][col + tempCol * i]].collision){
                    explosionArea.get(direction).add(new int[]{row + tempRow * i, col + tempCol * i});
                }
                else {
                    return;
                }
            }
        }
    }

    public void drawExplode(Graphics2D g2) {
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        // STOP MOVING THE CAMERA AT EDGE (ENTITY CAN NOT MOVE IF AT EDGE)
        // TOP
        if (gp.player.screenX >= gp.player.worldX) {
            screenX = worldX;
        }
        // LEFT
        if (gp.player.screenY >= gp.player.worldY) {
            screenY = worldY;
        }
        // RIGHT
        double rightOffSet = gp.screenWidth - gp.player.screenX;
        if (rightOffSet >= gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        // BOTTOM
        double bottomOffSet = gp.screenHeight - gp.player.screenY;
        if (bottomOffSet >= gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        screenX -= 10;
        screenY -= 10;
        g2.drawImage(fontExplosion[currentAnimExplosion], (int) screenX, (int) screenY, null);

        //DRAW UP
        displayExplosion(upBodyExplosion[currentAnimExplosion], upExplosion[currentAnimExplosion], g2, 0, -gp.tileSize, "Up");
        //DRAW DOWN
        displayExplosion(downBodyExplosion[currentAnimExplosion], downExplosion[currentAnimExplosion], g2, 0, gp.tileSize, "Down");
        //DRAW RIGHT
        displayExplosion(rightBodyExplosion[currentAnimExplosion], rightExplosion[currentAnimExplosion], g2, gp.tileSize, 0, "Right");
        //DRAW LEFT
        displayExplosion(leftBodyExplosion[currentAnimExplosion], leftExplosion[currentAnimExplosion], g2, -gp.tileSize, 0, "Left");
    }

    private void displayExplosion(BufferedImage imageExplosion, BufferedImage imageLast, Graphics2D g2, int gapX, int gapY, String direction) {
        int tempRow = 0, tempCol = 0;

        if (Objects.equals(direction, "Up")) tempRow = -1;
        else if (Objects.equals(direction, "Down")) tempRow = 1;
        else if (Objects.equals(direction, "Left")) tempCol = -1;
        else if (Objects.equals(direction, "Right")) tempCol = 1;

        //DRAW BODY
        for (int i = 1; i < radiusExplosion; i++) {
            if (row + tempRow * i >=0 && col + tempCol * i >= 0) {
                if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row + tempRow * i][col + tempCol * i]].collision)
                    g2.drawImage(imageExplosion, (int) (screenX + gapX * i), (int) (screenY + gapY * i), null);
                else return;
            }
        }
        //DRAW THE LAST
        if (row + tempRow * radiusExplosion >=0 && col + tempCol * radiusExplosion >= 0){
            if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row + tempRow * radiusExplosion][col + tempCol * radiusExplosion]].collision)
                g2.drawImage(imageLast, (int) screenX + (gp.tileSize * radiusExplosion) * tempCol, (int) screenY + (gp.tileSize * radiusExplosion) * tempRow, null);
        }
    }

    //Explode
    public boolean isExplode() {
        return exploded;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getRadiusExplosion() {
        return radiusExplosion;
    }

    public void setRadiusExplosion(int radiusExplosion) {
        this.radiusExplosion = radiusExplosion;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }

    public double getWorldX() {
        return worldX;
    }

    public double getWorldY() {
        return worldY;
    }
}
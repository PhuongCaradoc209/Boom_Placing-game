package object;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

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

    public Boom(int col, int row, int countToExplode, GamePanel gp) {
        super(gp);
        boomAnim = new BufferedImage[3];

        this.row = row;
        this.col = col;
        this.worldX = gp.tileSize * col + 10;
        this.worldY = gp.tileSize * row + 10;
        this.exploded = false;
        this.startExplode = false;

        radiusExplosion = 1;
        fontExplosion = new BufferedImage[4];
        rightExplosion = new BufferedImage[4];
        leftExplosion = new BufferedImage[4];
        upExplosion = new BufferedImage[4];
        downExplosion = new BufferedImage[4];

        setUpImage();
        collision = false;
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
        //CHECK UP
        if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row - 1][col]].collision)
            g2.drawImage(upExplosion[currentAnimExplosion], (int) screenX, (int) screenY - gp.tileSize, null);
        //CHECK DOWN
        if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row + 1][col]].collision)
            g2.drawImage(downExplosion[currentAnimExplosion], (int) screenX, (int) screenY + gp.tileSize, null);
        //CHECK RIGHT
        if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row][col + 1]].collision)
            g2.drawImage(rightExplosion[currentAnimExplosion], (int) screenX + gp.tileSize, (int) screenY, null);
        //CHECK LEFT
        if (!gp.tileMgr.tile[gp.tileMgr.mapTileNum[gp.currentMap][row][col - 1]].collision)
            g2.drawImage(leftExplosion[currentAnimExplosion], (int) screenX - gp.tileSize, (int) screenY, null);
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

    public double getScreenX(){
        return screenX;
    }

    public double getScreenY(){
        return screenY;
    }

    public double getWorldX(){
        return worldX;
    }

    public double getWorldY(){
        return worldY;
    }
}
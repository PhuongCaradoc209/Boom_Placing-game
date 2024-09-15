package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Boom extends Entity {
    private boolean exploded;
    BufferedImage[] boomAnim;
    private int frameBoom;
    private int intervalBoom = 14;
    private int currentFrame = 0;
    private int countToExplode = 0;
    private int maxCountToExplode = 6; // Số lần lặp lại toàn bộ animation

    public Boom(int col, int row, int countToExplode, GamePanel gp) {
        super(gp);
        boomAnim = new BufferedImage[3];
        this.worldX = gp.tileSize * col + 10;
        this.worldY = gp.tileSize * row + 10;
        this.exploded = false;
        setUpImage();
    }

    private void setUpImage() {
        for (int i = 0; i < 3; i++) {
            boomAnim[i] = setup("object/boom_" + (i + 1), 3 * gp.tileSize / 4, 3 * gp.tileSize / 4);
        }
    }

    public void update() {
        frameBoom++; // Tăng frameBoom theo thời gian
        if (frameBoom == intervalBoom) {
            frameBoom = 0;
            currentFrame++; // Tăng currentFrame để thay đổi hình ảnh
            if (currentFrame >= boomAnim.length) { // Quay lại frame đầu tiên nếu đã hết
                currentFrame = 0;
                countToExplode++;
            }
            if (countToExplode >= maxCountToExplode) {
                exploded = true;
                countToExplode = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (exploded) {

        } else {
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;

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
            g2.drawImage(boomAnim[currentFrame],  (int) screenX, (int) screenY, null);
        }
    }

    //Explode
    public boolean isExplode() {
        return exploded;
    }
}

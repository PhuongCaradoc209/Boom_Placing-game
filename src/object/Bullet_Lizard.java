package object;

import entity.Projectile;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet_Lizard extends Projectile {
    GamePanel gp;
    public Bullet_Lizard(GamePanel gp) {
        super(gp);
        this.gp = gp;
        size = gp.tileSize - 30;

        name = "Bullet_Slime";
        speed = 3;
        setMaxLife(120);
        setLife(getMaxLife());
        setAttack(1);
        getImage();
    }

    public void getImage(){
        up1 = setup("projectile/lizard/lizard_up_1", 32, 32);
        up2 = setup("projectile/lizard/lizard_up_1", 32, 32);

        right1 = setup("projectile/lizard/lizard_right_1", 32, 32);
        right2 = setup("projectile/lizard/lizard_right_1", 32, 32);

        left1 = setup("projectile/lizard/lizard_left_1", 32, 32);
        left2 = setup("projectile/lizard/lizard_left_1", 32, 32);

        down1 = setup("projectile/lizard/lizard_down_1", 32, 32);
        down2 = setup("projectile/lizard/lizard_down_1", 32, 32);
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
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
        ////////////////////////
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
//                if (spriteNum == 3) {
//                    image = up3;
//                }
//                if (spriteNum == 4) {
//                    image = up4;
//                }
//                if (spriteNum == 5) {
//                    image = up5;
//                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
//                if (spriteNum == 3) {
//                    image = down3;
//                }
//                if (spriteNum == 4) {
//                    image = down4;
//                }
//                if (spriteNum == 5) {
//                    image = down5;
//                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
//                if (spriteNum == 3) {
//                    image = left3;
//                }
//                if (spriteNum == 4) {
//                    image = left4;
//                }
//                if (spriteNum == 5) {
//                    image = left5;
//                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
//                if (spriteNum == 3) {
//                    image = right3;
//                }
//                if (spriteNum == 4) {
//                    image = right4;
//                }
//                if (spriteNum == 5) {
//                    image = right5;
//                }
                break;
        }
        g2.drawImage(image, (int) screenX, (int) screenY + 10, size, size, null);
    }
}

package object;

import entity.Projectile;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet_Slime extends Projectile {
    private BufferedImage up5, down5, right5, left5;
    GamePanel gp;
    public Bullet_Slime(GamePanel gp) {
        super(gp);
        this.gp = gp;
        size = gp.tileSize;

        name = "Bullet_Slime";
        speed = 5;
        setMaxLife(60);
        setLife(getMaxLife());
        setAttack(1);
        getImage();
    }

    public void getImage(){
//        up1 = setup("projectile/slime/bullet_slime_1", 44, 32);
//        up2 = setup("projectile/slime/bullet_slime_2", 44, 32);
//        up3 = setup("projectile/slime/bullet_slime_3", 44, 32);
//        up4 = setup("projectile/slime/bullet_slime_4", 44, 32);
//        up5 = setup("projectile/slime/bullet_slime_5", 44, 32);
//
//        right1 = setup("projectile/slime/bullet_slime_1", 44, 32);
//        right2 = setup("projectile/slime/bullet_slime_2", 44, 32);
//        right3 = setup("projectile/slime/bullet_slime_3", 44, 32);
//        right4 = setup("projectile/slime/bullet_slime_4", 44, 32);
//        right5 = setup("projectile/slime/bullet_slime_5", 44, 32);
//
//        down1 = setup("projectile/slime/bullet_slime_1", 44, 32);
//        down2 = setup("projectile/slime/bullet_slime_2", 44, 2);
//        down3 = setup("projectile/slime/bullet_slime_3", 44, 32);
//        down4 = setup("projectile/slime/bullet_slime_4", 44, 32);
//        down5 = setup("projectile/slime/bullet_slime_5", 44, 32);
//
//        left1 = setup("projectile/slime/bullet_slime_1_left", 44, 32);
//        left2 = setup("projectile/slime/bullet_slime_2_left", 44, 32);
//        left3 = setup("projectile/slime/bullet_slime_3_left", 44, 32);
//        left4 = setup("projectile/slime/bullet_slime_4_left", 44, 32);
//        left5 = setup("projectile/slime/bullet_slime_5_left", 44, 32);


        up1 = setup("projectile/slime/fireball_up_1", 16, 16);
        up2 = setup("projectile/slime/fireball_up_2", 16, 16);

        right1 = setup("projectile/slime/fireball_right_1", 16, 16);
        right2 = setup("projectile/slime/fireball_right_2", 16, 16);

        left1 = setup("projectile/slime/fireball_left_1", 16, 16);
        left2 = setup("projectile/slime/fireball_left_2", 16, 16);

        down1 = setup("projectile/slime/fireball_down_1", 16, 16);
        down2 = setup("projectile/slime/fireball_down_2", 16, 16);
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
        g2.drawImage(image, (int) screenX, (int) screenY, size, size, null);
    }
}

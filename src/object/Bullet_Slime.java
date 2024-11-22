package object;

import entity.Projectile;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet_Slime extends Projectile {
    private BufferedImage[][] animationFrames;
    private int currentFrameIndex;
    private int frameCounter;
    private int frameDelay;
    GamePanel gp;

    public Bullet_Slime(GamePanel gp) {
        super(gp);
        this.gp = gp;
        size = gp.tileSize * 2 + 20;

        name = "Bullet_Slime";
        speed = 2;
        setMaxLife(120);
        setLife(getMaxLife());
        setAttack(1);
        frameCounter = 0;
        frameDelay = 5; // Adjust as needed for smoother/slower animation
        currentFrameIndex = 0;
        loadAnimationFrames();
    }

    public void loadAnimationFrames() {
        animationFrames = new BufferedImage[4][30]; // 4 directions, each with 30 frames

        for (int i = 0; i < 30; i++) {
            animationFrames[0][i] = setup("projectile/slime/up/4_" + (i), 100, 100); // Up animation
            animationFrames[1][i] = setup("projectile/slime/down/2_" + (i), 100, 100); // Down animation
            animationFrames[2][i] = setup("projectile/slime/left/1_" + (i), 100, 100); // Left animation
            animationFrames[3][i] = setup("projectile/slime/right/3_" + (i), 100, 100); // Right animation
        }
    }

    @Override
    public void update() {
        super.update();

        // Increment frame counter and update the current frame if necessary
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrameIndex = (currentFrameIndex + 1) % 30;
            frameCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
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

        int dirIndex = switch (direction) {
            case "up" -> 0;
            case "down" -> 1;
            case "left" -> 2;
            case "right" -> 3;
            default -> 0;
        };

        BufferedImage currentFrame = animationFrames[dirIndex][currentFrameIndex];
        switch (direction) {
            case "up":
                g2.drawImage(currentFrame, (int) screenX - 30, (int) screenY, size, size, null);
                break;
            case "down":
                g2.drawImage(currentFrame, (int) screenX - 30, (int) screenY, size, size, null);
                break;
            case "left":
                g2.drawImage(currentFrame, (int) screenX, (int) screenY - 30, size, size, null);
                break;
            case "right":
                g2.drawImage(currentFrame, (int) screenX, (int) screenY - 30, size, size, null);
                break;
            default:
                break;
        }
    }
}
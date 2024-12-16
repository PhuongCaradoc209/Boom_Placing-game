package enemy;

import entity.Entity;
import main.GamePanel;
import object.Bullet_Lizard;
import object.Bullet_Slime;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Ene_Lizard extends Entity {
    private BufferedImage[] upIdle = new BufferedImage[6];
    private BufferedImage[] downIdle = new BufferedImage[6];
    private BufferedImage[] leftIdle = new BufferedImage[6];
    private BufferedImage[] rightIdle = new BufferedImage[6];
    private BufferedImage[] upRun = new BufferedImage[8];
    private BufferedImage[] downRun = new BufferedImage[8];
    private BufferedImage[] leftRun = new BufferedImage[8];
    private BufferedImage[] rightRun = new BufferedImage[8];
    private BufferedImage[] animationFrames;

    private BufferedImage[] attackUp = new BufferedImage[5];
    private BufferedImage[] attackDown = new BufferedImage[5];
    private BufferedImage[] attackLeft = new BufferedImage[5];
    private BufferedImage[] attackRight = new BufferedImage[5];

    private int animationIndex_idle = 0;
    private int animationIndex_run = 0;
    private long lastTime = 0;
    private static final int ANIMATION_FRAME_TIME = 150;
    private int attackCounter = 0;

    private boolean isRunning;
    private boolean isAttacking;

    public Ene_Lizard(GamePanel gp) {
        super(gp);
        name = "lizard";
        speed = 1;
        direction = "downStand";
        isRunning = false;

        size = gp.tileSize + 5;
        setMaxLife(3);
        setLife(getMaxLife());

        solidArea.x = (10 * gp.tileSize) / 48;
        solidArea.y = (20 * gp.tileSize) / 48;
        solidArea.width = (28 * gp.tileSize) / 48;
        solidArea.height = (30 * gp.tileSize) / 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        getAttackFrames();

        animationFrames = downIdle;
        projectile = new Bullet_Lizard(gp);
    }

    public void getImage() {
        for (int i = 0; i < 6; i++) {
            downIdle[i] = rightIdle[i] = setup("enemy/lizard/idle_right_" + (i + 1), 62, 62);
            upIdle[i] = leftIdle[i] = setup("enemy/lizard/idle_left_" + (i + 1), 62, 62);
        }
        for (int i = 0; i < 8; i++) {
            downRun[i] = rightRun[i] = setup("enemy/lizard/run_right_" + (i + 1), 66, 66);
            leftRun[i] = upRun[i] = setup("enemy/lizard/run_left_" + (i + 1), 66, 66);
        }
    }

    public void getAttackFrames() {
        for (int i = 0; i < 5; i++) {
            attackDown[i] = attackRight[i] = setup("enemy/lizard/attack_right_" + (i + 1), 70, 70);
            attackUp[i] = attackLeft[i] = setup("enemy/lizard/attack_left_" + (i + 1), 70, 70);
        }
    }


    public void update(){
        setAction();
        getEntityCoordinates(this);
        checkCollision();

        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        updateAnimation();

        if (invisible) {
            invisibleCounter++;
            if (invisibleCounter > 60) {
                invisible = false;
                invisibleCounter = 0;
            }
        }

        int xDistance = (int) Math.abs(worldX - gp.player.worldX);
        int yDistance = (int) Math.abs(worldY - gp.player.worldY);
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        if (!onPath && tileDistance < 3){
            int i = new Random().nextInt(100) + 1;
            if (i > 50){
                onPath = true;
            }
        }
        if (onPath && tileDistance > 3){
            onPath = false;
        }
    }

    public void setAction(){
        if (onPath){
            fire();
            int goalCol = Math.min(gp.maxWorldCol - 1, Math.max(0, (int) ((gp.player.worldX + gp.player.solidArea.x) / gp.tileSize)));
            int goalRow = Math.min(gp.maxWorldRow - 1, Math.max(0, (int) ((gp.player.worldY + gp.player.solidArea.y) / gp.tileSize)));
            searchPath(goalRow,goalCol);

            int i = new Random().nextInt(200);
            if (i>197 && !projectile.alive && shotAvailableCounter == 30){
                projectile.set(worldX, worldY, direction,true,this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }
            animationFrames = switch (direction) {
                case "up" -> upRun;
                case "down" -> downRun;
                case "left" -> leftRun;
                case "right" -> rightRun;
                default -> downRun;
            };
            isRunning = true;
        }
        else {
            actionLookCounter++;

            if (actionLookCounter == 100) {
                int randomBehavior = (int) (Math.random() * 2);
                if (randomBehavior == 0) {
                    isRunning = false;
                    switch ((int) (Math.random() * 4)) {
                        case 0 -> direction = "upStand";
                        case 1 -> direction = "downStand";
                        case 2 -> direction = "leftStand";
                        case 3 -> direction = "rightStand";
                    }
                    animationFrames = switch (direction) {
                        case "upStand" -> upIdle;
                        case "downStand" -> downIdle;
                        case "leftStand" -> leftIdle;
                        case "rightStand" -> rightIdle;
                        default -> downIdle;
                    };
                } else {
                    isRunning = true;
                    switch ((int) (Math.random() * 4)) {
                        case 0 -> direction = "up";
                        case 1 -> direction = "down";
                        case 2 -> direction = "left";
                        case 3 -> direction = "right";
                    }
                    animationFrames = switch (direction) {
                        case "up" -> upRun;
                        case "down" -> downRun;
                        case "left" -> leftRun;
                        case "right" -> rightRun;
                        default -> downRun;
                    };
                }

                actionLookCounter = 0;
            }
        }
    }

    public void fire() {
        if (!isAttacking) {
            int i = new Random().nextInt(100) + 1;
            if (i > 99 && !projectile.alive && shotAvailableCounter == 100) {
                isAttacking = true;
                attackCounter = 0;
            }
        }

        if (shotAvailableCounter < 100 && !isAttacking) {
            shotAvailableCounter++;
        }
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= ANIMATION_FRAME_TIME) {
            if (animationFrames == upIdle || animationFrames == downIdle || animationFrames == leftIdle || animationFrames == rightIdle) {
                animationIndex_idle = (animationIndex_idle + 1) % animationFrames.length;
            } else if (animationFrames == upRun || animationFrames == downRun || animationFrames == leftRun || animationFrames == rightRun) {
                animationIndex_run = (animationIndex_run + 1) % animationFrames.length;
            }
            lastTime = currentTime;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage currentFrame;

        if (isAttacking) {
            int frameIndex = (attackCounter / 10) % attackUp.length;

            currentFrame = switch (direction) {
                case "up" -> attackUp[frameIndex];
                case "down" -> attackDown[frameIndex];
                case "left" -> attackLeft[frameIndex];
                case "right" -> attackRight[frameIndex];
                default -> attackDown[0];
            };

            if (frameIndex == attackUp.length - 1 && attackCounter % 10 == 0) {
                isAttacking = false;
                projectile.set(worldX, worldY, direction, true, this);
                gp.projectileList.add(projectile);
            }

            attackCounter++;
        } else if (animationFrames == upIdle || animationFrames == downIdle || animationFrames == leftIdle || animationFrames == rightIdle) {
            currentFrame = animationFrames[animationIndex_idle];
        } else {
            currentFrame = animationFrames[animationIndex_run];
        }

        double screenX = worldX - gp.player.worldX + gp.player.screenX;
        double screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (gp.player.screenX >= gp.player.worldX) screenX = worldX;
        if (gp.player.screenY >= gp.player.worldY) screenY = worldY;
        double rightOffset = gp.screenWidth - gp.player.screenX;
        if (rightOffset >= gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        double bottomOffset = gp.screenHeight - gp.player.screenY;
        if (bottomOffset >= gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - worldY);
        }

        if (invisible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        if (isDead) {
            deadAnimation_1(g2);
            if (isRunning)
                g2.drawImage(currentFrame, (int) screenX, (int) screenY, size + 2, size + 2, null);
            else
                g2.drawImage(currentFrame, (int) screenX, (int) screenY, size, size, null);

        } else {
            if (isRunning && !isAttacking)
                g2.drawImage(currentFrame, (int) screenX, (int) screenY, size + 2, size + 2, null);
            else if (isAttacking)
                g2.drawImage(currentFrame, (int) screenX - 10, (int) screenY, size + 50, size + 2, null);
            else
                g2.drawImage(currentFrame, (int) screenX, (int) screenY, size, size, null);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}

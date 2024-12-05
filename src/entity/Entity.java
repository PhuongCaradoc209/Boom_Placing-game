package entity;

import buff.Buff;
import buff.BuffManager;
import enemy.Ene_Lizard;
import main.GamePanel;
import UI.UtilityTool;
import object.Boom;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Entity {
    public GamePanel gp;
    protected int size;
    public double worldX, worldY;
    private int col, row;
    public double speed;

    protected BufferedImage
            up1, up2, up3, up4,
            down1, down2, down3, down4,
            right1, right2, right3, right4,
            left1, left2, left3, left4,
            standRight1, standRight2, standRight3, standRight4,
            standLeft1, standLeft2, standLeft3, standLeft4;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea;// by this, you can manage which part of tile can be collision
    public boolean collisionOn = false;
    public boolean collision = false;
    public int actionLookCounter = 0;

    //CHARACTER STATUS
    private int maxLife;
    private int life;
    public boolean alive = true;
    protected boolean isDead = false;

    //OBJ
    public String name;
    public BufferedImage image, image1;

    //BOOM
    private boolean outOfBoomCoordinate = false;
    public List<Boom> ownBooms;
    private int boomAmount;
    private boolean placedBoom = false;
    private int boomExplosionRadius;

    //DEATH ANIMATION
    protected boolean hasTwoAnimationDeath;
    protected BufferedImage[] deathAnimationFrames;
    protected int currentDeathFrame;
    protected int deathAnimationSpeed;
    protected int deathFrameCounter;
    protected boolean deathAnimationComplete;
    private boolean deathAnimation_1_completed = false;

    //COUNTER
    public boolean invisible = false;
    protected int invisibleCounter = 0;
    private int dyingCounter = 0;
    protected int shotAvailableCounter = 0;

    //BUFF
    public BuffManager ownBuffManager;

    //WEAPON
    protected Projectile projectile;

    //PATH
    protected boolean onPath = false;

    public Entity(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        ownBooms = new ArrayList<>();
        boomExplosionRadius = 1;
        hasTwoAnimationDeath = false;
        deathAnimationComplete = false;

        ownBuffManager = new BuffManager(gp);
    }

    public void setAction() {
    }

    public void checkCollision() {
        collisionOn = false;
        if (gp.currentMap == 0) {
            gp.cChecker.checkPlayer(this);
        }
        gp.cChecker.checkTile(this);
        gp.cChecker.checkAtEdge(this);
        gp.cChecker.checkEntity(this, gp.aSetter.getObjectMap(gp.currentMap));
        gp.cChecker.checkBoom(this, gp.boomManager.booms);
        getBuff(gp.cChecker.checkCollectBuff(this));
    }

    public void update() {
//        checkPlaceBomb();

        if (deathAnimation_1_completed && hasTwoAnimationDeath) updateDeathAnimation_2();
        else {
            setAction();
            getEntityCoordinates(this);

            checkCollision();

            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1)
                    spriteNum = 2;
                else if (spriteNum == 2)
                    spriteNum = 3;
                else if (spriteNum == 3)
                    spriteNum = 4;
                else if (spriteNum == 4)
                    spriteNum = 1;
                spriteCounter = 0;
            }
            if (invisible) {
                invisibleCounter++;
                if (invisibleCounter > 60) {
                    invisible = false;
                    invisibleCounter = 0;
                }
            }
        }
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
                if (spriteNum == 3) {
                    image = up3;
                }
                if (spriteNum == 4) {
                    image = up4;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down3;
                }
                if (spriteNum == 4) {
                    image = down4;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                if (spriteNum == 4) {
                    image = left4;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                if (spriteNum == 4) {
                    image = right4;
                }
                break;
        }
//        if (isDead && !deathAnimationComplete && canDeath) {
//            g2.drawImage(deathAnimationFrames[currentDeathFrame], (int) screenX, (int) screenY, size, size, null);
//        }else{
//
//        }

        if (invisible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        if (isDead) {
            deadAnimation_1(g2);
            g2.drawImage(image, (int) screenX, (int) screenY, size, size, null);
        } else if (deathAnimation_1_completed && hasTwoAnimationDeath) {
            g2.drawImage(deathAnimationFrames[currentDeathFrame], (int) screenX, (int) screenY, size, size, null);
        } else {
            g2.drawImage(image, (int) screenX, (int) screenY, size, size, null);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO
                    .read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void placeBoom() {
        if (ownBooms.size() <= getBoomAmount()) {
            Boom boom = new Boom(col, row, this, gp);
            placedBoom = true;
            boom.collision = false;
            gp.boomManager.booms.add(boom);
            ownBooms.add(boom);
            gp.keyHandler.spacePressed = false;
        }
    }

    protected void deadAnimation_1(Graphics2D g2) {
        dyingCounter++;
        int i = 5;
        if (dyingCounter <= i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > i && dyingCounter <= 2 * i) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 2 * i && dyingCounter <= 3 * i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 3 * i && dyingCounter <= 4 * i) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 4 * i && dyingCounter <= 5 * i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 5 * i && dyingCounter <= 6 * i) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 6 * i && dyingCounter <= 7 * i) {
            changeAlpha(g2, 0f);
        }
        if (dyingCounter > 7 * i && dyingCounter <= 8 * i) {
            changeAlpha(g2, 1f);
        }
        if (dyingCounter > 8 * i) {
            deathAnimation_1_completed = true;
            isDead = false;
            alive = false;
            if (!hasTwoAnimationDeath) deathAnimationComplete = true;
        }
    }

    private void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    protected void loadDeathAnimationFrames(String path, int frames) {
        for (int i = 0; i < frames; i++) {
            deathAnimationFrames[i] = setup(path + (i + 1), gp.tileSize, gp.tileSize);
        }
    }

    private void updateDeathAnimation_2() {
        if (!deathAnimationComplete) {
            deathFrameCounter++;

            if (deathFrameCounter >= deathAnimationSpeed) {
                currentDeathFrame++;
                deathFrameCounter = 0;

                if (currentDeathFrame >= deathAnimationFrames.length) {
                    deathAnimationComplete = true;
                }
            }
        }
    }

    public void getBuff(Buff buff) {
        if (buff != null) {
            for (int i = 0; i < gp.buffManagerGame.buffs.size(); i++) {
                if (gp.buffManagerGame.buffs.get(i).equals(buff)) {
                    ownBuffManager.addBuff(gp.buffManagerGame.buffs.get(i), this);
                    gp.buffManagerGame.buffs.remove(i);
                    return;
                }
            }
        }
    }

    public void checkPlaceBomb() {
        for (String key : gp.aSetter.getObjectMap(gp.currentMap).keySet()) {
            String[] coordinates = key.split(",");
            int tileCol = Integer.parseInt(coordinates[0]);
            int tileRow = Integer.parseInt(coordinates[1]);

            if (Math.abs(col - tileCol) < 2 && Math.abs(row - tileRow) < 2) {
                placeBoom();
                return;
            }
        }
    }

    public void searchPath(int goalRow, int goalCol) {
        int startCol = (int) (worldX + solidArea.x) / gp.tileSize;
        int startRow = (int) (worldY + solidArea.y) / gp.tileSize;

        gp.pathFinder.setNode(startRow, startCol, goalRow, goalCol);

        if (gp.pathFinder.search()) {
            //NEXT WORLD X & Y
            int nextX = gp.pathFinder.pathList.getFirst().col * gp.tileSize;
            int nextY = gp.pathFinder.pathList.getFirst().row * gp.tileSize;

            double enLeftX = worldX + solidArea.x;
            double enRightX = worldX + solidArea.x + solidArea.width;
            double enTopY = worldY + solidArea.y;
            double enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                //left or right
                if (enLeftX > nextX) direction = "left";
                else if (enLeftX < nextX) direction = "right";
            } else if (enTopY > nextY && enLeftX > nextX) {
                //up or left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }
        }
    }

    public void fire(){
        int i = new Random().nextInt(100) + 1;
        if (i > 99 && !projectile.alive && shotAvailableCounter == 100) {
            projectile.set(worldX, worldY, direction, true, this);

            gp.projectileList.add(projectile);

            shotAvailableCounter = 0;
        }

        if (shotAvailableCounter < 100){
            shotAvailableCounter++;
        }
    }

    public void getEntityCoordinates(Entity entity) {
        col = (int) ((worldX + gp.tileSize / 2) / gp.tileSize);
        row = (int) ((worldY + gp.tileSize / 2) / gp.tileSize);
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public double getWorldX() {
        return worldX;
    }

    public void setWorldX(double worldX) {
        this.worldX = worldX;
    }

    public double getWorldY() {
        return worldY;
    }

    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }

    public boolean isOutOfBoomCoordinate() {
        return outOfBoomCoordinate;
    }

    public void setOutOfBoomCoordinate(boolean outOfBoomCoordinate) {
        this.outOfBoomCoordinate = outOfBoomCoordinate;
    }

    public int getBoomAmount() {
        return boomAmount;
    }

    public void setBoomAmount(int boomAmount) {
        this.boomAmount = boomAmount;
    }

    public boolean isPlacedBoom() {
        return placedBoom;
    }

    public void setPlacedBoom(boolean placedBoom) {
        this.placedBoom = placedBoom;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getBoomExplosionRadius() {
        return boomExplosionRadius;
    }

    public void setBoomExplosionRadius(int boomExplosionRadius) {
        this.boomExplosionRadius = boomExplosionRadius;
    }
}
package entity;

import main.GamePanel;
import main.UtilityTool;
import object.Boom;
import object.BoomManager;

import javax.imageio.ImageIO;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Entity {
    public GamePanel gp;
    protected int size;
    public double worldX, worldY;
    private int col, row;
    public double speed;

    public BufferedImage
            up1, up2, up3, up4,
            down1, down2, down3, down4,
            right1, right2 , right3, right4,
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

    //OBJ
    public String name;
    public BufferedImage image, image1;

    //BOOM
    public BoomManager boomManager;

    public Entity(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        boomManager = new BoomManager(gp, this);
    }

    public void setAction() {
    }

    public void update() {
        setAction();
        getEntityCoordinates(this);

        if (gp.currentMap == 0) {
            gp.cChecker.checkPlayer(this);
        }
        gp.cChecker.checkTile(this);
        gp.cChecker.checkAtEdge(this);

        // IF COLLISION IS FALSE, PLAYER CAN MOVE
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
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        // IF PLAYER AT THE EDGE
//        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
//                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
//                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
//                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
//            g2.drawImage(image, (int) screenX, (int) screenY, size, size, null);
//        } else if (gp.player.screenX > gp.player.worldX ||
//                gp.player.screenY > gp.player.worldY ||
//                rightOffSet > gp.worldWidth - gp.player.worldX ||
//                bottomOffSet > gp.worldHeight - gp.player.worldY) {
        g2.drawImage(image, (int) screenX, (int) screenY, size, size, null);
//        }
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
        getEntityCoordinates(this);
        boomManager.booms.add(new Boom(col, row, 70, gp));
        gp.keyHandler.spacePressed = false;
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
}
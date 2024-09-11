package entity;

import main.GamePanel;
//import main.KeyHandler;
//import object.Fishing_Rod;
//import object.OBJ_FishingRod1;
import main.KeyHandler;
import tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {
    KeyHandler key;
    TileManager tileM;

    public double screenX;
    public double screenY;
    public double temp_worldX;
    public double temp_worldY;
    public int interactEntity_Index;
    public ArrayList<Entity> interactEntity;
    public Entity currentFishingRod;
    private double x, y;
    private int npcIndex, animalIndex, iTileIndex, objIndex;

    public Player(GamePanel gp, KeyHandler key, TileManager tileM) {
        super(gp);
        this.key = key;
        this.tileM = tileM;
        size = gp.tileSize;

        setDefaultValues();

        screenX = (double) gp.screenWidth / 2 - ((double) gp.tileSize / 2); // set the player at then center of the
        // screen
        screenY = (double) gp.screenHeight / 2 - ((double) gp.tileSize / 2);

        interactEntity = new ArrayList<>();

        // AREA COLLISION
        solidArea = new Rectangle();
        solidArea.x = (8 * gp.tileSize) / 48;
        solidArea.y = (16 * gp.tileSize) / 48;
        solidArea.width = (32 * gp.tileSize) / 48;
        solidArea.height = (32 * gp.tileSize) / 48;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        direction = "down";
        setPlayerImage();
    }

    public void setPlayerImage() {
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * 2;
        speed = (double) gp.worldWidth / 800;
        direction = "standDown";
    }

    public void getPlayerImage() {
        down1 = right1 = setup("player/right_1", 32, 32);
        down2 = right2 = setup("player/right_2", 32, 32);
        down3 = right3 = setup("player/right_3", 32, 32);
        down4 = right4 = setup("player/right_4", 32, 32);

        up1 = left1 = setup("player/left_1", 32, 32);
        up2 = left2 = setup("player/left_2", 32, 32);
        up3 = left3 = setup("player/left_3", 32, 32);
        up4 = left4 = setup("player/left_4", 32, 32);

        standDown1 = setup("player/standDown_1", 32, 32);
        standDown2 = setup("player/standDown_2", 32, 32);
        standDown3 = setup("player/standDown_3", 32, 32);
        standDown4 = setup("player/standDown_4", 32, 32);

        standUp1 = setup("player/standDown_1", 32, 32);
        standUp2 = setup("player/standDown_2", 32, 32);
        standUp3 = setup("player/standDown_3", 32, 32);
        standUp4 = setup("player/standDown_4", 32, 32);
    }

    public void update() {
        if (key.downPressed || key.upPressed || key.leftPressed || key.rightPressed) {
            if (key.upPressed) {
                direction = "up";
            } else if (key.downPressed) {
                direction = "down";
            } else if (key.leftPressed) {
                direction = "left";
            } else {
                direction = "right";
            }
            // SET SOUND
//            setTileSound(tileM);
        } else {
            if (Objects.equals(direction, "up")) {
                direction = "standUp";
            } else if (Objects.equals(direction, "down")) {
                direction = "standDown";
            } else if (Objects.equals(direction, "right")) {
                direction = "standRight";
            } else if (Objects.equals(direction, "left")) {
                direction = "standLeft";
            }
            // STOP SOUND
//            gp.stopMusic("grass");
        }

        // UPDATE the solidArea due to zoom in and out
        solidArea.x = (10 * gp.tileSize) / 48;
        solidArea.y = (20 * gp.tileSize) / 48;
        solidArea.width = (30 * gp.tileSize) / 48;
        solidArea.height = (35 * gp.tileSize) / 48;

        // CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);
        // CHECK IF AT EDGE
        gp.cChecker.checkAtEdge(this);

//         IF COLLISION IS FALSE, PLAYER CAN MOVE
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
        if (spriteCounter > 24) {
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

//    private void setTileSound(TileManager tileM) {
//        // getPLayerCol and Row at the center point of player
//        int playerCol = (int) ((worldX + gp.tileSize / 2) / gp.tileSize);
//        int playerRow = (int) ((worldY + gp.tileSize / 2) / gp.tileSize);
//        int tileIndex = tileM.mapTileNum[gp.currentMap][playerRow][playerCol];
//
//        if (tileIndex > 0 && tileIndex < 27 && tileIndex != 16 && tileIndex != 17 && tileIndex != 18) {
//            gp.playMusic("grass", 1);
//        } else {
//            gp.stopMusic("grass");
//        }
//    }

//    public void hitInteractiveTile(int i) {
//        if (i != 999 && !gp.iTile[0].get(i).isOpen) {
//            gp.playSoundEffect("Door open", 4);
//            gp.iTile[0].add(gp.iTile[0].get(i).getInteractedForm());
//            gp.iTile[0].remove(i);
//        }
//        if (i == 999) {
//            if (!gp.iTile[0].contains(gp.iTile[0].get(0).getInteractedForm())
//                    && gp.iTile[0].get(0).name.equals("Door Open")) {
//                gp.playSoundEffect("Door close", 5);
//                gp.iTile[0].add(gp.iTile[0].get(0).getInteractedForm());
//                gp.iTile[0].remove(0);
//            }
//        }
//    }

    public void draw(Graphics2D g) {
        // g.setColor(Color.white);
        // g.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
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
            case "standUp":
            case "standLeft":
                if (spriteNum == 1) {
                    image = standUp1;
                }
                if (spriteNum == 2) {
                    image = standUp2;
                }
                if (spriteNum == 3) {
                    image = standUp3;
                }
                if (spriteNum == 4) {
                    image = standUp4;
                }
                break;
            case "standDown":
            case "standRight":
                if (spriteNum == 1) {
                    image = standDown1;
                }
                if (spriteNum == 2) {
                    image = standDown2;
                }
                if (spriteNum == 3) {
                    image = standDown3;
                }
                if (spriteNum == 4) {
                    image = standDown4;
                }
                break;
        }
        // STOP MOVING THE CAMERA AT EDGE (PLAYER CAN NOT MOVE IF AT EDGE)
        x = screenX;
        y = screenY;
        // TOP
        if (gp.player.screenX >= worldX) {
            x = worldX;
        }
        // LEFT
        if (gp.player.screenY >= worldY) {
            y = worldY;
        }
        // RIGHT
        double rightOffSet = gp.screenWidth - screenX;
        if (rightOffSet >= gp.worldWidth - worldX) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }
        // BOTTOM
        double bottomOffSet = gp.screenHeight - screenY;
        if (bottomOffSet >= gp.worldHeight - worldY) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }
        g.drawImage(image, (int) x, (int) y, size, size, null);
    }

}

package entity;

import main.GamePanel;
//import main.KeyHandler;
//import object.Fishing_Rod;
//import object.OBJ_FishingRod1;
import main.KeyHandler;
import object.Bullet_Slime;
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
    public ArrayList<Entity> interactEntity;
    private double x, y;
    private int playerCol, playerRow;
    private int npcIndex, animalIndex, objIndex;
    private int[] iTileCoordinate;

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
        solidArea.x = (10 * gp.tileSize) / 48;
        solidArea.y = (20 * gp.tileSize) / 48;
        solidArea.width = (28 * gp.tileSize) / 48;
        solidArea.height = (30 * gp.tileSize) / 48;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        direction = "down";
        setPlayerImage();

        //SET UP BOOM
        setBoomAmount(1);
    }

    public void setPlayerImage() {
        getPlayerImage();
    }

    public void setDefaultValues() {
        speed = 1.5;
        direction = "standDown";

        //PLAYER STATUS
        setMaxLife(3);
        setLife(getMaxLife());
        setDead(false);
        invisible = false;

        //CLEAR BUFF
        ownBuffManager.buffs.clear();
        setBoomAmount(1);
    }

    public void setCoordinate(double x, double y) {
        worldX = gp.tileSize * x;
        worldY = gp.tileSize * y;
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

        standLeft1 = setup("player/standLeft_1", 32, 32);
        standLeft2 = setup("player/standLeft_2", 32, 32);
        standLeft3 = setup("player/standLeft_3", 32, 32);
        standLeft4 = setup("player/standLeft_4", 32, 32);

        standRight1 = setup("player/standRight_1", 32, 32);
        standRight2 = setup("player/standRight_2", 32, 32);
        standRight3 = setup("player/standRight_3", 32, 32);
        standRight4 = setup("player/standRight_4", 32, 32);
    }

    public void update() {;
        getEntityCoordinates(this);

        if (key.spacePressed) {
            if (ownBooms.size() < getBoomAmount()) {
                placeBoom();
            }
        }

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
                direction = "standLeft";
            } else if (Objects.equals(direction, "down")) {
                direction = "standRight";
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
        solidArea.width = (28 * gp.tileSize) / 48;
        solidArea.height = (30 * gp.tileSize) / 48;

        // CHECK TILE COLLISION
//        collisionOn = false;
//        gp.cChecker.checkTile(this);
//        //CHECK BOOM
//        if (isOutOfBoomCoordinate()){
//            gp.cChecker.checkBoom(this, gp.boomManager.booms);
//        }
//        // CHECK INTERACT TILE COLLISION
//        iTileCoordinate = gp.cChecker.checkEntity(this, gp.aSetter.getObjectMap(gp.currentMap));
//        gp.cChecker.checkEntity(this, gp.enemy);
//        // CHECK IF AT EDGE
//        gp.cChecker.checkAtEdge(this);
//        //CHECK EVENT
//        gp.eHandler.checkEvent();

        getBuff( gp.cChecker.checkCollectBuff(this));

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

        if (invisible){
            invisibleCounter++;
            if (invisibleCounter > 60){
                invisible = false;
                invisibleCounter = 0;
            }
        }
        if (isDead()) gp.gameState = gp.gameOverState;
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
            case "standLeft":
                if (spriteNum == 1) {
                    image = standLeft1;
                }
                if (spriteNum == 2) {
                    image = standLeft2;
                }
                if (spriteNum == 3) {
                    image = standLeft3;
                }
                if (spriteNum == 4) {
                    image = standLeft4;
                }
                break;
            case "standRight":
                if (spriteNum == 1) {
                    image = standRight1;
                }
                if (spriteNum == 2) {
                    image = standRight2;
                }
                if (spriteNum == 3) {
                    image = standRight3;
                }
                if (spriteNum == 4) {
                    image = standRight4;
                }
                break;
        }

        if (invisible)
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

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

        //reset
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}

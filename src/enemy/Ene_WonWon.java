package enemy;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class Ene_WonWon extends Entity {

    public Ene_WonWon(GamePanel gp) {
        super(gp);

        name = "wonwon";
        speed = 0.5;
        direction = "down";
        size = gp.tileSize;
        collisionOn = false;

        setMaxLife(2);
        setLife(getMaxLife());

        solidArea.x = (10 * gp.tileSize) / 48;
        solidArea.y = (20 * gp.tileSize) / 48;
        solidArea.width = (28 * gp.tileSize) / 48;
        solidArea.height = (30 * gp.tileSize) / 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setBoomAmount(1);
        getImage();
        canDeath = false;
    }

    public void getImage() {
        up1 = setup("enemy/wonwon/wonwon-up-1", gp.tileSize, gp.tileSize);
        up2 = setup("enemy/wonwon/wonwon-up-2", gp.tileSize, gp.tileSize);
        up3 = setup("enemy/wonwon/wonwon-up-3", gp.tileSize, gp.tileSize);
        up4 = setup("enemy/wonwon/wonwon-up-4", gp.tileSize, gp.tileSize);

        right1 = setup("enemy/wonwon/wonwon-right-1", gp.tileSize, gp.tileSize);
        right2 = setup("enemy/wonwon/wonwon-right-2", gp.tileSize, gp.tileSize);
        right3 = setup("enemy/wonwon/wonwon-right-3", gp.tileSize, gp.tileSize);
        right4 = setup("enemy/wonwon/wonwon-right-4", gp.tileSize, gp.tileSize);

        down1 = setup("enemy/wonwon/wonwon-down-1", gp.tileSize, gp.tileSize);
        down2 = setup("enemy/wonwon/wonwon-down-2", gp.tileSize, gp.tileSize);
        down3 = setup("enemy/wonwon/wonwon-down-3", gp.tileSize, gp.tileSize);
        down4 = setup("enemy/wonwon/wonwon-down-4", gp.tileSize, gp.tileSize);

        left1 = setup("enemy/wonwon/wonwon-left-1", gp.tileSize, gp.tileSize);
        left2 = setup("enemy/wonwon/wonwon-left-2", gp.tileSize, gp.tileSize);
        left3 = setup("enemy/wonwon/wonwon-left-3", gp.tileSize, gp.tileSize);
        left4 = setup("enemy/wonwon/wonwon-left-4", gp.tileSize, gp.tileSize);
    }

    public void setAction() {
        actionLookCounter++;
        if (actionLookCounter == 100) {
            Random random = new Random();
            int i = random.nextInt(200) + 1;
            if (i <= 50) {
                direction = "down";
            } else if (i <= 100) {
                direction = "left";
            } else if (i <= 150) {
                direction = "right";
            } else {
                direction = "up";
            }
            actionLookCounter = 0;
        }
    }
}

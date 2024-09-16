package enemy;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class Ene_Slime extends Entity {

    public Ene_Slime(GamePanel gp) {
        super(gp);

        name = "slime";
        speed = 0.5;
        direction = "down";
        size = gp.tileSize;
        collisionOn = false;

        setMaxLife(1);
        setLife(getMaxLife());

        solidArea.x = (10 * gp.tileSize) / 48;
        solidArea.y = (20 * gp.tileSize) / 48;
        solidArea.width = (28 * gp.tileSize) / 48;
        solidArea.height = (30 * gp.tileSize) / 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = right1 = setup("enemy/slime/slime-right-0", gp.tileSize, gp.tileSize);
        up2 = right2 = setup("enemy/slime/slime-right-1", gp.tileSize, gp.tileSize);
        up3 = right3 = setup("enemy/slime/slime-right-2", gp.tileSize, gp.tileSize);
        up4 = right4 = setup("enemy/slime/slime-right-3", gp.tileSize, gp.tileSize);

        down1 = left1 = setup("enemy/slime/slime-left-0", gp.tileSize, gp.tileSize);
        down2 = left2 = setup("enemy/slime/slime-left-1", gp.tileSize, gp.tileSize);
        down3 = left3 = setup("enemy/slime/slime-left-2", gp.tileSize, gp.tileSize);
        down4 = left4 = setup("enemy/slime/slime-left-3", gp.tileSize, gp.tileSize);
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

package enemy;

import entity.Entity;
import main.GamePanel;
import object.Bullet_Slime;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Ene_Slime extends Entity {
    private int i;
    int goalCol, goalRow;

    public Ene_Slime(GamePanel gp) {
        super(gp);

        name = "slime";
        speed = 0.8;
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

        deathAnimationFrames = new BufferedImage[5];
        loadDeathAnimationFrames("enemy/slime/slime-die-", 5);
        currentDeathFrame = 0;
        deathAnimationSpeed = 10;
        deathFrameCounter = 0;
        hasTwoAnimationDeath = true;

        projectile = new Bullet_Slime(gp);
        setBoomAmount(1);
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

    public void update(){
        super.update();

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

    public void setAction() {
        if (onPath){
            fire();
            int goalCol = (int) ((gp.player.worldX + gp.player.solidArea.x)/gp.tileSize);
            int goalRow = (int) ((gp.player.worldY + gp.player.solidArea.y)/gp.tileSize);
            searchPath(goalRow,goalCol);

            int i = new Random().nextInt(200);
            if (i>197 && !projectile.alive && shotAvailableCounter == 30){
                projectile.set(worldX, worldY, direction,true,this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }
        }
        else {
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
}

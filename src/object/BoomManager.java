package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoomManager {
    private GamePanel gp;
    private Entity entity;
    private ExplosionManager explosionManager;

    public List<Boom> booms;
    private int boomAmount;

    public BoomManager(GamePanel gp, Entity entity) {
        this.gp = gp;
        this.entity = entity;
        this.booms = new ArrayList<>();
        this.explosionManager = new ExplosionManager(gp, entity);
    }

    public void update() {
        if (gp.keyHandler.spacePressed) {
            if (booms.size() < boomAmount) {
                entity.placeBoom();
            }
        }
        for (int i = 0; i < booms.size(); i++) {
            booms.get(i).update();
            checkEntityOutOfBoom(booms.get(i));
            if (booms.get(i).isExplode()) {
                explosionManager.checkDestructibleTiles(booms.get(i));
                booms.remove(i);
                entity.setOutOfBoomCoordinate(false);
                i--;
            }
        }
    }

    public void draw(Graphics2D g2) {
        //DRAW BOOMS
        for (Boom boom : booms) {
            boom.draw(g2);
        }
    }

    public void setBoomAmount(int boomAmount) {
        this.boomAmount = boomAmount;
    }

    public void checkEntityOutOfBoom(Boom boom) {
        //get the entity's solid area position within the game world
        entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
        entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);

        //get the npc's solid area position within the game world
        boom.solidArea.x = (int) (boom.getWorldX() + boom.solidArea.x);
        boom.solidArea.y = (int) (boom.getWorldY() + boom.solidArea.y);


        switch (entity.direction) {
            //SIMULATING ENTITY'S MOVEMENT AND CHECK WHERE IT WILL BE AFTER IT MOVED
            case "up":
                entity.solidArea.y -= (int) entity.speed + 5;
                break;

            case "down":
                entity.solidArea.y += (int) entity.speed + 5;
                break;

            case "right":
                entity.solidArea.x += (int) entity.speed + 5;
                break;

            case "left":
                entity.solidArea.x -= (int) entity.speed + 5;
                break;
        }
        if (!entity.solidArea.intersects(boom.solidArea)) {
            {
                entity.setOutOfBoomCoordinate(true);
                boom.collision = true;
            }
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        boom.solidArea.x = boom.solidAreaDefaultX;
        boom.solidArea.y = boom.solidAreaDefaultY;
    }
}

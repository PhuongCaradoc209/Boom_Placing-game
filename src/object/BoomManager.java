package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoomManager {
    private GamePanel gp;
    private ExplosionManager explosionManager;

    public List<Boom> booms;
    private Entity entity;

    public BoomManager(GamePanel gp) {
        this.gp = gp;
        this.booms = new ArrayList<>();
        this.explosionManager = new ExplosionManager(gp);
    }

    public void update() {
        for (int i = 0; i < booms.size(); i++) {
            booms.get(i).update();
            checkAllEntitiesOutOfBoom(booms.get(i));

            entity = getEntityPlacedBoom(booms.get(i));
            if (entity != null) {
                if (booms.get(i).isExplode()) {
                    entity.ownBooms.remove(booms.get(i));
                    if (entity.ownBooms.isEmpty())
                        entity.setPlacedBoom(false);
                }
            }

            if (booms.get(i).isExplode()) {
                explosionManager.checkDestructibleTiles(booms.get(i));
                explosionManager.hitEntities(booms.get(i));
                booms.remove(i);
                i--;
            }
        }
    }

    public Entity getEntityPlacedBoom(Boom boom) {
        for (Entity entity : gp.entityManager.getEntities(gp.currentMap)) {
            if (entity.isPlacedBoom()) {
                List<Boom> entityBooms = entity.ownBooms; // Assuming the entity has a list of bombs it placed
                if (entityBooms.contains(boom)) {
                    return entity;
                }
            }
        }
        return null;
    }

    public void draw(Graphics2D g2) {
        // DRAW BOOMS
        for (Boom boom : booms) {
            boom.draw(g2);
        }
    }

    public void checkAllEntitiesOutOfBoom(Boom boom) {
        for (Entity entity : gp.entityManager.getEntities(gp.currentMap)) {
            entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
            entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);
            boom.solidArea.x = (int) (boom.getWorldX() + boom.solidArea.x);
            boom.solidArea.y = (int) (boom.getWorldY() + boom.solidArea.y);

            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= (int) entity.speed;
                    break;
                case "down":
                    entity.solidArea.y += (int) entity.speed;
                    break;
                case "right":
                    entity.solidArea.x += (int) entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= (int) entity.speed;
                    break;
            }

            //check the entity that place boom is out of that boom or not
            if (!entity.solidArea.intersects(boom.solidArea) && entity.isPlacedBoom()) {
                entity.setOutOfBoomCoordinate(true);
                boom.collision = true;
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            boom.solidArea.x = boom.solidAreaDefaultX;
            boom.solidArea.y = boom.solidAreaDefaultY;
        }
    }
}

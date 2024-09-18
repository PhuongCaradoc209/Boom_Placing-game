package entity;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    GamePanel gp;
    private List<Entity> entities;

    public EntityManager(GamePanel gp) {
        this.gp = gp;
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void updateEntities() {
        for (Entity entity : entities) {
            entity.update();
            checkEntityDied(entity);
        }
    }

    public void checkEntityDied(Entity entity) {
        if (entity.deathAnimationComplete){
            removeEntity(entity);
            gp.enemy[gp.currentMap].remove(entity);
        }
    }
}
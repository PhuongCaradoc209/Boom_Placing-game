package entity;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    GamePanel gp;
    private List<Entity>[] entities;

    public EntityManager(GamePanel gp) {
        this.gp = gp;
        this.entities = new ArrayList[gp.maxMap];
        for (int i = 0; i < gp.maxMap; i++) {
            entities[i] = new ArrayList<>();
        }
    }

    public void addEntity(Entity entity, int currentMap) {
        entities[currentMap].add(entity);
    }

    public void removeEntity(Entity entity, int currentMap) {
        entities[currentMap].remove(entity);
    }

    public List<Entity> getEntities(int currentMap) {
        return entities[currentMap];
    }

    public void updateEntities() {
        for (int i = 0; i < entities[gp.currentMap].size(); i++) {
            entities[gp.currentMap].get(i).update();
            checkEntityDied(entities[gp.currentMap].get(i));
        }
    }

    public void checkEntityDied(Entity entity) {
        if (entity.deathAnimationComplete){
            removeEntity(entity, gp.currentMap);
            gp.enemy[gp.currentMap].remove(entity);
        }
    }
}
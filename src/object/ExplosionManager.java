package object;

import entity.Entity;
import main.GamePanel;

public class ExplosionManager {
    private GamePanel gp;

    private String key;
    private int hardness;

    public ExplosionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkDestructibleTiles(Boom boom) {
        for (int[] coord : boom.explosionArea.get("Up")) {
            check(coord[0], coord[1]);
        }
        for (int[] coord : boom.explosionArea.get("Down")) {
            check(coord[0], coord[1]);

            System.out.println(coord[0] + "," +coord[1]);

        }
        for (int[] coord : boom.explosionArea.get("Right")) {
            check(coord[0], coord[1]);
        }
        for (int[] coord : boom.explosionArea.get("Left")) {
            check(coord[0], coord[1]);
        }
    }

    public void hitEntities(Boom boom) {
        for (int i = 0; i < gp.entityManager.getEntities().size(); i++) { // Loop through all entities in the game
            if (isEntityInExplosionRange(boom, gp.entityManager.getEntities().get(i))) {
                gp.eHandler.damagePit(gp.entityManager.getEntities().get(i)); // Handle entity damage
            }
        }
    }

    private void check(int row, int column) {
        if (gp.aSetter.hasObjectAt(column, row, gp.currentMap)) {
            key = column + "," + row;
            hardness = gp.aSetter.getObjectMap(gp.currentMap).get(key).getHardness();
            if (hardness == 1)
                gp.aSetter.removeObject(column, row, gp.currentMap);
            else {
                gp.aSetter.getObjectMap(gp.currentMap).get(key).spriteNum++;
                gp.aSetter.getObjectMap(gp.currentMap).get(key).setHardness(--hardness);
            }
        }
    }

    private boolean isEntityInExplosionRange(Boom boom, Entity entity) {
        int entityCol = entity.getCol();
        int entityRow = entity.getRow();

        int boomCol = boom.getCol();
        int boomRow = boom.getRow();

        int explosionRadius = boom.getRadiusExplosion();

        // Check if the entity is within the explosion range (both horizontal and vertical directions)
        if (Math.abs(entityCol - boomCol) <= explosionRadius && entityRow == boomRow) {
            return true;
        }
        if (Math.abs(entityRow - boomRow) <= explosionRadius && entityCol == boomCol) {
            return true;
        }

        return false;
    }
}

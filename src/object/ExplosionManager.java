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
        for (int i = 1; i <= boom.getRadiusExplosion(); i++) {
            // Check upward direction
            check(boom.getCol(), boom.getRow() - i);

            // Check downward direction
            check(boom.getCol(), boom.getRow() + i);

            // Check left direction
            check(boom.getCol() - i, boom.getRow());

            // Check right direction
            check(boom.getCol() + i, boom.getRow());
        }
    }

    public void hitEntities(Boom boom) {
        for (int i=0; i<gp.entityManager.getEntities().size();i++) { // Loop through all entities in the game
            if (isEntityInExplosionRange(boom, gp.entityManager.getEntities().get(i))) {
                gp.eHandler.damagePit(gp.entityManager.getEntities().get(i)); // Handle entity damage
            }
        }
    }

    private void check(int x, int y) {
        if (gp.aSetter.hasObjectAt(x, y, gp.currentMap)) {
            key = x + "," + y;
            hardness = gp.aSetter.getObjectMap(gp.currentMap).get(key).getHardness();
            if (hardness == 1)
                gp.aSetter.removeObject(x, y, gp.currentMap);
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

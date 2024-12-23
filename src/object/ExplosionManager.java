package object;

import entity.Entity;
import main.GamePanel;

import java.util.Objects;

public class ExplosionManager {
    private GamePanel gp;

    private String key;
    private int hardness;

    public ExplosionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkDestructibleTiles(Boom boom) {
        for (int[] coord : boom.explosionArea.get("Up")) {
            checkInteractTileAt(coord[0], coord[1]);
        }
        for (int[] coord : boom.explosionArea.get("Down")) {
            checkInteractTileAt(coord[0], coord[1]);
        }
        for (int[] coord : boom.explosionArea.get("Right")) {
            checkInteractTileAt(coord[0], coord[1]);
        }
        for (int[] coord : boom.explosionArea.get("Left")) {
            checkInteractTileAt(coord[0], coord[1]);
        }
    }

    public void hitEntities(Boom boom) {
        for (int i = 0; i < gp.entityManager.getEntities(gp.currentMap).size(); i++) {
            if (isEntityInExplosionRange(boom, gp.entityManager.getEntities(gp.currentMap).get(i))) {
                gp.eHandler.damagePit(gp.entityManager.getEntities(gp.currentMap).get(i));
            }
        }
    }

    private void checkInteractTileAt(int row, int column) {
        if (gp.aSetter.hasObjectAt(column, row, gp.currentMap)) {
            key = column + "," + row;
            hardness = gp.aSetter.getObjectMap(gp.currentMap).get(key).getHardness();
            if (hardness == 1)
            {
                if (Objects.equals(gp.aSetter.getObjectMap(gp.currentMap).get(key).name, "Box"))
                {
                    gp.buffManagerGame.randomBuff(row,column,gp.aSetter.getObjectMap(gp.currentMap).get(key));
                }
                gp.aSetter.removeObject(column, row, gp.currentMap);
            }
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

        if (entityRow == boomRow && entityCol == boomCol) {
            return true;
        }

        for (int[] coord : boom.explosionArea.get("Up")) {
            if (coord[0] == entityRow && coord[1] == entityCol) {
                return true;
            }
        }
        for (int[] coord : boom.explosionArea.get("Down")) {
            if (coord[0] == entityRow && coord[1] == entityCol) {
                return true;
            }
        }
        for (int[] coord : boom.explosionArea.get("Right")) {
            if (coord[0] == entityRow && coord[1] == entityCol) {
                return true;
            }
        }
        for (int[] coord : boom.explosionArea.get("Left")) {
            if (coord[0] == entityRow && coord[1] == entityCol) {
                return true;
            }
        }

        return false;
    }
}

package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class ExplosionManager {
    GamePanel gp;
    Entity entity;

    private String key;
    private int hardness;

    public ExplosionManager(GamePanel gp, Entity entity) {
        this.gp = gp;
        this.entity = entity;
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

        if (isPlayerInExplosionRange(boom)) {
            gp.eHandler.damagePit();
        }
    }

    private void check(int x, int y) {
        if (gp.aSetter.hasObjectAt(x, y)) {
            key = x + "," + y;
            hardness = gp.aSetter.getObjectMap().get(key).getHardness();
            if (hardness == 1)
                gp.aSetter.removeObject(x, y);
            else {
                gp.aSetter.getObjectMap().get(key).spriteNum++;
                gp.aSetter.getObjectMap().get(key).setHardness(--hardness);
            }
        }
    }


    private boolean isPlayerInExplosionRange(Boom boom) {
        int playerCol = gp.player.getCol();
        int playerRow = gp.player.getRow();

        int boomCol = boom.getCol();
        int boomRow = boom.getRow();

        int explosionRadius = boom.getRadiusExplosion();
        System.out.println(Math.abs(playerCol - boomCol));

        if (Math.abs(playerCol - boomCol) <= explosionRadius && playerRow == boomRow) {
            return true;
        }
        if (Math.abs(playerRow - boomRow) <= explosionRadius && playerCol == boomCol) {
            return true;
        }

        return false;
    }
}

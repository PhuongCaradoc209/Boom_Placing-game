package object;

import main.GamePanel;

public class ExplosionManager {
    GamePanel gp;

    private String key;
    private int hardness;
    public ExplosionManager(GamePanel gp) {
        this.gp = gp;
    }
    public void checkDestructibleTiles(Boom boom){
        check(boom.getCol() + 1, boom.getRow());
        check(boom.getCol() - 1, boom.getRow());
        check(boom.getCol(), boom.getRow() + 1);
        check(boom.getCol(), boom.getRow() - 1);
    }

    private void check(int x, int y){
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
}

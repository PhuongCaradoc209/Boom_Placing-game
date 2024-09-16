package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "heart";
        size = 3*gp.tileSize/5;
        image = setup("object/heart_full", size, size);
        image1 = setup("object/heart_empty",size, size);

        collision = false;
    }
}

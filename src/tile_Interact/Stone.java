package tile_Interact;

import main.GamePanel;

public class Stone extends InteractiveTile {
    public Stone(GamePanel gp, int col, int row){
        super(gp, col, row);
        name = "Stone";
        size = gp.tileSize;
        down1 = setup("object/stone", gp.tileSize, gp.tileSize);

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        isOpen = false;
        collision = true;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
//    public InteractiveTile getInteractedForm(){
//        InteractiveTile tile = new IT_Door_open(gp, 20, 12);
//        return tile;
//    }
}
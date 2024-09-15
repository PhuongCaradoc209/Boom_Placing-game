package tile_Interact;

import main.GamePanel;

public class Tree extends InteractiveTile {
    public Tree(GamePanel gp, int col, int row){
        super(gp, col, row);
        name = "Tree";
        size = gp.tileSize;
        down1 = setup("object/tree", gp.tileSize, gp.tileSize);

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        setHardness(1);
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
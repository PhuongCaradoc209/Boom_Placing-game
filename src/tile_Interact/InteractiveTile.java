package tile_Interact;

import main.GamePanel;
import entity.Entity;

public class InteractiveTile extends Entity {
    GamePanel gp;
    public boolean isOpen = false;
    public InteractiveTile(GamePanel gp, int col, int row){
        super(gp);
        this.gp = gp;
    }

    public InteractiveTile getInteractedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    public void update(){
    }
}
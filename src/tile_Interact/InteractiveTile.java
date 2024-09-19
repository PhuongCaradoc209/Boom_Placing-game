package tile_Interact;

import main.GamePanel;
import entity.Entity;

public class InteractiveTile extends Entity {
    GamePanel gp;
    private int hardness;
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

    public int getHardness(){
        return hardness;
    }

    public void setHardness(int hardness){
        this.hardness = hardness;
    }
}
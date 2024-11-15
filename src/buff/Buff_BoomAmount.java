package buff;

import entity.Entity;
import main.GamePanel;

public class Buff_BoomAmount extends Buff {
    public Buff_BoomAmount(Entity target, int row, int col, GamePanel gp) {
        super(target, row, col, gp);
        setDuration(-1); //infinite buff
        setDeBuff(false);

        getImage();
        setMaxAmount(2); //3
        setName("Multi-Bomb");
        setDescription("Adds one extra bomb for each buff, allowing the player to place more bombs simultaneously.");
    }
    @Override
    public void getImage() {
        buffImage = setup("buff_debuff/add_boom", 16, 16);
    }

    @Override
    public void removeEffect() {
    }

    @Override
    public void applyEffect() {
        target.setBoomAmount(target.getBoomAmount() + 1);
    }
}

package buff;

import entity.Entity;
import main.GamePanel;

public class Buff_RadiusBoom extends Buff {
    public Buff_RadiusBoom(Entity target, int row, int col, GamePanel gp) {
        super(target, row, col, gp);
        setDuration(-1); //infinite buff
        setDeBuff(false);

        getImage();
        setMaxAmount(2);
        setName("Blast Radius+");
        setDescription("Increases the bomb's explosion radius by one additional cell.");
    }
    @Override
    public void getImage() {
        buffImage = setup("buff_debuff/fire_spell", 16, 16);
    }

    @Override
    public void removeEffect() {
    }

    @Override
    public void applyEffect() {
        target.setBoomExplosionRadius(target.getBoomExplosionRadius() + 1);
    }
}
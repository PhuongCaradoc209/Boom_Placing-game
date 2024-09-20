package buff;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuffManager {
    private GamePanel gp;
    public List<Buff> buffs;
    private int appearCounter;

    public BuffManager(GamePanel gp) {
        buffs = new ArrayList<Buff>();
        this.gp = gp;
    }

    public void addBuff(Buff buff, Entity target) {
        appearCounter = 0;
        for (Buff b : buffs) {
            if (b.getClass() == buff.getClass()) {
                appearCounter++;
            }
        }

        if (appearCounter < buff.maxAmount || buff.maxAmount == -1) {
            buffs.add(buff);
            buff.setTarget(target);
            buff.applyEffect();
        }
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void update() {
        for (int i = 0; i < buffs.size(); i++) {
            Buff buff = buffs.get(i);
            buff.update();
            if (buff.getDuration() == 0) {
                buffs.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Buff buff : buffs) {
            buff.draw(g2);
        }
    }

    public void randomBuff(int row, int col, Entity target) {
        Random random = new Random();
        int i = random.nextInt(200) + 1;
        if (i <= 50) {
            addBuff(new Buff_RadiusBoom(target, row, col, gp));
        } else if (i <= 100) {
            addBuff(new Buff_BoomAmount(target, row, col, gp));
        } else if (i <= 150) {
        } else {
        }
    }
}
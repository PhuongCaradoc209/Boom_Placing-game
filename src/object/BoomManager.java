package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoomManager {
    private GamePanel gp;
    private Entity entity;
    private ExplosionManager explosionManager;

    public List<Boom> booms;
    private int boomAmount;

    public BoomManager(GamePanel gp, Entity entity) {
        this.gp = gp;
        this.entity = entity;
        this.booms = new ArrayList<>();
        this.explosionManager = new ExplosionManager(gp, entity);
    }

    public void update(){
        if (gp.keyHandler.spacePressed) {
            if (booms.size() < boomAmount) {
                entity.placeBoom();
            }
        }
        for (int i = 0; i < booms.size(); i++) {
            booms.get(i).update();
            if (booms.get(i).isExplode()) {
                explosionManager.checkDestructibleTiles(booms.get(i));
                booms.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g2){
        //DRAW BOOMS
        for (Boom boom : booms) {
            boom.draw(g2);
        }
    }

    public void setBoomAmount(int boomAmount) {
        this.boomAmount = boomAmount;
    }
}

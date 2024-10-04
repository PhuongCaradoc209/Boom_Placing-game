package entity;

import main.GamePanel;

public class Projectile extends Entity {
    Entity user;

    private int attack;

    public Projectile(GamePanel gp) {
        super(gp);
        alive = false;
    }

    public void set(double worldX, double worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.setLife(this.getMaxLife());
    }

    public void update() {
        if (user != gp.player) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (!gp.player.invisible && contactPlayer) {
                gp.eHandler.damagePit(gp.player);
                alive = false;
                setDead(true);
                deathAnimationComplete = true;
            }
        }

        switch (direction) {
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "right":
                worldX += speed;
                break;
            case "left":
                worldX -= speed;
                break;
        }

        setLife(getLife() - 1);
        if (getLife() <= 0) {
            alive = false;
            setDead(true);
            deathAnimationComplete = true;
        }

//        spriteCounter++;
//        if (spriteCounter > 15) {
//            if (spriteNum == 1)
//                spriteNum = 2;
//            else if (spriteNum == 2)
//                spriteNum = 3;
//            else if (spriteNum == 3)
//                spriteNum = 4;
//            else if (spriteNum == 4)
//                spriteNum = 5;
//            else if (spriteNum == 5)
//                spriteNum = 1;
//            spriteCounter = 0;
//        }

        spriteCounter++;
        if (spriteCounter > 15) {
            if (spriteNum == 1)
                spriteNum = 2;
            else if (spriteNum == 2)
                spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
}
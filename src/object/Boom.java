package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Boom extends Entity {
    private int x,y;
    private boolean exploded;
    BufferedImage[] boomAnim;
    private int frameBoom;
    private int intervalBoom = 24;
    private int currentFrame = 0;
    private int countToExplode = 0;
    private int maxCountToExplode = 4; // Số lần lặp lại toàn bộ animation

    public Boom(int x, int y, int countToExplode, GamePanel gp){
        super(gp);
        boomAnim = new BufferedImage[3];
        this.x = x;
        this.y = y;
        this.exploded = false;
        setUpImage();
    }

    private void setUpImage(){
        for(int i = 0; i < 3; i++){
            boomAnim[i] = setup("object/boom_" + (i + 1), 3*gp.tileSize/4,3*gp.tileSize/4);
        }
    }

    public void update() {
        frameBoom++; // Tăng frameBoom theo thời gian
        if (frameBoom == intervalBoom) {
            frameBoom = 0;
            currentFrame++; // Tăng currentFrame để thay đổi hình ảnh
            if (currentFrame >= boomAnim.length) { // Quay lại frame đầu tiên nếu đã hết
                currentFrame = 0;
                countToExplode++;
            }
            if (countToExplode >= maxCountToExplode) {
                exploded = true;
                countToExplode = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(boomAnim[currentFrame], x, y, null);
    }

    //Explode
    public boolean isExplode() {
        return exploded;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

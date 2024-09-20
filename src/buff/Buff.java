package buff;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Buff {
    GamePanel gp;
    private int duration;

    protected Entity target;
    private int startTime;
    private boolean isDeBuff;

    private int row, col;
    private double worldX, worldY;

    BufferedImage buffImage;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;

    protected int maxAmount;
    private String name;

    private double time = 0;
    private double amplitude = 5; // biên độ dao động
    private double speed = 0.075;  // tốc độ dao động

    public Buff(Entity target, int row, int col, GamePanel gp) {
        this.target = target;
        this.row = row;
        this.col = col;
        this.gp = gp;

        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 2 * gp.tileSize / 3;
        solidArea.height = 2 * gp.tileSize / 3;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        worldX = col * gp.tileSize;
        worldY = row * gp.tileSize;
    }

    public abstract void getImage();

    public abstract void applyEffect();

    public abstract void removeEffect();

    public void update() {
        if (duration > 0) duration--;
    }

    public void draw(Graphics2D g2) {
        double screenX = target.worldX - gp.player.worldX + gp.player.screenX;
        double screenY = target.worldY - gp.player.worldY + gp.player.screenY;

        // STOP MOVING THE CAMERA AT EDGE (ENTITY CAN NOT MOVE IF AT EDGE)
        // TOP
        if (gp.player.screenX >= gp.player.worldX) {
            screenX = target.worldX;
        }
        // LEFT
        if (gp.player.screenY >= gp.player.worldY) {
            screenY = target.worldY;
        }
        // RIGHT
        double rightOffSet = gp.screenWidth - gp.player.screenX;
        if (rightOffSet >= gp.worldWidth - gp.player.worldX) {
            screenX = gp.screenWidth - (gp.worldWidth - target.worldX);
        }
        // BOTTOM
        double bottomOffSet = gp.screenHeight - gp.player.screenY;
        if (bottomOffSet >= gp.worldHeight - gp.player.worldY) {
            screenY = gp.screenHeight - (gp.worldHeight - target.worldY);
        }
        // Tạo hiệu ứng dao động
        time += speed;
        double oscillation = Math.sin(time) * amplitude;

        // Vẽ hình ảnh với hiệu ứng dao động Y
        g2.drawImage(buffImage, (int) screenX + gp.tileSize / 6, (int) (screenY + oscillation) + gp.tileSize / 6, 2 * gp.tileSize / 3, 2 * gp.tileSize / 3, null);
    }


    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO
                    .read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public boolean isDeBuff() {
        return isDeBuff;
    }

    public void setDeBuff(boolean deBuff) {
        isDeBuff = deBuff;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double getWorldX() {
        return worldX;
    }

    public void setWorldX(double worldX) {
        this.worldX = worldX;
    }

    public double getWorldY() {
        return worldY;
    }

    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package event;

import entity.Entity;
import main.GamePanel;

public class EventHandler {
    GamePanel gp;
    private EventRect[][][] eventRect;
    private int count = 0;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxMap][gp.maxWorldRow][gp.maxWorldCol];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gp.maxMap && row < gp.maxWorldRow && col < gp.maxWorldCol) {
            eventRect[map][row][col] = new EventRect();
            eventRect[map][row][col].x = 23;
            eventRect[map][row][col].y = 23;
            eventRect[map][row][col].width = 2;
            eventRect[map][row][col].height = 2;
            eventRect[map][row][col].eventRectDefaultX = eventRect[map][row][col].x;
            eventRect[map][row][col].eventRectDefaultY = eventRect[map][row][col].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        healing();
    }

    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        if (map == gp.currentMap) {
            gp.player.solidArea.x = (int) (gp.player.worldX + gp.player.solidArea.x);
            gp.player.solidArea.y = (int) (gp.player.worldY + gp.player.solidArea.y);

            eventRect[map][row][col].x = row * gp.tileSize + eventRect[map][row][col].x;
            eventRect[map][row][col].y = col * gp.tileSize + eventRect[map][row][col].y;

            if (gp.player.solidArea.intersects(eventRect[map][row][col])) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][row][col].x = eventRect[map][row][col].eventRectDefaultX;
            eventRect[map][row][col].y = eventRect[map][row][col].eventRectDefaultY;
        }

        return hit;
    }

    public void damagePit(Entity entity) {
        if (entity.getLife() > 1) {
            entity.setLife(entity.getLife() - 1);
        } else {
            if (entity != gp.player) {
                entity.setDead(true);
            }
        }
    }

    public void healing() {
        if (gp.keyHandler.HPressed) {
            gp.player.setLife(gp.player.getMaxLife());
        }
        gp.keyHandler.HPressed = false;
    }
}
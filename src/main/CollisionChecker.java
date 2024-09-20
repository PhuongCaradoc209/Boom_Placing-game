package main;

import buff.Buff;
import entity.Entity;
import object.Boom;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkAtEdge(Entity entity) {
        //IF PLAYER MOVING TO THE EDGE
        if (entity.direction.equals("up") || entity.direction.equals("diagonalUpLeft") || entity.direction.equals("diagonalUpRight")) {
            if (entity.worldY <= 0) {
                entity.collisionOn = true;
            }
        }
        if (entity.direction.equals("down") || entity.direction.equals("diagonalDownLeft") || entity.direction.equals("diagonalDownRight")) {
            if (gp.worldHeight - entity.worldY - gp.tileSize - 5 <= 0) {
                entity.collisionOn = true;
            }
        }
        if (entity.direction.equals("left") || entity.direction.equals("diagonalUpLeft") || entity.direction.equals("diagonalDownLeft")) {
            if (entity.worldX <= 0) {
                entity.collisionOn = true;
            }
        }
        if (entity.direction.equals("right") || entity.direction.equals("diagonalUpRight") || entity.direction.equals("diagonalDownRight")) {
            if (gp.worldWidth - entity.worldX - gp.tileSize - 5 <= 0) {
                entity.collisionOn = true;
            }
        }
    }

    public void checkTile(Entity entity) {
        //CREATE COORDINATE OF 4 EDGES OF AN ENTITY
        double entityLeftWorldX = (entity.worldX + entity.solidArea.x);
        double entityRightWorldX = (entity.worldX + entity.solidArea.x + entity.solidArea.width);

        double entityTopWorldY = (entity.worldY + entity.solidArea.y);
        double entityBottomWorldY = (entity.worldY + entity.solidArea.y + entity.solidArea.height);

        int entityLeftCol = (int) (entityLeftWorldX / gp.tileSize);
        int entityRightCol = (int) (entityRightWorldX / gp.tileSize);

        int entityTopRow = (int) (entityTopWorldY / gp.tileSize);
        int entityBottomRow = (int) (entityBottomWorldY / gp.tileSize);

        //CHECK WHICH ARE TWO TILES FOR TWO SIDES OF ENTITY
        int tileNum1 = 1, tileNum2 = 1;
        if (entity.direction.equals("up") || entity.direction.equals("diagonalUpLeft") || entity.direction.equals("diagonalUpRight")) {
            //because when Entity go up, the first one that interacts with tiles is Top Edge,
            entityTopRow = (int) ((entityTopWorldY - entity.speed) / gp.tileSize);
            //so we find out what tile the player is trying to step in
            //tileNum1 is the point at the left-top conner of the entity
            tileNum1 = gp.tileMgr.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
            //tileNum1 is the point at the right-top conner of the entity
            tileNum2 = gp.tileMgr.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];
        }
        if (entity.direction.equals("down") || entity.direction.equals("diagonalDownLeft") || entity.direction.equals("diagonalDownRight")) {
            entityBottomRow = (int) ((entityBottomWorldY + entity.speed) / gp.tileSize);
            tileNum1 = gp.tileMgr.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
            tileNum2 = gp.tileMgr.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
        }
        if (entity.direction.equals("left") || entity.direction.equals("diagonalUpLeft") || entity.direction.equals("diagonalDownLeft")) {
            entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
            tileNum1 = gp.tileMgr.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
            tileNum2 = gp.tileMgr.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
        }
        if (entity.direction.equals("right") || entity.direction.equals("diagonalUpRight") || entity.direction.equals("diagonalDownRight")) {
            entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
            tileNum1 = gp.tileMgr.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];
            tileNum2 = gp.tileMgr.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
        }

        if (gp.tileMgr.tile[tileNum1].collision || gp.tileMgr.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }
    }

    public int checkObj(Entity entity, boolean isPlayer) {
        int index = 999;
        for (int i = 0; i < gp.obj[gp.currentMap].size(); i++) {
            if (gp.obj[gp.currentMap].get(i) != null) {
                //get the entity's solid area position within the game world
                entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
                entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);
                //get the object's solid area position within the game world
                gp.obj[gp.currentMap].get(i).solidArea.x = (int) (gp.obj[gp.currentMap].get(i).worldX + gp.obj[gp.currentMap].get(i).solidArea.x);
                gp.obj[gp.currentMap].get(i).solidArea.y = (int) (gp.obj[gp.currentMap].get(i).worldY + gp.obj[gp.currentMap].get(i).solidArea.y);

                switch (entity.direction) {
                    //SIMULATING ENTITY'S MOVEMENT AND CHECK WHERE IT WILL BE AFTER IT MOVED
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(gp.obj[gp.currentMap].get(i).solidArea)) {
                    if (gp.obj[gp.currentMap].get(i).collision == true) {
                        entity.collisionOn = true;
                    }
                    if (isPlayer) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap].get(i).solidArea.x = gp.obj[gp.currentMap].get(i).solidAreaDefaultX;
                gp.obj[gp.currentMap].get(i).solidArea.y = gp.obj[gp.currentMap].get(i).solidAreaDefaultY;
            }
        }
        return index;
    }

    public <T extends Entity> int checkEntity(Entity entity, ArrayList<T>[] target) {
        int index = 999;
        for (int i = 0; i < target[gp.currentMap].size(); i++) {
            if (target[gp.currentMap].get(i) != null) {
                //get the entity's solid area position within the game world
                entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
                entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);

                //get the npc's solid area position within the game world
                target[gp.currentMap].get(i).solidArea.x = (int) (target[gp.currentMap].get(i).worldX + target[gp.currentMap].get(i).solidArea.x);
                target[gp.currentMap].get(i).solidArea.y = (int) (target[gp.currentMap].get(i).worldY + target[gp.currentMap].get(i).solidArea.y);


                switch (entity.direction) {
                    //SIMULATING ENTITY'S MOVEMENT AND CHECK WHERE IT WILL BE AFTER IT MOVED
                    case "up":
                        entity.solidArea.y -= (int) entity.speed + 5;
                        break;

                    case "down":
                        entity.solidArea.y += (int) entity.speed + 5;
                        break;

                    case "right":
                        entity.solidArea.x += (int) entity.speed + 5;
                        break;

                    case "left":
                        entity.solidArea.x -= (int) entity.speed + 5;
                        break;
                }
                if (entity.solidArea.intersects(target[gp.currentMap].get(i).solidArea)) {
                    if (target[gp.currentMap].get(i) != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap].get(i).solidArea.x = target[gp.currentMap].get(i).solidAreaDefaultX;
                target[gp.currentMap].get(i).solidArea.y = target[gp.currentMap].get(i).solidAreaDefaultY;
            }
        }
        return index;
    }

    public <T extends Entity> int[] checkEntity(Entity entity, HashMap<String, T> targetMap) {
        int[] collisionCoordinates = null;

        for (String key : targetMap.keySet()) {
            T targetEntity = targetMap.get(key);

            if (targetEntity != null) {
                // Tính toán vùng va chạm của entity
                Rectangle entitySolidArea = new Rectangle(
                        (int) (entity.worldX + entity.solidArea.x),
                        (int) (entity.worldY + entity.solidArea.y),
                        entity.solidArea.width,
                        entity.solidArea.height
                );

                // Tính toán vùng va chạm của target entity
                Rectangle targetSolidArea = new Rectangle(
                        (int) (targetEntity.worldX + targetEntity.solidArea.x),
                        (int) (targetEntity.worldY + targetEntity.solidArea.y),
                        targetEntity.solidArea.width,
                        targetEntity.solidArea.height
                );

                // Mô phỏng chuyển động của entity
                switch (entity.direction) {
                    case "up":
                        entitySolidArea.y -= (int) entity.speed + 5;
                        break;
                    case "down":
                        entitySolidArea.y += (int) entity.speed + 5;
                        break;
                    case "left":
                        entitySolidArea.x -= (int) entity.speed + 5;
                        break;
                    case "right":
                        entitySolidArea.x += (int) entity.speed + 5;
                        break;
                }

                if (entitySolidArea.intersects(targetSolidArea) && targetEntity != entity) {
                    entity.collisionOn = true;
                    String[] coords = key.split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);

                    collisionCoordinates = new int[]{x, y};
                    break;
                }
            }
        }
        return collisionCoordinates;
    }

    public void checkBoom(Entity entity, List<Boom> target) {
        int index = 999;
        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                // Tính toán vùng va chạm của entity
                Rectangle entitySolidArea = new Rectangle(
                        (int) (entity.worldX + entity.solidArea.x),
                        (int) (entity.worldY + entity.solidArea.y),
                        entity.solidArea.width,
                        entity.solidArea.height
                );

                // Tính toán vùng va chạm của target entity
                Rectangle targetSolidArea = new Rectangle(
                        (int) (target.get(i).getWorldX() + target.get(i).solidArea.x),
                        (int) (target.get(i).getWorldY() + target.get(i).solidArea.y),
                        target.get(i).solidArea.width,
                        target.get(i).solidArea.height
                );

                // Mô phỏng chuyển động của entity
                switch (entity.direction) {
                    case "up":
                        entitySolidArea.y -= (int) entity.speed + 5;
                        break;
                    case "down":
                        entitySolidArea.y += (int) entity.speed + 5;
                        break;
                    case "left":
                        entitySolidArea.x -= (int) entity.speed + 5;
                        break;
                    case "right":
                        entitySolidArea.x += (int) entity.speed + 5;
                        break;
                }

                if (entitySolidArea.intersects(targetSolidArea) && target.get(i) != entity && target.get(i).collision) {
                    entity.collisionOn = true;
                    index = i;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }
    }

    public void checkPlayer(Entity entity) {
        //get the entity's solid area position within the game world
        entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
        entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);
        //get the player's solid area position within the game world
        gp.player.solidArea.x = (int) (gp.player.worldX + gp.player.solidArea.x);
        gp.player.solidArea.y = (int) (gp.player.worldY + gp.player.solidArea.y);

        switch (entity.direction) {
            //SIMULATING ENTITY'S MOVEMENT AND CHECK WHERE IT WILL BE AFTER IT MOVED
            case "up":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                }
                break;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

    public Buff checkCollectBuff(Entity entity) {
        for (int i = 0; i < gp.buffManagerGame.buffs.size(); i++) {
            if (gp.buffManagerGame.buffs.get(i) != null) {
                
                entity.solidArea.x = (int) (entity.worldX + entity.solidArea.x);
                entity.solidArea.y = (int) (entity.worldY + entity.solidArea.y);

               gp.buffManagerGame.buffs.get(i).solidArea.x = (int) (gp.buffManagerGame.buffs.get(i).getWorldX() +gp.buffManagerGame.buffs.get(i).solidArea.x);
               gp.buffManagerGame.buffs.get(i).solidArea.y = (int) (gp.buffManagerGame.buffs.get(i).getWorldY() +gp.buffManagerGame.buffs.get(i).solidArea.y);

                switch (entity.direction) {
                    //SIMULATING ENTITY'S MOVEMENT AND CHECK WHERE IT WILL BE AFTER IT MOVED
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                }
                if (entity.solidArea.intersects(gp.buffManagerGame.buffs.get(i).solidArea)) {
                    return gp.buffManagerGame.buffs.get(i);
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.buffManagerGame.buffs.get(i).solidArea.x = gp.buffManagerGame.buffs.get(i).solidAreaDefaultX;
                gp.buffManagerGame.buffs.get(i).solidArea.y = gp.buffManagerGame.buffs.get(i).solidAreaDefaultY;
            }
        }
        return null;
    }
}

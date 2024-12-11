package main;
import enemy.Ene_Lizard;
import enemy.Ene_Slime;
import enemy.Ene_WonWon;
import entity.Entity;
import tile_Interact.Box;
import tile_Interact.InteractiveTile;
import tile_Interact.Stone;
import tile_Interact.Tree;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;

public class AssetSetter {
    GamePanel gp;
    private int i,mapNum;
    private Random random;
    private HashMap<String, InteractiveTile>[] objectMaps;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        random = new Random();
        objectMaps = new HashMap[gp.maxMap];
        for (int i = 0; i < objectMaps.length; i++) {
            objectMaps[i] = new HashMap<>();
        }
    }

    public void setObject() {
        mapNum = 0;
        i = 0;
    }

    public void setInteractiveTile() {
        mapNum = 0;

        i = 0;
        loadTreesFromFile(Tree.class,"res/coordinateTiles/treeCoordinate_" + (i+1),gp, mapNum);
        loadTreesFromFile(Box.class,"res/coordinateTiles/boxCoordinate_" + (i+1),gp, mapNum);
        loadTreesFromFile(Stone.class,"res/coordinateTiles/stoneCoordinate_" + (i+1),gp, mapNum);

        mapNum++;
        i++;
        loadTreesFromFile(Tree.class,"res/coordinateTiles/treeCoordinate_" + (i+1),gp, mapNum);
        loadTreesFromFile(Box.class,"res/coordinateTiles/boxCoordinate_" + (i+1),gp, mapNum);
        loadTreesFromFile(Stone.class,"res/coordinateTiles/stoneCoordinate_" + (i+1),gp, mapNum);
    }

    public void setEnemy(){
        mapNum = 0;
        i = 0;

        gp.enemy[mapNum].add(new Ene_Slime(gp));
        gp.enemy[mapNum].get(i).worldX = 10 * gp.tileSize;
        gp.enemy[mapNum].get(i).worldY = 10 * gp.tileSize;


        i = 0;
        mapNum++;

        gp.enemy[mapNum].add(new Ene_Slime(gp));
        gp.enemy[mapNum].get(i).worldX = 3 * gp.tileSize;
        gp.enemy[mapNum].get(i).worldY = 2 * gp.tileSize;
        i++;

        gp.enemy[mapNum].add(new Ene_WonWon(gp));
        gp.enemy[mapNum].get(i).worldX = 12 * gp.tileSize;
        gp.enemy[mapNum].get(i).worldY = 13 * gp.tileSize;
        i++;

        gp.enemy[mapNum].add(new Ene_Lizard(gp));
        gp.enemy[mapNum].get(i).worldX = 2 * gp.tileSize;
        gp.enemy[mapNum].get(i).worldY = 12 * gp.tileSize;
        i++;
    }

    public <T extends Entity> void loadTreesFromFile(Class<T> tile, String fileName, GamePanel gp, int mapIndex) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                Constructor<T> constructor = tile.getConstructor(GamePanel.class, int.class, int.class);

                T object = constructor.newInstance(gp, x, y);

                String key = x + "," + y;
                objectMaps[mapIndex].put(key, (InteractiveTile) object);
            }
        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, InteractiveTile> getObjectMap(int mapIndex) {
        return objectMaps[mapIndex];
    }

    public void removeObject(int x, int y, int mapIndex) {
        String key = x + "," + y;
        objectMaps[mapIndex].remove(key);
    }

    public boolean hasObjectAt(int x, int y, int mapIndex) {
        String key = x + "," + y;
        return objectMaps[mapIndex].containsKey(key);
    }
}
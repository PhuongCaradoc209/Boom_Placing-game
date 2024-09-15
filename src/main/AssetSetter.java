package main;
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
    private HashMap<String, InteractiveTile> objectMap;
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        random = new Random();
        objectMap = new HashMap<>();
    }

    public void setObject() {
        mapNum = 0;
        i = 0;
    }

    public void setInteractiveTile() {
        mapNum = 0;

        i = 0;
        loadTreesFromFile(Tree.class,"res/object/treeCoordinate",gp);
        loadTreesFromFile(Box.class,"res/object/boxCoordinate",gp);
        loadTreesFromFile(Stone.class,"res/object/stoneCoordinate",gp);
    }

    public <T extends Entity> void loadTreesFromFile(Class<T> tile, String fileName, GamePanel gp) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                Constructor<T> constructor = tile.getConstructor(GamePanel.class, int.class, int.class);

                T object = constructor.newInstance(gp, x, y);

                String key = x + "," + y;
                objectMap.put(key, (InteractiveTile) object);
            }
        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, InteractiveTile> getObjectMap() {
        return objectMap;
    }

    public void removeObject(int x, int y) {
        String key = x + "," + y; // Tạo chuỗi key từ tọa độ
        objectMap.remove(key);
    }

    public boolean hasObjectAt(int x, int y) {
        String key = x + "," + y;
        return objectMap.containsKey(key);
    }
}
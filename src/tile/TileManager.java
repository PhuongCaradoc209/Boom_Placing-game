package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();


    public TileManager(GamePanel gp) {
        this.gp = gp;


        //READ TILE DATA FILE
        InputStream is = getClass().getResourceAsStream("/tiles/tiledata");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //GETTING THE TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;
        try {
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        tile = new Tile[fileNames.size()];
        mapTileNum = new int[gp.maxMap][gp.maxWorldRow][gp.maxWorldCol];

        getTileImage();
        loadMap("/maps/mapdata", 0);
    }

    public void getTileImage() {
        String fileName;
        boolean collision;
        for (int i = 0; i< fileNames.size() - 1; i++){
            //Get a file name
            fileName = fileNames.get(i);

            //Get a collision status
            if (collisionStatus.get(i).equals("true")){
                collision = true;
            }else{
                collision = false;
            }

            setup(i, fileName, collision);
        }
    }

    private void setup(int index, String fileName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            String img = String.format("tiles/" + "%02d" + ".png", index);
            tile[index].image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(img)));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            String line;
            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                String[] numbers = line.split(" ");
                for (int col = 0; col < gp.maxWorldCol && col < numbers.length; col++) {
                    mapTileNum[map][row][col] = Integer.parseInt(numbers[col]);
                }
                row++;
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void draw(Graphics2D g2) {
        int worldRow = 0, worldCol = 0;
        int tileNum;
        while (worldRow < gp.maxWorldRow && worldCol < gp.maxWorldCol) {
            tileNum = mapTileNum[gp.currentMap][worldRow][worldCol];
            //Coordinate for the world game
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            //Coordinate for the screen

            double screenX = worldX - 15 + 15;
            double screenY = worldY - 15 + 15;

            //STOP MOVING THE CAMERA AT EDGE (TILES CAN NOT MOVE IF AT EDGE)
            //TOP
//            if (gp.player.screenX >= gp.player.worldX) {
//                screenX = worldX;
//            }
//            //LEFT
//            if (gp.player.screenY >= gp.player.worldY) {
//                screenY = worldY;
//            }
//            //RIGHT
//            double rightOffSet = gp.screenWidth - gp.player.screenX;
//            if (rightOffSet >= gp.worldWidth - gp.player.worldX) {
//                screenX = gp.screenWidth - (gp.worldWidth - worldX);
//            }
//            //BOTTOM
//            double bottomOffSet = gp.screenHeight - gp.player.screenY;
//            if (bottomOffSet >= gp.worldHeight - gp.player.worldY) {
//                screenY = gp.screenHeight - (gp.worldHeight - worldY);
//            }
//            ////////////////////////
//
//            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
//                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
//                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
//                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
//                g2.drawImage(tile[tileNum].image, (int) screenX, (int) screenY, null);
//            }
//            //IF PLAYER AT THE EDGE
//            else if (gp.player.screenX > gp.player.worldX ||
//                    gp.player.screenY > gp.player.worldY ||
//                    rightOffSet > gp.worldWidth - gp.player.worldX ||
//                    bottomOffSet > gp.worldHeight - gp.player.worldY) {
                g2.drawImage(tile[tileNum].image, (int) screenX, (int) screenY, null);
//            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

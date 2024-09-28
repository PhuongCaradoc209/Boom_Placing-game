package UI;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Heart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class UI {
    GamePanel gp;
    Graphics2D g2;

    // FONT AND TEXT
    Font pixel;

    //
    private UI_mapSelection mapSelection;

    //GRAPHICS
    private final Area screenArea;
    BufferedImage image;
    BufferedImage playerImage;
    BufferedImage mapImage_1;
    private final BufferedImage heart_full, heart_empty;

    // SETTING
    public int commandNum = 0;

    //COLOR
    final Color primaryColor_green = new Color(0x809d49);
    final Color primaryColor_greenOutline = new Color(0x236B06);

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            pixel = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }

        mapSelection = new UI_mapSelection(gp);

        // SET UP SCREEN AREA
        screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        //GRAPHICS
        //TITLE SCREEN
        playerImage = setup("player/standRight_1", 32, 32);

        //MAP SELECT SCREEN
        mapImage_1 = setup("maps/map_1", 256, 256);

        //PLAYER LIFE
        Entity heartStatus = new OBJ_Heart(gp);
        heart_full = heartStatus.image;
        heart_empty = heartStatus.image1;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixel);
        g2.setColor(Color.white);

        // TITTLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //PLAY STATE
        else if (gp.gameState == gp.playState) {
            drawPLayerInformation();
        }
        //MAP SELECT STATE
        else if (gp.gameState == gp.mapSelectState){
            drawMapSelectScreen();
        }
    }

    private void drawPLayerInformation(){
        drawPlayerLife();
    }

    private void drawPlayerLife(){
        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;

        int i = 0;

        // DRAW BLANK HEARTS
        while (i < gp.player.getMaxLife()) {
            g2.drawImage(heart_empty, x, y, null);
            i ++;
            x += (4*gp.tileSize / 5);
        }
        // RESET
        x = gp.tileSize / 4;
        y = gp.tileSize / 4;
        i = 0;
        while (i < gp.player.getLife()) {
            g2.drawImage(heart_full, x, y, null);
            i++;
            x += (4*gp.tileSize / 5);
        }
    }

    private void drawTitleScreen() {
        // DRAW BACKGROUND
        g2.setColor(primaryColor_green);
        g2.fill(screenArea);

        // TITTLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        g2.setColor(Color.white);
        String text = "BOOM C";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 2;

        // SHADOW TEXT
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);

        // MAIN COLOR TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;
        // TEXT OUTLINE
        g2.setColor(primaryColor_greenOutline);
        g2.drawString(text, x - 2, y - 2);
        g2.drawString(text, x - 2, y + 2);
        g2.drawString(text, x + 2, y - 2);
        g2.drawString(text, x + 2, y + 2);
        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);
        // DRAW TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage, x - 3*gp.tileSize/2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.setColor(Color.white);
            if (gp.keyHandler.enterPressed == true) {
                gp.gameState = gp.mapSelectState;
            }
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 1.5;
        // TEXT OUTLINE
        g2.setColor(primaryColor_greenOutline);
        g2.drawString(text, x - 2, y - 2);
        g2.drawString(text, x - 2, y + 2);
        g2.drawString(text, x + 2, y - 2);
        g2.drawString(text, x + 2, y + 2);
        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);
        // DRAW TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage, x - 3*gp.tileSize/2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.setColor(Color.white);
            if (gp.keyHandler.enterPressed == true) {
                gp.gameState = gp.mapSelectState;
            }
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize * 1.5;
        // TEXT OUTLINE
        g2.setColor(primaryColor_greenOutline);
        g2.drawString(text, x - 2, y - 2);
        g2.drawString(text, x - 2, y + 2);
        g2.drawString(text, x + 2, y - 2);
        g2.drawString(text, x + 2, y + 2);
        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);
        // DRAW TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage, x - 3*gp.tileSize/2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.setColor(Color.white);
            if(gp.keyHandler.enterPressed == true){
                System.exit(0);
            }
        }
        //Reset enterPressed
        gp.keyHandler.enterPressed = false;
    }

    private void drawMapSelectScreen(){
        mapSelection.draw(g2);
        if (gp.keyHandler.enterPressed) {
            gp.tileMgr.loadMap("/maps/mapdata_" + (mapSelection.getSelectedMapIndex() + 1), mapSelection.getSelectedMapIndex());
            gp.gameState = gp.playState;
        }
        gp.keyHandler.enterPressed = false;
    }

    //FEATURE METHOD
    private int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
    private BufferedImage setup(String imagePath, int width, int height) {
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
}

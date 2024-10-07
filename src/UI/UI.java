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
    UI_mapSelection mapSelection;

    // FONT AND TEXT
    Font pixel;
    protected Font font, font1, font1a, font2, font3, font3a, font4, font4a, font4b, font5, font6, font7, font8;
    protected String currentDialogue = "";
    protected String currentNotification = "";
    protected String currentTittle = "";

    //GRAPHICS
    protected final Area screenArea;
    protected BasicStroke defaultStroke;

    //IMAGES
    BufferedImage image;
    BufferedImage playerImage;
    BufferedImage mapImage_1;
    private final BufferedImage heart_full, heart_empty;

    // SETTING
    public int commandNum = 0;
    public int subState = 0;

    //COLOR
    final Color primaryColor_green = new Color(0x809d49);
    final Color primaryColor_greenOutline = new Color(0x236B06);
    final Color colorOfVolume = new Color(0x4155be);


    public UI(GamePanel gp) {
        this.gp = gp;
        mapSelection = new UI_mapSelection(gp);

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            pixel = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }

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

        // SET UP FONT
        font = pixel.deriveFont(Font.BOLD, 60f);
        font1 = pixel.deriveFont(Font.BOLD, 30f);
        font1a = pixel.deriveFont(Font.PLAIN, 32f);
        font2 = pixel.deriveFont(Font.BOLD, 10f);
        font3 = pixel.deriveFont(Font.BOLD, 20f);
        font3a = pixel.deriveFont(Font.PLAIN, 20f);
        font4 = pixel.deriveFont(Font.BOLD, 25f);
        font4a = pixel.deriveFont(Font.PLAIN, 22f);
        font4b = pixel.deriveFont(Font.PLAIN, 25f);
        font5 = pixel.deriveFont(Font.BOLD, 38f);
        font6 = pixel.deriveFont(Font.PLAIN, 18f);
        font7 = pixel.deriveFont(Font.BOLD, 15f);
        font8 = pixel.deriveFont(Font.BOLD, 45f);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        defaultStroke = new BasicStroke(3);

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
        else if (gp.gameState == gp.mapSelectState) {
            drawMapSelectScreen();
        }
        // OPTION STATE
        else if (gp.gameState == gp.optionState) {
            drawPLayerInformation();
            drawOptionScreen();
        }
    }

    private void drawPLayerInformation() {
        drawPlayerLife();
    }

    private void drawPlayerLife() {
        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;

        int i = 0;

        // DRAW BLANK HEARTS
        while (i < gp.player.getMaxLife()) {
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x += (4 * gp.tileSize / 5);
        }
        // RESET
        x = gp.tileSize / 4;
        y = gp.tileSize / 4;
        i = 0;
        while (i < gp.player.getLife()) {
            g2.drawImage(heart_full, x, y, null);
            i++;
            x += (4 * gp.tileSize / 5);
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
            g2.drawImage(playerImage, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
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
            g2.drawImage(playerImage, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
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
            g2.drawImage(playerImage, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
            g2.setColor(Color.white);
            if (gp.keyHandler.enterPressed == true) {
                System.exit(0);
            }
        }
        //Reset enterPressed
        gp.keyHandler.enterPressed = false;
    }

    private void drawMapSelectScreen() {
        mapSelection.draw(g2);
        if (gp.keyHandler.enterPressed) {
            gp.tileMgr.loadMap("/maps/mapdata_" + (mapSelection.getSelectedMapIndex() + 1), mapSelection.getSelectedMapIndex());
            gp.gameState = gp.playState;
        }
        gp.keyHandler.enterPressed = false;
    }

    public void drawOptionScreen() {
        g2.setColor(new Color(0.222f, 0.222f, 0.222f, 0.7f));
        g2.fill(screenArea);

        // SUB WINDOW
        int frameWidth = gp.tileSize * 5;
        int frameHeight = gp.tileSize * 7;
        int frameX = getCenterForElement(gp.screenWidth, frameWidth);
        int frameY = getCenterForElement(gp.screenHeight, frameHeight);

        // drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        drawSubWindow1(frameX, frameY, frameWidth, frameHeight, new Color(0x94a2e3), Color.BLACK, 5, 25);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        switch (subState) {
            case 0:
                option_top(frameX, frameY);
                break;

            case 1:
                options_control(frameX, frameY);
                break;

            case 2:
                options_endGameConfirmation(frameX, frameY);
                break;

            case 3:
                break;
        }
        gp.keyHandler.enterPressed = false;
    }

    private void option_top(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        g2.setFont(font);
        String text = "OPTIONS";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize / 2;
        g2.drawString(text, textX, textY + gp.tileSize * 3 / 4);
        g2.setFont(font1a);

        // MUSIC
        textX -= gp.tileSize / 2;
        textY += gp.tileSize + 60;
        g2.drawString("Music", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
        }
        // MUSIC VOLUME CONTROL
        g2.drawRect(textX + 120, textY - 13, 120, 12);
//        int volumeWidth = 24 * gp.music.volumeScale;
//        g2.fillRect(textX, textY + 6, volumeWidth, 12);
//        drawSubWindow1(volumeWidth + 5 + gp.tileSize * 41 / 4, textY, 13, 25, colorOfVolume, Color.BLACK, 2, 5);
//
//        g2.setStroke(defaultStroke);
//        // SE VOLUME
//        g2.setColor(colorOfVolume);
//        textY += gp.tileSize;
//        g2.drawRect(textX, textY + 6, 120, 12);
//        volumeWidth = 24 * gp.soundEffect.volumeScale;
//        g2.fillRect(textX, textY + 6, volumeWidth, 12);
//        drawSubWindow1(volumeWidth + 5 + gp.tileSize * 41 / 4, textY, 13, 25, colorOfVolume, Color.BLACK, 2, 5);

        // SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
        }

        // CONTROL
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 2;
                commandNum = 0;
            }
        }

        // END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 3;
                commandNum = 0;
            }

        }

        // BACK
        textY += gp.tileSize;
        g2.drawString("Back", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        g2.setColor(colorOfVolume);


//        //SAVE
//        gp.config.saveConfig();
    }

    private void options_control(int frameX, int frameY) {
        int textX;
        int textY;

        // DISPLAY TITTLE AND KEY
        String text = "CONTROL";
        textY = frameY + gp.tileSize;
        g2.setFont(font8);
        g2.drawString(text, center(text, gp.tileSize * 6, gp.tileSize * 8), textY);
        g2.setFont(font4b);

        textX = frameX + gp.tileSize * 3 / 2;
        int textX1 = textX + gp.tileSize * 4;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY);
        g2.drawString("W,A,S,D", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Fishing", textX, textY);
        g2.drawString("Space", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Bag", textX, textY);
        g2.drawString("B", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Menu", textX, textY);
        g2.drawString("ESC", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Collection", textX, textY);
        g2.drawString("C", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Fish tank", textX, textY);
        g2.drawString("L", textX1, textY);

        // BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    private void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the game and /nreturn to the tittle screen?";

        for (String line : currentDialogue.split("/n")) {
            g2.drawString(line, textX, textY);
            textY += gp.tileSize;
        }

        // YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 2;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }

        // NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }
    }

    //FEATURE METHOD
    protected int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    protected void drawSubWindow1(int x, int y, int width, int height, Color cbg, Color cs, int strokeSize, int arc) {
        g2.setColor(cbg);
        g2.fillRoundRect(x, y, width, height, arc, arc);
        g2.setColor(cs);
        g2.setStroke(new BasicStroke(strokeSize));
        g2.drawRoundRect(x, y, width, height, arc, arc);
    }

    protected int center(String s, int imageX, int imageWidth) {
        int textWidth = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
        return imageX + (imageWidth - textWidth) / 2;
    }

    private int getCenterForElement(int parentWidth, int childWidth) {
        return (parentWidth - childWidth) / 2;
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

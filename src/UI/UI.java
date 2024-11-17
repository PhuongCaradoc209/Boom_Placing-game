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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    UI_mapSelection mapSelection;

    // FONT AND TEXT
    Font pixel;
    protected Font font, font_title, font_text_plain, font_text_bold;
    protected String currentDialogue = "";
    protected String currentNotification = "";
    protected String currentTittle = "";
    List<String> lines;
    int lineHeight, yOffset;

    //GRAPHICS
    protected final Area screenArea;
    protected BasicStroke defaultStroke;

    //IMAGES
    BufferedImage image;
    BufferedImage playerImage_1, playerImage_2, playerImage_3, playerImage_4;
    BufferedImage mapImage_1;
    private final BufferedImage heart_full, heart_empty;
    BufferedImage iconSelected;
    BufferedImage controlIcon;
    BufferedImage playerIcon;

    //SETTING FOR MENU
    public int commandNum_Menu = 0;
    public int commandNum_Buff = -2;
    public int subMenuState = 0;

    //TITLE SETTING
    public int commandNum_Title = 0;

    //OPTION SETTING
    public int commandNum = -1;
    public int subOptionState = 0;

    //COLOR
    final Color primaryColor_green = new Color(0x809d49);
    final Color primaryColor_greenOutline = new Color(0x236B06);
    final Color colorOfVolume = new Color(0x4155be);
    final Color menuBackground = new Color(0x574852);
    final Color menuBorder = new Color(0xba9158);
    final Color menuSubBackground = new Color(0xba9158);
    final Color menuColorFont1 = new Color(0xd2b287);
    final Color menuColorFont2 = new Color(0x574852);

    //PLAYER ANIMATION
    private BufferedImage[] playerFrames;
    private int frameIndex = 0;
    private int frameCounter = 0;
    private int frameThreshold = 15;

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
        playerImage_1 = setup("player/standRight_1", 32, 32);
        playerImage_2 = setup("player/standRight_2", 32, 32);
        playerImage_3 = setup("player/standRight_3", 32, 32);
        playerImage_4 = setup("player/standRight_4", 32, 32);

        playerFrames = new BufferedImage[]{
                playerImage_1, playerImage_2, playerImage_3, playerImage_4
        };

        //ICON
        iconSelected = setup("icon/selectedIcon_1", 32, 32);
        controlIcon = setup("icon/control", 32, 32);
        playerIcon = setup("icon/player", 32, 32);

        //MAP SELECT SCREEN
        mapImage_1 = setup("maps/map_1", 256, 256);

        //PLAYER LIFE
        Entity heartStatus = new OBJ_Heart(gp);
        heart_full = heartStatus.image;
        heart_empty = heartStatus.image1;

        // SET UP FONT
        font = pixel.deriveFont(Font.BOLD, 60f);
        font_title = pixel.deriveFont(Font.BOLD, 48f);
        font_text_plain = pixel.deriveFont(Font.PLAIN, 35f);
        font_text_bold = pixel.deriveFont(Font.BOLD, 35f);
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
        // MENU STATE
        else if (gp.gameState == gp.menuState) {
//            drawCharacterStatus();
            drawMenu();
        }
        //GAME OVER START
        else if (gp.gameState == gp.gameOverState){
            drawGameOver();
        }
    }

    private void drawPLayerInformation() {
        drawPlayerLife(gp.tileSize / 4, gp.tileSize / 4, gp.tileSize / 2);
    }

    private void drawPlayerLife(int startX, int startY, int size) {
        // Draw blank hearts
        int x = startX;
        int y = startY;
        int i = 0;

        // DRAW BLANK HEARTS
        while (i < gp.player.getMaxLife()) {
            g2.drawImage(heart_empty, x, y, size, size, null);
            i++;
            x += (size * 1.2); // Adjust the spacing between hearts (size * 1.2 for padding)
        }

        // Reset position for full hearts
        x = startX;
        y = startY;
        i = 0;

        // DRAW FULL HEARTS
        while (i < gp.player.getLife()) {
            g2.drawImage(heart_full, x, y, size, size, null);
            i++;
            x += (size * 1.2); // Adjust the spacing between hearts (size * 1.2 for padding)
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
        if (commandNum_Title == 0) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage_1, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
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
        if (commandNum_Title == 1) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage_1, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
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
        if (commandNum_Title == 2) {
            g2.setColor(new Color(0xF9F07A));
            g2.drawString(text, x, y);
            g2.drawImage(playerImage_1, x - 3 * gp.tileSize / 2, y - gp.tileSize, gp.tileSize, gp.tileSize, null);
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
            gp.currentMap = mapSelection.getSelectedMapIndex();
            gp.gameState = gp.playState;
        }
        gp.keyHandler.enterPressed = false;
    }

    private void drawMenu() {
        g2.setColor(new Color(0.222f, 0.222f, 0.222f, 0.7f));
        g2.fill(screenArea);

        // SUB WINDOW
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 7;
        int frameX = getCenterForElement(gp.screenWidth, frameWidth);
        int frameY = getCenterForElement(gp.screenHeight, frameHeight);

        int frameButtonY = frameY + 20;
        int spaceButton = 20;
        int buttonWidth = gp.tileSize;
        int buttonHeight = gp.tileSize;

        Color selected = new Color(0xba9158);
        Color unSelected = new Color(0x574852);

        Color characterButton = unSelected;
        Color optionButton = unSelected;

        if (commandNum_Menu == 0) {
            characterButton = selected;
            if (gp.keyHandler.rightPressed && subMenuState == 0) {
                subMenuState = 2;
                commandNum = 0;
            }

            if (gp.keyHandler.leftPressed && (commandNum_Buff == -2 || commandNum != 1)) {
                subMenuState = 0;
                commandNum = -1;
            }
        }
        if (commandNum_Menu == 1) {
            optionButton = selected;
            if (gp.keyHandler.rightPressed) {
                subMenuState = 1;
                commandNum = 0;
            }
            if (gp.keyHandler.leftPressed) {
                subMenuState = 0;
                commandNum = -1;
            }
        }

        //CHARACTER STATUS BUTTON
        drawSubWindow1(frameX - 60, frameButtonY, gp.tileSize, gp.tileSize, characterButton, new Color(0x493628), 0, 25);
        g2.drawImage(playerIcon,
                frameX - 68 + (gp.tileSize - gp.tileSize + 10) / 2,
                frameButtonY + (gp.tileSize - gp.tileSize + 10) / 2,
                gp.tileSize - 10,
                gp.tileSize - 10,
                null);
        frameButtonY += (spaceButton + buttonHeight);

        //OPTION STATUS BUTTON
        drawSubWindow1(frameX - 60, frameButtonY, gp.tileSize, gp.tileSize, optionButton, new Color(0x493628), 0, 25);
        g2.drawImage(controlIcon,
                frameX - 68 + (gp.tileSize - gp.tileSize + 10) / 2,
                frameButtonY + (gp.tileSize - gp.tileSize + 10) / 2,
                gp.tileSize - 10,
                gp.tileSize - 10,
                null);

        // Draw the sub-window
        drawSubWindow1(frameX, frameY, frameWidth, frameHeight, menuBackground, menuBorder, 10, 25);
        g2.setColor(menuColorFont1);
        g2.setFont(g2.getFont().deriveFont(32F));

        switch (commandNum_Menu) {
            case 0:
                drawCharacterStatus(frameWidth, frameHeight, frameX, frameY);
                break;

            case 1:
                drawOptionScreen(frameWidth, frameHeight, frameX, frameY);
                break;

            case 3:
                break;
        }
        gp.keyHandler.rightPressed = false;
    }

    private void drawCharacterStatus(int frameWidth, int frameHeight, int frameX, int frameY) {
        // DRAW CHARACTER IMAGE
        int imageX = frameX + 20;
        int imageY = frameY + gp.tileSize + 40;
        int imageWidth = gp.tileSize * 2;
        int imageHeight = gp.tileSize * 2;
        drawCharacterAnimation(imageX, imageY, imageWidth, imageHeight);

        // CHARACTER SHADOW
        g2.setColor(new Color(0, 0, 0, 0.3f));
        int ovalWidth = gp.tileSize + 30;
        int ovalHeight = gp.tileSize / 4;

        int ovalX = getCenterForElement(imageX + imageWidth + frameX + 15, ovalWidth);
        int ovalY = imageY + imageHeight - gp.tileSize / 8;

        g2.fillOval(ovalX, ovalY, ovalWidth, ovalHeight);

        //DRAW PLAYER STATUS
        g2.setColor(menuColorFont1);
        int textY = gp.tileSize * 3 / 2;
        int lineSpacing = gp.tileSize / 2;

        g2.setFont(font);
        String title = "Player";
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        int textX = frameX + (frameWidth - titleWidth) / 2;

        g2.drawString(title, textX, textY);
        textY += lineSpacing * 2;

        textX += 20;
        lineSpacing += 20;

        String textContent;
        int textContentY = textY + gp.tileSize * 3;
        int rectWidth = frameWidth - 100;
        int rectX = frameX + getCenterForElement(frameWidth, rectWidth);

        g2.setColor(menuSubBackground);
        g2.fillRoundRect(rectX, textContentY - 50, rectWidth, gp.tileSize * 2, 10, 10);

        //DRAW LIFE
        g2.setColor(menuColorFont1);
        g2.setFont(font_text_plain);
        g2.drawString("Life", textX, textY);
        drawPlayerLife(textX + 100, textY - 35, 40);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            g2.setColor(menuColorFont2);
            textContent = "This shows the player's current life status";
            drawTextBlock(g2, textContent, rectX, textContentY, rectWidth);
        }
        textY += lineSpacing;

        //DRAW BUFF
        g2.setColor(menuColorFont1);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));
        g2.drawString("Buff", textX, textY);
        int tempX = textX + 100;
        int temp2X = tempX;
        int iconSpacing = 45;
        for (int i = 0; i < gp.player.ownBuffManager.buffs.size(); i++) {
            if (i % 3 == 0 && i != 0) {
                tempX = temp2X;
                textY += lineSpacing;
            }
            g2.drawImage(gp.player.ownBuffManager.buffs.get(i).buffImage, tempX, textY - 35, 40, 40, null);
            if (commandNum == 1 && commandNum_Buff == i) {
                g2.setFont(font_text_bold);
                g2.setColor(menuColorFont2);
                g2.drawImage(iconSelected, tempX, textY - 35, 40, 40, null);
                g2.drawString(gp.player.ownBuffManager.buffs.get(i).getName() + " (Max: " + gp.player.ownBuffManager.buffs.get(i).getMaxAmount() + ")", rectX + 20, textContentY);
                String description = gp.player.ownBuffManager.buffs.get(i).getDescription();
                drawTextBlock(g2, description, rectX, textContentY + g2.getFontMetrics().getHeight(), rectWidth);
            }
            tempX += iconSpacing;
        }
        if (commandNum == 1) {
            g2.setFont(font_text_plain);
            g2.setColor(menuColorFont1);
            g2.drawString(">", textX - 25, textY);
            if (commandNum_Buff == -1 || commandNum_Buff == -2) {
                g2.setColor(menuColorFont2);
                g2.setFont(font_text_bold);
                textContent = "Buffs provide temporary boosts to the player";
                drawTextBlock(g2, textContent, rectX, textContentY, rectWidth);
            }
        }

        textY += lineSpacing;
        //DRAW DEBUFF
        g2.setColor(menuColorFont1);
        g2.setFont(font_text_plain);
        g2.drawString("Debuff", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
        }
    }

    private void drawOptionScreen(int frameWidth, int frameHeight, int frameX, int frameY) {
        switch (subOptionState) {
            case 0:
                option_top(frameX, frameY);
                break;

            case 1:
                break;

            case 2:
                options_control(frameX, frameY);
                break;

            case 3:
                options_endGameConfirmation(frameX, frameY);
                break;
        }
        gp.keyHandler.enterPressed = false;
    }

    private void option_top(int frameX, int frameY) {
        int textX;
        int textY;
        // TITLE
        g2.setFont(font_title);
        String text = "CONTROL";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize / 2;
        g2.drawString(text, textX, textY + gp.tileSize * 3 / 4);
        g2.setFont(font_text_plain);

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
                subOptionState = 2;
                commandNum = 0;
            }
        }

        // END GAME
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subOptionState = 3;
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
        g2.setFont(font_title);
        textX = getCenterForElement(gp.tileSize * 6, g2.getFontMetrics().stringWidth(text));
        g2.drawString(text, frameX + textX, textY);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 33F));

        textX = frameX + gp.tileSize;
        int textX1 = textX + gp.tileSize * 3;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY);
        g2.drawString("W,A,S,D", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Placing boom", textX, textY);
        g2.drawString("Space", textX1, textY);

        textY += gp.tileSize;
        g2.drawString("Menu", textX, textY);
        g2.drawString("ESC", textX1, textY);

        // BACK
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 6;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subOptionState = 0;
                commandNum = 2;
            }
        }
    }

    private void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize;

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
                subOptionState = 0;
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
                subOptionState = 0;
                commandNum = 3;
            }
        }
    }

    private void drawGameOver(){
        g2.setColor(new Color(0.222f, 0.222f, 0.222f, 0.85f));
        g2.fill(screenArea);

        int x, y;
        String text;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
        g2.setColor(Color.red);
        text = "You Are Dead";
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        text = "Quit";
        x= getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
            g2.drawString(">", x - 40, y);

    }

    //FEATURE METHOD
    protected int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    protected void drawSubWindow1(int x, int y, int width, int height, Color backgroundColor, Color strokeColor, int strokeSize, int arc) {
        // Adjust coordinates and dimensions to account for the stroke width
        int adjustedX = x + strokeSize / 2;
        int adjustedY = y + strokeSize / 2;
        int adjustedWidth = width - strokeSize;
        int adjustedHeight = height - strokeSize;

        // Set the color for the background and draw the filled rounded rectangle
        g2.setColor(backgroundColor);
        g2.fillRoundRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, arc, arc);

        // Set the color and size for the stroke and draw the outline of the rounded rectangle
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(strokeSize));
        g2.drawRoundRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, arc, arc);
    }

    private void drawCharacterAnimation(int x, int y, int width, int height) {
        frameCounter++;

        if (frameCounter >= frameThreshold) {
            frameIndex = (frameIndex + 1) % playerFrames.length;
            frameCounter = 0;
        }
        g2.drawImage(playerFrames[frameIndex], x, y, width, height, null);
    }

    protected int center(String s, int imageX, int imageWidth) {
        int textWidth = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
        return imageX + (imageWidth - textWidth) / 2;
    }

    private int getCenterForElement(int parentWidth, int childWidth) {
        return (parentWidth - childWidth) / 2;
    }

    private void drawTextBlock(Graphics2D g2, String textContent, int rectX, int textContentY, int rectWidth) {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
        List<String> lines = splitTextIntoLines(g2, textContent, rectWidth - 40);
        int lineHeight = g2.getFontMetrics().getHeight();
        int yOffset = textContentY;

        for (String line : lines) {
            g2.drawString(line, rectX + 20, yOffset);
            yOffset += lineHeight;
        }
    }

    private List<String> splitTextIntoLines(Graphics2D g2, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        FontMetrics metrics = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine + (currentLine.length() == 0 ? "" : " ") + word;
            int testLineWidth = metrics.stringWidth(testLine);

            if (testLineWidth <= maxWidth) {
                currentLine.append((currentLine.length() == 0 ? "" : " ") + word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
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

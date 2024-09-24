package UI;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UI_mapSelection {
    GamePanel gp;
    private List<BufferedImage> mapList;
    private int selectedMapIndex = 0;
    private int scrollOffset = 0;

    //VARIABLE
    private int x, y;
    private final int size;
    private final int numberOfMapDisplayOnScreen = 1;
    private final int x_Map;
    private final int strokeWidth;

    //GRAPHICS
    private final Color shadow;

    public UI_mapSelection(GamePanel gp) {
        this.gp = gp;
        mapList = new ArrayList<BufferedImage>();
        mapList.add(setup("maps/map_1", 256, 256));
        mapList.add(setup("buff_debuff/add_boom", 256, 256));
        mapList.add(setup("buff_debuff/fire_spell", 256, 256));

        size = gp.tileSize * 5;
        x_Map = getCenter(gp.screenWidth, size);
        strokeWidth = 10;

        shadow = new Color(0, 0, 0, 50);
    }

    public void updateSelection(int direction) {
        selectedMapIndex += direction;

        //IF SELECT MAP OUT OF VISIBLE
        if (selectedMapIndex < scrollOffset) {
            scrollOffset--;
        } else if (selectedMapIndex >= scrollOffset + numberOfMapDisplayOnScreen) {
            scrollOffset++;
        }

        if (selectedMapIndex < 0) selectedMapIndex = mapList.size() - 1;
        if (scrollOffset < 0) scrollOffset = selectedMapIndex;
        if (selectedMapIndex >= mapList.size()) {
            selectedMapIndex = 0;
            scrollOffset = selectedMapIndex;
        }
    }

    public void draw(Graphics2D g2) {
        //BACKGROUND
        g2.setColor(new Color(0x809d49));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITTLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        g2.setColor(Color.white);
        String text = "Map Selection";
        x = getCenter(gp.screenWidth, (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth());
        y = gp.tileSize + 10;

        // SHADOW TEXT
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);

        // MAIN COLOR TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (gp.keyHandler.leftPressed) {
            updateSelection(-1);
            gp.keyHandler.leftPressed = false;
        }
        if (gp.keyHandler.rightPressed) {
            updateSelection(1);
            gp.keyHandler.rightPressed = false;
        }

        // DRAW MAPS
        for (int i = scrollOffset; i < Math.min(scrollOffset + numberOfMapDisplayOnScreen, mapList.size()); i++) {
            x = (i - scrollOffset) * 300 + x_Map;
            y = gp.tileSize * 2;
            if (i == selectedMapIndex) {
                g2.setColor(shadow);
                g2.fillRoundRect(x - strokeWidth - 20, y - strokeWidth - 20, size + 2 * strokeWidth + 40, size + 2 * strokeWidth + 40, 25, 25);

                g2.setColor(Color.black);
                g2.fillRoundRect(x - strokeWidth, y - strokeWidth, size + 2 * strokeWidth, size + 2 * strokeWidth, 25, 25);
                g2.drawImage(mapList.get(i), x, y, size, size, null);
            }
        }
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

    private int getCenter(int parentWidth, int childWidth) {
        return (parentWidth - childWidth) / 2;
    }

    public int getSelectedMapIndex(){
        return selectedMapIndex;
    }
}

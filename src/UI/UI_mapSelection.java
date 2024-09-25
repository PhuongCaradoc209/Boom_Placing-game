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
    private List<String> titleMap;
    private int selectedMapIndex = 0;
    private int scrollOffset = 0;

    //VARIABLE
    private int x, y;
    private int size;
    private final int numberOfMapDisplayOnScreen = 1;
    private final int x_Map;
    private final int strokeWidth;

    //GRAPHICS
    private final Color shadow;
    private final BufferedImage arrow_left;
    private final BufferedImage arrow_right;
    private BufferedImage temp_arrow_left;
    private BufferedImage temp_arrow_right;
    private float arrowHoverProgress = 0.0f; // Giá trị từ 0.0 đến 1.0
    private float hoverSpeed = 0.1f; // Tốc độ thay đổi

    public UI_mapSelection(GamePanel gp) {
        this.gp = gp;
        mapList = new ArrayList<BufferedImage>();
        mapList.add(setup("maps/map_1", 256, 256));
        mapList.add(setup("buff_debuff/add_boom", 256, 256));
        mapList.add(setup("buff_debuff/fire_spell", 256, 256));

        size = gp.tileSize * 5;
        x_Map = getCenter(gp.screenWidth, size);
        strokeWidth = 10;

        //GRAPHICS
        shadow = new Color(0, 0, 0, 50);
        arrow_left = setup("icon/arrow_left", 16, 16);
        arrow_right = setup("icon/arrow_right", 16, 16);
        temp_arrow_left = arrow_left;
        temp_arrow_right = arrow_right;
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
        g2.setColor(new Color(0x795757));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (gp.keyHandler.leftPressed) {
            updateSelection(-1);
            gp.keyHandler.leftPressed = false;
            arrowHoverProgress = 0.0f;
        }
        if (gp.keyHandler.rightPressed) {
            updateSelection(1);
            gp.keyHandler.rightPressed = false;
            arrowHoverProgress = 0.0f;
        }

        // Tăng dần giá trị arrowHoverProgress lên đến 1.0f
        if (arrowHoverProgress < 1.0f) {
            arrowHoverProgress += hoverSpeed;
            if (arrowHoverProgress > 1.0f) {
                arrowHoverProgress = 1.0f;
            }
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

        // TITTLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        g2.setColor(Color.white);
        String text = "Map " + (selectedMapIndex + 1);
        x = getCenter(gp.screenWidth, (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth());
        y = gp.tileSize + 20;

        // SHADOW TEXT
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);

        // MAIN COLOR TEXT
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // DRAW ARROWS
        if (selectedMapIndex != mapList.size() - 1) {
            drawArrowWithInterpolation(g2, temp_arrow_right, gp.screenWidth - gp.tileSize - 10, getCenter(gp.screenHeight, gp.tileSize), gp.tileSize, gp.tileSize, arrowHoverProgress);
        }
        if (selectedMapIndex != 0) {
            drawArrowWithInterpolation(g2, temp_arrow_left, 0, getCenter(gp.screenHeight, gp.tileSize), gp.tileSize, gp.tileSize, arrowHoverProgress);
        }

        temp_arrow_left = arrow_left;
        temp_arrow_right = arrow_right;
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

    public int getSelectedMapIndex() {
        return selectedMapIndex;
    }

    private void drawArrowWithInterpolation(Graphics2D g2, BufferedImage arrow, int x, int y, int width, int height, float progress) {
        int newWidth = (int) (width + (progress * 10));
        int newHeight = (int) (height + (progress * 10));

        // Thiết lập độ trong suốt
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, progress));

        // Vẽ mũi tên
        g2.drawImage(arrow, x, y, newWidth, newHeight, null);

        // Reset độ trong suốt
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}

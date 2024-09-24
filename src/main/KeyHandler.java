package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// KeyListener: the listener interface for receiving keyboard events (keystrokes)
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed,
            spacePressed, escapePressed, HPressed;
    public boolean AnnouceCompleteAnimation;
    // DEBUG
    boolean checkDrawTime = false;
    GamePanel gp;
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

//         TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleState(key);
        }

        if (gp.gameState == gp.mapSelectState) {
            mapSelectState(key);
        }
        if (gp.currentMap == 0) {
            // PLAY STATE
            if (gp.gameState == gp.playState) {
                gamePlayerState(key);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if (key == KeyEvent.VK_H) {
            HPressed = false;
        }
    }

    private void titleState(int key) {
        if (key == KeyEvent.VK_W) {
            gp.ui.commandNum--;
//            gp.playSoundEffect("select_sound", 6);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }
        if (key == KeyEvent.VK_S) {
            gp.ui.commandNum++;
//            gp.playSoundEffect("select_sound", 6);
            if (gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
        if (key == KeyEvent.VK_ENTER) {
//            gp.playSoundEffect("click_sound", 7);
            enterPressed = true;
        }
    }

    private void mapSelectState(int key) {
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (key == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    private void gamePlayerState(int key) {
        if (key == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
        if (key == KeyEvent.VK_H) {
            HPressed = true;
        }

        // DEBUG
        if (key == KeyEvent.VK_T) {
            checkDrawTime = (checkDrawTime == true) ? false : true;
        }
    }
}

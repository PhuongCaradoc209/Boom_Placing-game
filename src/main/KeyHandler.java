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
            // OPTIONS STATE
            else if (gp.gameState == gp.optionState) {
                optionState(key);
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
            gp.gameState = gp.optionState;
//            gp.playSoundEffect("Menu", 10);
//            gp.music.stop("Bird");
//            gp.music.stop("Background");
        }
        if (key == KeyEvent.VK_H) {
            HPressed = true;
        }

        // DEBUG
        if (key == KeyEvent.VK_T) {
            checkDrawTime = (checkDrawTime == true) ? false : true;
        }
    }

    public void optionState(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
//            gp.playSoundEffect("Menu", 10);
            gp.gameState = gp.playState;
            gp.ui.subState = 0;
        }
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = true;
//            gp.playSoundEffect("clickItem", 14);
        }
        int maxCommandNum;
        switch (gp.ui.subState) {
            case 0:
                maxCommandNum = 5;
                if (key == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = maxCommandNum;
                    }
                }
                if (key == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum > maxCommandNum) {
                        gp.ui.commandNum = 0;
                    }
                }
                if (key == KeyEvent.VK_A) {
                    if (gp.ui.subState == 0) {
//                        if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
//                            gp.music.volumeScale--;
//                            gp.music.checkVolume();
//                            gp.playSoundEffect("Menu_setting", 12);
//                        }
//                        if (gp.ui.commandNum == 2 && gp.soundEffect.volumeScale > 0) {
//                            gp.soundEffect.volumeScale--;
//                            gp.playSoundEffect("Menu_setting", 12);
//                        }
                    }
                }
                if (key == KeyEvent.VK_D) {
                    if (gp.ui.subState == 0) {
//                        if (gp.ui.commandNum == 1 && gp.music.volumeScale >= 0) {
//                            if (gp.music.volumeScale < 5) {
//                                gp.music.volumeScale++;
//                            }
//                            gp.music.checkVolume();
//                            gp.playSoundEffect("Menu_setting", 12);
//                        }
//                        if (gp.ui.commandNum == 2 && gp.soundEffect.volumeScale >= 0) {
//                            if (gp.soundEffect.volumeScale < 5) {
//                                gp.soundEffect.volumeScale++;
//                            }
//                            gp.playSoundEffect("Menu_setting", 12);
//                        }
                    }
                }
                break;
            case 3:
                maxCommandNum = 1;
                if (key == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum < 0) {
                        gp.ui.commandNum = maxCommandNum;
                    }
                }
                if (key == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum > maxCommandNum) {
                        gp.ui.commandNum = 0;
                    }
                }
        }
    }
}

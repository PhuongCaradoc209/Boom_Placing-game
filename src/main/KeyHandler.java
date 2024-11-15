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
            // MENU STATE
            else if (gp.gameState == gp.menuState) {
                menuState(key);
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
            gp.ui.commandNum_Title--;
//            gp.playSoundEffect("select_sound", 6);
            if (gp.ui.commandNum_Title < 0) {
                gp.ui.commandNum_Title = 2;
            }
        }
        if (key == KeyEvent.VK_S) {
            gp.ui.commandNum_Title++;
//            gp.playSoundEffect("select_sound", 6);
            if (gp.ui.commandNum_Title > 2) {
                gp.ui.commandNum_Title = 0;
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
            gp.gameState = gp.menuState;
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

    private void menuState(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
//            gp.playSoundEffect("Menu", 10);
            gp.gameState = gp.playState;
        }

        if (key == KeyEvent.VK_D) {
            rightPressed = true;
//            gp.playSoundEffect("clickItem", 14);
        }

        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }

        //CONTROL SUB STATE
        switch (gp.ui.subMenuState){
            case 0:
                //CONTROL COMMAND
                int maxCommandNum;
                maxCommandNum = 1;
                if (key == KeyEvent.VK_W) {
                    gp.ui.commandNum_Menu--;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum_Menu < 0) {
                        gp.ui.commandNum_Menu = maxCommandNum;
                    }
                }
                if (key == KeyEvent.VK_S) {
                    gp.ui.commandNum_Menu++;
//                    gp.playSoundEffect("Menu_Button", 11);
                    if (gp.ui.commandNum_Menu > maxCommandNum) {
                        gp.ui.commandNum_Menu = 0;
                    }
                }
                break;
            case 1:
                optionState(key);
                break;
            case 2:
                characterStatusState(key);
                break;
        }
    }

    private void characterStatusState(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
//            gp.playSoundEffect("Menu", 10);
            gp.gameState = gp.playState;
        }
        int maxCommandNum;
        maxCommandNum = 2;
        if (key == KeyEvent.VK_W) {
            gp.ui.commandNum--;
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
        int maxCommandNum_Buff = gp.player.ownBuffManager.buffs.size() - 1;

        if (gp.ui.commandNum == 1){
            if (key == KeyEvent.VK_A){
                gp.ui.commandNum_Buff--;
                if (gp.ui.commandNum_Buff < -2) {
                    gp.ui.commandNum_Buff = -2;
                }
            }
            if (key == KeyEvent.VK_D){
                if (gp.ui.commandNum_Buff == -2) gp.ui.commandNum_Buff++;
                gp.ui.commandNum_Buff++;
                if (gp.ui.commandNum_Buff > maxCommandNum_Buff) {
                    gp.ui.commandNum_Buff = -2;
                }
            }
        }
    }

    private void optionState(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
//            gp.playSoundEffect("Menu", 10);
            gp.gameState = gp.playState;
            gp.ui.subOptionState = 0;
        }
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = true;
//            gp.playSoundEffect("clickItem", 14);
        }
        int maxCommandNum;
        switch (gp.ui.subOptionState) {
            case 0:
                maxCommandNum = 4;
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
                    if (gp.ui.subOptionState == 0) {
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
                    if (gp.ui.subOptionState == 0) {
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

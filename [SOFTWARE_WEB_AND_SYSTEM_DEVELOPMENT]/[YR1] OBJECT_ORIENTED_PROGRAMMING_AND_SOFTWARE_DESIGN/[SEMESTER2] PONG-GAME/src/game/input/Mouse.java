package com.brawl_pong.game.input;

import com.brawl_pong.game.main.GameScreen;
import com.brawl_pong.game.misc.Player;
import com.brawl_pong.game.states.MainMenuPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.SQLException;

import static java.lang.System.exit;

public class Mouse implements MouseListener, MouseMotionListener {

    private GameScreen gameScreen;


    public Mouse(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int menu_x = e.getX();
        int menu_y = e.getY();

        if (gameScreen.state == GameScreen.GAMESTATES.MAIN_MENU) {
            if (menu_x >= 360 && menu_x <= 420 && menu_y >= 240 && menu_y <= 250) {
                gameScreen.state = GameScreen.GAMESTATES.GAME;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (menu_x >= 360 && menu_x <= 420 && menu_y >= 390 && menu_y <= 500) {
                System.exit(1);
            } else if (menu_x >= 370 && menu_x <= 405 && menu_y >= 370 && menu_y <= 380) {
                gameScreen.state = GameScreen.GAMESTATES.STATS;
            }
        }

        else if (gameScreen.state == GameScreen.GAMESTATES.YOULOSE) {
            if (menu_x >= 350 && menu_x <= 410 && menu_y >= 290 && menu_y <= 300)
            {
                gameScreen.state = GameScreen.GAMESTATES.GAME;
                gameScreen.resetEntireGame();

            }

            else if (menu_x >= 340 && menu_x <= 420 && menu_y >= 370 && menu_y <= 400) {
                gameScreen.state = GameScreen.GAMESTATES.MAIN_MENU;
            }

        }

        else if (gameScreen.state == GameScreen.GAMESTATES.YOUWIN) {
            if (menu_x >= 350 && menu_x <= 405 && menu_y >= 290 && menu_y <= 300) {
                gameScreen.state = GameScreen.GAMESTATES.GAME;
                gameScreen.resetEntireGame();

            } else if (menu_x >= 340 && menu_x <= 420 && menu_y >= 370 && menu_y <= 400) {
                gameScreen.state = GameScreen.GAMESTATES.MAIN_MENU;
            }

        }


        }


        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }

        @Override
        public void mouseDragged (MouseEvent e){
        }

        @Override
        public void mouseMoved (MouseEvent e)
        {
            gameScreen.changePlayerPos(e.getY());
        }
    }


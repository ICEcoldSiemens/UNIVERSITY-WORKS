package com.brawl_pong.game.main;

import java.util.Scanner;

public class Game implements Runnable {

    // The instance of the game window, game screen and game thread
    private GameWindow gameWindow;
    private GameScreen gameScreen;
    Thread game;


    // The Game constructor running the game window containing game screen, providing input to game screen
    public Game()
    {
        gameScreen = new GameScreen();
        gameScreen.requestFocus();
        gameWindow = new GameWindow(gameScreen);
        initThread();

    }

    // Initialization of the game thread
    private void initThread()
    {
        game = new Thread(this);
        game.start();
    }

    // Rendering the contents within game screen + Capping the frame rate around 120 FPS
    @Override
    public void run()
    {
        final double FPS = 120.0;
        double time_per_frame = 1000000000.0 / FPS;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {
            now = System.nanoTime();
            if (now - lastFrame >= time_per_frame) {

                gameScreen.repaint();
                lastFrame = now;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000)
            {
                System.currentTimeMillis();
                System.out.println("[SETTINGS] Frames Per Second: " + frames + " FPS");
                break;
            }
        }

    }

}

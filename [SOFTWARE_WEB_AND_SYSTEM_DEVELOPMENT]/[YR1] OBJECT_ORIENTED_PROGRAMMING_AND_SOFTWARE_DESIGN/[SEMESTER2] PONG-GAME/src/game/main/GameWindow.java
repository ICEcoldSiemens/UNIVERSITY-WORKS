package com.brawl_pong.game.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GameWindow extends JFrame {

    public GameWindow(GameScreen gameScreen)
    {
        this.setSize(800, 600);
        this.setTitle("BRAWL PONG: Pong Game");
        this.setBackground(Color.WHITE);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().add(gameScreen);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameScreen.updateGame();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                gameScreen.repaint();
            }
        });
        timer.start();

    }

}

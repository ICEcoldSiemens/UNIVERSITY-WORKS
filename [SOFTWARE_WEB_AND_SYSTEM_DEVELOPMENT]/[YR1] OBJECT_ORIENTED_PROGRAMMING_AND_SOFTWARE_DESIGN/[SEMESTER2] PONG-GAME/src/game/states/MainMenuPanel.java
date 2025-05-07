package com.brawl_pong.game.states;

import com.brawl_pong.game.main.Game;
import com.brawl_pong.game.main.GameScreen;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel
{



    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.white);
        g.drawString("BRAWL PONG", 360, 40);

        g2d.setColor(Color.white);
        g2d.drawString("PLAY GAME", 360, 250);
        g2d.drawString("STATISTICS", 360, 380);
        g2d.drawString("EXIT GAME", 360, 500);

    }
}

package com.brawl_pong.game.states;

import javax.swing.*;
import java.awt.*;

public class YouLosePanel
{
    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.white);
        g.drawString("GAME OVER: YOU LOSE", 330, 200);

        g2d.setColor(Color.white);
        g.drawString("Play Again?", 350, 300);
        g.drawString("Return To Menu", 340, 400);

    }
}

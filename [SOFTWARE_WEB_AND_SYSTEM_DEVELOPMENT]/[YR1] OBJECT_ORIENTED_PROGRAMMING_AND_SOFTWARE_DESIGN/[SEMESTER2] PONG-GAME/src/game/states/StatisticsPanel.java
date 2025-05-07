package com.brawl_pong.game.states;

import com.brawl_pong.game.misc.Database;

import java.awt.*;

public class StatisticsPanel
{

    private Database db = new Database();
    private String username = db.getPlayerUsername();
    private int wins = db.getWins();
    private int loss = db.getLosses();

    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.white);
        g.drawString(username + "'s" + " STATISTICS ", 360, 40);

        g2d.setColor(Color.white);
        g2d.drawString(username + "'s" + " WINS: " + wins, 360, 250);
        g2d.drawString(username + "'s" + " LOSSES: " + loss, 350, 380);

    }

}

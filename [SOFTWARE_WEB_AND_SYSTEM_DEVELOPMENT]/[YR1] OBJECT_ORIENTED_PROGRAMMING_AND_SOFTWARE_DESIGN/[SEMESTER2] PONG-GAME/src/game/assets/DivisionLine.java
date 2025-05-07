package com.brawl_pong.game.assets;

import java.awt.*;

public class DivisionLine
{
    private int x, y, width, height;
    private Color color;

    public DivisionLine(int x, int y, int width, int height, Color color)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}

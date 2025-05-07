package com.brawl_pong.game.assets;

import com.brawl_pong.game.main.GameScreen;

import java.awt.*;

public class Ball_Pong
{
    private int xPos, cx, cy, speed, size;
    private Color color;
    private int yPos;
    private final int MAXIMUM_SPEED = 20;

    public Ball_Pong(int xPos, int yPos, int cx, int cy, int speed, int size, Color c)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.cx = cx;
        this.cy = cy;
        this.speed = speed;
        this.size = size;
        this.color = c;

    }

    public void move()
    {
        xPos += cx;
        yPos += cy;
    }

    public void increaseSpeed()
    {
       if(speed < MAXIMUM_SPEED)
       {
           speed++;
           cx = (cx/Math.abs(cx)*speed);
           cy = (cy/Math.abs(cy)*speed);
       }
    }

    private void resetSpeed()
    {
        speed = 5;
        cx = (cx/Math.abs(cx)*speed);
        cy = (cy/Math.abs(cy)*speed);
    }

    public void resetBall()
    {
        resetSpeed();
        xPos = 400;
        yPos = 300;
    }

    public void bounce(int top, int bottom)
    {
       if(yPos > bottom - size)
       {
           reverseY();
       } else if (yPos < top)
       {
           reverseY();
       }
    }

    public int getSize()
    {
        return size;
    }

    public int getxPos()
    {
        return xPos;
    }

    public int getyPos()
    {
        return yPos;
    }

    public void reverseX()
    {
       cx *= -1;
    }

    private void reverseY()
    {
       cy *= -1;
    }

    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillOval(xPos, yPos, size, size);
    }

}

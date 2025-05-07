package com.brawl_pong.game.assets;

import java.awt.*;

public class Rect_Paddle {

    private static final int PADDLE_WIDTH = 15;
    private int x, y, height, ymove;
    private Color color = Color.DARK_GRAY;


    public Rect_Paddle(int x, int y, int height, int ymove, Color color)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        this.ymove = ymove;
        this.color = color;
    }

    public void changeMove(int moveY)
    {
        int centerY = y + (height / 2);

        if(centerY > moveY)
        {
            y -= ymove;
        }

        else if (centerY < moveY)
        {
         y += ymove;
        }
    }

    public boolean collision(Ball_Pong p)
    {
       int right = x + PADDLE_WIDTH;
       int bottom = y + height;

       if(p.getxPos() > (x-p.getSize()) && p.getxPos() < right)
       {
           if(p.getyPos() > y && p.getyPos() < bottom)
           {
               return true;
           }

       }
       return false;
    }



    public void setBounds(int screen_height)
    {
        if(y > screen_height-100)
        {
            y = screen_height-100;
        } else if (y < 10)
        {
            y = 5;
        }
    }

    public void draw(Graphics g)
    {
      g.setColor(color);
      g.fillRect(x,y,PADDLE_WIDTH,height);

    }


}

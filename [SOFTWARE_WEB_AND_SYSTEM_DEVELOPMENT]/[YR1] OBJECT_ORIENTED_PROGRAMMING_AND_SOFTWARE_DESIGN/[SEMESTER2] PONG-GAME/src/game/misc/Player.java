package com.brawl_pong.game.misc;

import java.awt.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Player
{
    private int playerScore;
    private int overallScore;
    private String playerName;
    private Color color;
    private Database db;


    public Player(String playerName)
    {
        db = new Database();
        db.getPlayerUsername();
        if(db.getPlayerUsername() == null)

        {
            System.out.println("[IN-GAME] What is your username: ");
            Scanner sc = new Scanner(System.in);
            playerName = sc.nextLine();

            db.setPlayerUsername(playerName);
        }

        this.playerName = playerName;
    }



    public int getPlayerScore()
    {
        return playerScore;
    }

    public void setPlayerScore(int playerScore)
    {
        this.playerScore = playerScore;
    }


    public Color getColor()
    {
        System.out.print("[IN-GAME] Which color do you want to use? " + "Red? Blue? White? Yellow? Green? ");
        Scanner sc = new Scanner(System.in);

        switch(sc.nextLine().toLowerCase())
        {
           case "red":
               color = Color.red;
               break;
               case "blue":
                   color = Color.blue;
                   break;
                   case "white":
                       color = Color.white;
                       break;
                       case "yellow":
                           color = Color.yellow;
                           break;
                           case "green":
                               color = Color.green;
                               break;

        }
        return color;
    }


    public void increaseScore()
    {
        playerScore++;
    }

}

package com.brawl_pong.game.main;

import com.brawl_pong.game.assets.Ball_Pong;
import com.brawl_pong.game.assets.DivisionLine;
import com.brawl_pong.game.assets.Rect_Paddle;
import com.brawl_pong.game.input.Mouse;
import com.brawl_pong.game.misc.Database;
import com.brawl_pong.game.misc.Player;
import com.brawl_pong.game.states.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class GameScreen extends JPanel
{

    private Database db = new Database();

    public enum GAMESTATES
    {
        MAIN_MENU, STATS, YOUWIN, YOULOSE, PAUSED, GAME;
    };

    public GAMESTATES state = GAMESTATES.MAIN_MENU;
    private MainMenuPanel main_main = new MainMenuPanel();
    private StatisticsPanel stats = new StatisticsPanel();
    private YouWinPanel youWin = new YouWinPanel();
    private YouLosePanel youLose = new YouLosePanel();


    // The Game screen width and height, player's and computer's score and player's gamername
    static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
    public int aiScore = 0;
    public int collisonScore;


    // KeyListener & MouseListener for input on the Game screen + y-coordinate of the mouse
    private final Mouse mouse = new Mouse(this);
    private int mouseY = 0;


    // Game objects for the paddles, the pong ball & the division line
    private Player player = new Player("PLAYER ONE");
    private Rect_Paddle paddleplayer = new Rect_Paddle(20,200,100,10,player.getColor());
    private Rect_Paddle ai_player = new Rect_Paddle(750,200,100,10,Color.white);
    private Ball_Pong ball = new Ball_Pong(400,300,5,5,5,10,Color.WHITE);
    private DivisionLine division_line = new DivisionLine(380,40,5,500,Color.WHITE);


    // The GameScreen constructor to allow keyboard and mouse input
    public GameScreen()
    {
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }


    // Game loop which contains the majority of the logic of the game including movement and collisions of game objects
    public void updateGame() throws SQLException {
        if(state == GAMESTATES.GAME)
        {
            collisonScore = 0;
            ball.move();
            ball.bounce(0, SCREEN_HEIGHT);
            ai_player.changeMove(ball.getyPos());
            ai_player.collision(ball);
            paddleplayer.collision(ball);
            ai_player.setBounds(SCREEN_HEIGHT);
            paddleplayer.changeMove(mouseY);
            paddleplayer.setBounds(SCREEN_HEIGHT);

            gameOver();

            if (ball.getxPos() < 0) {

                aiScore++;
                resetGame();

            } else if (ball.getxPos() > 790) {

                player.increaseScore();
                resetGame();
            }

            if (ai_player.collision(ball) || paddleplayer.collision(ball))
            {
                ball.reverseX();
                collisonScore++;
                ball.increaseSpeed();
            }
        }

    }



    private void gameOver()
    {

        if(player.getPlayerScore() == 5)
        {
            state = GAMESTATES.YOUWIN;
            db.incrementPlayerWins();

        } else if (aiScore == 5)
        {
            state = GAMESTATES.YOULOSE;
            db.incrementPlayerLosses();
        }
    }


    public void resetEntireGame()
    {
        player.setPlayerScore(0);
        aiScore = 0;
        collisonScore = 0;
    }


    private void resetGame()
    {
        if(state == GAMESTATES.GAME)
        {
            try {
                ball.resetBall();
                collisonScore = 0;
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void changePlayerPos(int y)
    {
        if(state == GAMESTATES.GAME)
        {
            mouseY = y;
            repaint();
        }
    }

    public void paintComponent(Graphics g)
    {
        // A black rectangle is drawn over the game screen and given the game screen's width and height
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if(state == GAMESTATES.GAME)
        {
            g.setColor(Color.WHITE);
            g.drawString(db.getPlayerUsername() + ": " + player.getPlayerScore() + " || " + "COMPUTER: " + aiScore, 300, 20);

            // The paddles and the ball is drawn on the game screen
            ball.draw(g);
            paddleplayer.draw(g);
            ai_player.draw(g);
            division_line.draw(g);
        }

        else if (state == GAMESTATES.MAIN_MENU)
        {
           main_main.draw(g);
        }

        else if (state == GAMESTATES.STATS)
        {
           stats.draw(g);

        }

        else if (state == GAMESTATES.YOUWIN)
        {
            youWin.draw(g);
        }

        else if (state == GAMESTATES.YOULOSE)
        {
            youLose.draw(g);
        }


    }


}

package com.brawl_pong.game.misc;

import javax.swing.*;
import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:brawl_pong.db");
            System.out.println("[SETTINGS] Connected to Database");
            createTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS Player (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserName TEXT NOT NULL," +
                "WINS INTEGER DEFAULT O, " +
                "LOSSES INTEGER DEFAULT 0 " + ")";

        statement.executeUpdate(query);
        statement.close();
    }


    public String getPlayerUsername()
    {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT UserName FROM Player LIMIT 1");
            String username = resultSet.getString("UserName");
            resultSet.close();
            statement.close();
            return username;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPlayerUsername(String username)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Player (UserName, WINS, LOSSES) VALUES (?,0,0)");
            statement.setString(1, username);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getWins()
    {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT WINS FROM Player WHERE UserName = ?");
            int wins = resultSet.getInt("WINS");
            resultSet.close();
            statement.close();
            return wins;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getLosses()
    {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LOSSES FROM Player WHERE UserName = ?");
            int losses = resultSet.getInt("LOSSES");
            resultSet.close();
            statement.close();
            return losses;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void incrementPlayerWins()
    {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Player SET WINS = WINS + 1 WHERE UserName = ?");
            statement.setString(1, getPlayerUsername());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void incrementPlayerLosses()
    {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Player SET LOSSES = LOSSES + 1 WHERE UserName = ");
            statement.setString(1,getPlayerUsername());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}



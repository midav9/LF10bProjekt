package de.jbg.memeapp.Memes;

import de.jbg.memeapp.SessionInitiator;     //DAO

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

public class MemeQuery extends SessionInitiator {

    private Connection connection = getConnection();

    //Konstruktor
    public MemeQuery() throws SQLException {
        super();
    }

    //getQuery
    public ArrayList<Meme> execGetQuery(String getSqlQuery) throws SQLException {
        ArrayList<Meme> resultArray = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(getSqlQuery);
        while (result.next()) {
            Meme meme = new Meme();
            meme.setMeme_ID(result.getInt("meme_ID"));
            meme.setPic(result.getBlob("pic"));
            meme.setDate(result.getDate("date"));
            meme.setHeight(result.getInt("height"));
            meme.setLength(result.getInt("length"));
            meme.setSize(result.getInt("size"));
            //Fremdschlüssel bzw. sind auch NULL-Werte ok?, Attribute noch nicht definiert in Meme
            resultArray.add(meme);
        }
        return resultArray;
    }

    //InsertQuery
    InputStream inputStream;
    public void execInsertWithBlob(String insertSqlQuery, InputStream blobStream, java.sql.Date date, int height, int length, int size, int category, int tag) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(insertSqlQuery)) {
            stmt.setBlob(1, blobStream);
            stmt.setDate(2, date);
            stmt.setInt(3, height);
            stmt.setInt(4, length);
            stmt.setInt(5, size);
            stmt.setInt(6, category);
            stmt.setInt(7, tag);

            stmt.executeUpdate();
            System.out.println("Bild gespeichert");

        }
    }

    //Update & Delete Query
    public void execSomeQuery(String someSqlQuery) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(someSqlQuery)) {
            stmt.executeUpdate();
            System.out.println("Aktion ausgeführt");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}

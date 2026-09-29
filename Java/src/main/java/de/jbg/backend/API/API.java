/*
package de.jbg.memeapp.API;

import java.io.*;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.jbg.memeapp.Memes.Meme;
import de.jbg.memeapp.Memes.MemeQuery;
import org.mariadb.jdbc.MariaDbBlob;

public class API {

    public static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String httpMethod = exchange.getRequestMethod();
            InputStream inputStream;
            OutputStream outputStream;
            MemeQuery memeStatement;
            String requestURI;
            String[] parts;
            String memeID;

            switch (httpMethod) {
                case "GET":
                    requestURI = String.valueOf(exchange.getRequestURI());
                    parts = requestURI.split("/");
                    memeID = parts[parts.length-1];
                    //getBlobOfOneMeme
                    Blob response = new MariaDbBlob();   //API-Response
                    String getSqlQuery = "Select * FROM memes WHERE meme_ID=" + memeID;
                    /* not useful anymore, we just need one picture
                    if (!(parts.length == 4)) {
                        getSqlQuery = "Select * FROM memes";
                    }

                    try {
                        MemeQuery memeQuery = new MemeQuery();
                        ArrayList<Meme> memes = memeQuery.execGetQuery(getSqlQuery);
                        for (Meme meme : memes) {
                            response = meme.getPic();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    //send response
                    try {
                        exchange.sendResponseHeaders(200, response.length());
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                    outputStream = exchange.getResponseBody();
                    try {
                        outputStream.write(response.getBinaryStream().readAllBytes());
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                    outputStream.close();
                    break;

                case "POST":
                    inputStream = exchange.getRequestBody();
                    //maybe make input dynamic based on the file (https://github.com/haraldk/TwelveMonkeys)
                    String setSqlQuery = "INSERT INTO memes (pic, date, height, length, size, category, tag) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try {
                        memeStatement = new MemeQuery();
                        memeStatement.execInsertWithBlob(setSqlQuery,
                                inputStream,
                                Date.valueOf(LocalDate.now()),
                                100, 100, 32, 1, 1
                        );
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    //send some response
                    exchange.sendResponseHeaders(200, exchange.getResponseCode());
                    inputStream.close();
                    break;

                case "PUT":
                    inputStream = exchange.getRequestBody();
                    String inputString = new String(inputStream.readAllBytes());
                    requestURI = String.valueOf(exchange.getRequestURI());
                    parts = requestURI.split("/");
                    memeID = parts[parts.length-1];
                    //maybe make input dynamic based on the file (https://github.com/haraldk/TwelveMonkeys)
                    String updateSqlQuery = "UPDATE memes SET DATE = '"+inputString+"' WHERE meme_ID = "+memeID;
                    try {
                        memeStatement = new MemeQuery();
                        memeStatement.execSomeQuery(updateSqlQuery);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //send some response
                    exchange.sendResponseHeaders(200, exchange.getResponseCode());
                    inputStream.close();
                    break;

                case "DELETE":
                    requestURI = String.valueOf(exchange.getRequestURI());
                    parts = requestURI.split("/");
                    memeID = parts[parts.length-1];
                    //maybe make input dynamic based on the file (https://github.com/haraldk/TwelveMonkeys)
                    String deleteSqlQuery = "DELETE FROM memes WHERE meme_ID = "+memeID;
                    try {
                        memeStatement = new MemeQuery();
                        memeStatement.execSomeQuery(deleteSqlQuery);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //send some response
                    exchange.sendResponseHeaders(200, exchange.getResponseCode());
                    break;
            }

        }
    }
}
*/
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
import de.jbg.memeapp.tag.Tag;
import de.jbg.memeapp.tag.TagQuery;

public class tagAPI extends generalAPI implements HttpHandler {

    TagQuery tagStatement;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String httpMethod = exchange.getRequestMethod(); //hier da sonst NullPointerException

        switch (httpMethod) {
            case "GET":
                //getBlobOfOneMeme
                String response = null;   //API-Response
                String getSqlQuery = "Select * FROM tag WHERE tag_ID=" + getRequestID(exchange);
                try {
                    TagQuery tagQuery = new TagQuery();
                    ArrayList<Tag> tags = tagQuery.execGetQuery(getSqlQuery);
                    for (Tag tag : tags) {
                        response = tag.getName();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                exchange.sendResponseHeaders(200, response.length()); //length needed for image reader in UI
                outputStream = exchange.getResponseBody();
                try {
                    outputStream.write(response);
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
                    tagStatement = new MemeQuery();
                    tagStatement.execInsertWithBlob(setSqlQuery,
                            inputStream,
                            Date.valueOf(LocalDate.now()),
                            100, 100, 32, 1, 1
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                inputStream.close();
                sendSome200Response(exchange); //just as good behaviour
                break;



            case "PUT":
                inputStream = exchange.getRequestBody();
                String inputString = new String(inputStream.readAllBytes());
                String updateSqlQuery = "UPDATE memes SET DATE = '" + inputString + "' WHERE meme_ID = " + getRequestID(exchange);
                try {
                    tagStatement = new MemeQuery();
                    tagStatement.execSomeQuery(updateSqlQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                inputStream.close();
                sendSome200Response(exchange); //just as good behaviour
                break;

            case "DELETE":
                String deleteSqlQuery = "DELETE FROM memes WHERE meme_ID = " + getRequestID(exchange);
                try {
                    tagStatement = new MemeQuery();
                    tagStatement.execSomeQuery(deleteSqlQuery);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sendSome200Response(exchange); //just as good behaviour
                break;

        }

    }
}

 */
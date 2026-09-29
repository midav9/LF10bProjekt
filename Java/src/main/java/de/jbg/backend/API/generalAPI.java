package de.jbg.memeapp.API;

import java.io.*;

import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;

@Getter
public abstract class generalAPI {

    InputStream inputStream;
    OutputStream outputStream;

    public String getRequestID(HttpExchange exchange) {
        String requestURI = String.valueOf(exchange.getRequestURI());
        String[] parts = requestURI.split("/");
        String ID = parts[parts.length - 1];

        return ID;
    }


    public void sendSome200Response(HttpExchange exchange) throws IOException {
        try {
            exchange.sendResponseHeaders(200, exchange.getResponseCode());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
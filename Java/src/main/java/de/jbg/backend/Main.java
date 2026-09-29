package de.jbg.memeapp;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import de.jbg.memeapp.API.memeAPI;

import java.io.*;


public class Main {

    public static void main(String[] args) {

        //start API / Webserver (HTTP)
        HttpServer serverMeme = null;
        try {
            serverMeme = HttpServer.create(new InetSocketAddress(8000), 0);
            serverMeme.createContext("/api/memes", new memeAPI()); //this could be an abstract handler
            serverMeme.setExecutor(null); // creates a default executor
            serverMeme.start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

/*
        HttpServer serverTag = null;
        try {
            serverTag = HttpServer.create(new InetSocketAddress(8000), 0);
            serverTag.createContext("/api/tag", new tagAPI()); //this could be an abstract handler
            serverTag.setExecutor(null); // creates a default executor
            serverTag.start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        HttpServer serverCategory = null;
        try {
            serverCategory = HttpServer.create(new InetSocketAddress(8000), 0);
            serverCategory.createContext("/api/category", new categoryAPI()); //this could be an abstract handler
            serverCategory.setExecutor(null); // creates a default executor
            serverCategory.start();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
         */

    }
}
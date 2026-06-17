package com.myapp;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            // Tell Javalin to serve files from the 'public' resource folder
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(8080);
    }
}

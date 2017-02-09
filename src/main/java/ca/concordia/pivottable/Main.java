package ca.concordia.pivottable;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Application running at http://localhost:4567.");

        staticFiles.location("/ui/build");

        get("/hello", (req, res) -> {
            return "Hello, World!";
        } );

        get("/stop", (req, res) -> {
            stop();
            return "";
        });
    }
}

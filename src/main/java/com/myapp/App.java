package com.myapp;

import com.myapp.controller.MealController;
import com.myapp.db.DatabaseManager;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) throws Exception {

        DatabaseManager.initDatabase();

        MealController controller = new MealController();

        Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.routes.apiBuilder(() -> {
                path("/api/meals", () -> {
                    get(controller::getAllMeals);
                    post(controller::addMeal);
                    get("/random", controller::getRandomMeal);
                    delete("/{id}", controller::deleteMeal);
                    patch("/{id}/rating", controller::updateRating);
                    patch("/{id}/description", controller::updateDescription);
                });
            });
        }).start(8080);
    }
}

package com.myapp;

import com.myapp.controller.DiaryController;
import com.myapp.controller.MealController;
import com.myapp.db.DatabaseManager;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) throws Exception {

        DatabaseManager.initDatabase();

	DiaryController diaryController = new DiaryController();
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

		path("/api/diary", () -> {
            	    get("/{year}/months", diaryController::getMonths);
           	    get("/{year}/{month}/days", diaryController::getDays);
            	    get("/{year}/{month}/{day}", diaryController::getEntry);
        	});
            });

        }).start(8080);
    }
}

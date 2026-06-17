package com.myapp.controller;

import com.myapp.model.Meal;
import com.myapp.repository.MealRepository;
import io.javalin.http.Context;

import java.util.List;

public class MealController {

    private final MealRepository repo = new MealRepository();

    public void getAllMeals(Context ctx) throws Exception {
        List<Meal> meals = repo.getAllMeals();
        ctx.json(meals);
    }

    public void getRandomMeal(Context ctx) throws Exception {
        Meal meal = repo.getRandomMeal();
        if (meal == null) {
            ctx.status(404).result("No meals found");
        } else {
            ctx.json(meal);
        }
    }

    public void addMeal(Context ctx) throws Exception {
        String name        = ctx.formParam("name");
        String category    = ctx.formParam("category");
        String description = ctx.formParam("description");

        if (name == null || name.isBlank()) {
            ctx.status(400).result("Name is required");
            return;
        }

        repo.addMeal(name, category, description);
        ctx.status(201).result("Meal added");
    }

    public void deleteMeal(Context ctx) throws Exception {
        int id = Integer.parseInt(ctx.pathParam("id"));
        repo.deleteMeal(id);
        ctx.result("Meal deleted");
    }

    public void updateRating(Context ctx) throws Exception {
        int id          = Integer.parseInt(ctx.pathParam("id"));
        double rating   = Double.parseDouble(ctx.formParam("rating"));
        repo.updateRating(id, rating);
        ctx.result("Rating updated");
    }

    public void updateDescription(Context ctx) throws Exception {
        int id             = Integer.parseInt(ctx.pathParam("id"));
        String description = ctx.formParam("description");
        repo.updateDescription(id, description);
        ctx.result("Description updated");
    }
}

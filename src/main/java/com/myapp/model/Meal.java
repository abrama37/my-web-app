package com.myapp.model;

public class Meal {
    public int id;
    public String name;
    public String category;
    public String description;
    public double rating;
    public String createdAt;

    public Meal() {}

    public Meal(int id, String name, String category, String description, double rating, String createdAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.rating = rating;
        this.createdAt = createdAt;
    }
}

package com.example.user.mit1pokusaj.models;

public class Category {
    private String name;
    private int catImage;

    public Category(){}
    public Category(String name, int catImage) {
        this.name = name;
        this.catImage = catImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCatImage() {
        return catImage;
    }

    public void setCatImage(int catImage) {
        this.catImage = catImage;
    }
}

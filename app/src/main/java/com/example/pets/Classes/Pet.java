package com.example.pets.Classes;

public class Pet {

    String name;
    String breed;
    String description;
    String dateAdded;
    String healthDesc;


    public Pet(String name, String breed, String description, String dateAdded, String healthDesc) {
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.dateAdded = dateAdded;
        this.healthDesc = healthDesc;
    }

    public Pet(String name, String breed, String dateAdded) {
        this.name = name;
        this.breed = breed;
        this.dateAdded = dateAdded;
    }

    public Pet() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getHealthDesc() {
        return healthDesc;
    }

    public void setHealthDesc(String healthDesc) {
        this.healthDesc = healthDesc;
    }
}

package com.openclassrooms.realestatemanager.model.entity;

public class SearchCriteria {
    public double minPrice;
    public double maxPrice;
    public double minSurface;
    public double maxSurface;
    public int minRooms;
    public int minPhotos;
    public String location;

    public SearchCriteria(double minPrice, double maxPrice, double minSurface, double maxSurface,
                          int minRooms, int minPhotos, String location) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minSurface = minSurface;
        this.maxSurface = maxSurface;
        this.minRooms = minRooms;
        this.minPhotos = minPhotos;
        this.location = location;
    }
}
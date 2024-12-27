package com.openclassrooms.realestatemanager.model.entity;

import java.util.List;

public class OverpassResponse {

    public List<Element> elements;

    public static class Element {
        public String type;
        public long id;
        public double lat;
        public double lon;
    }
}
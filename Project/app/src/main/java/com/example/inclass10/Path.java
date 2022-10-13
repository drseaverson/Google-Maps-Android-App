package com.example.inclass10;

import java.util.List;

public class Path {
    List<Point> points;
    String title;

    public Path(List<Point> points, String title) {
        this.points = points;
        this.title = title;
    }

    public Path() {}

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

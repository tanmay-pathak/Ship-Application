package com.example.ships_application;

/**
 * Class to represent a rectangle that would be used as a rubber-band selection. Rectangle is drawn based on left,
 * top, width and height.
 */
public class RubberBandRectangle {
    /*
        Points stored for the rectangle.
     */
    double left, top, width, height;

    /**
     * Default constructor that creates a rectangle at given x,y coordinates of width and height of 0.
     *
     * @param x : x coordinate to start the rectangle
     * @param y : y coordinate to start the rectangle
     */
    public RubberBandRectangle(double x, double y) {
        left = x;
        top = y;
        width = 0;
        height = 0;
    }

    /**
     * Method to resize the rectangle.
     *
     * @param prevX : New starting left point of the rectangle.
     * @param prevY : New starting top point of the rectangle.
     * @param x     : New width
     * @param y     : New height
     */
    public void resize(double prevX, double prevY, double x, double y) {
        this.left = Math.min(prevX, x);
        this.top = Math.min(prevY, y);
        this.width = Math.abs(prevX - x);
        this.height = Math.abs(prevY - y);
    }
}

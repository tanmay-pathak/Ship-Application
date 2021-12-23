package com.example.ships_application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Class to represent a Ship. Supports move, rotate and creation of a ship. Also provides a method to check if a point
 * exists within the ship. Implements methods from the Groupable interface to allow ships to be made into a group.
 */
public class Ship implements Groupable {
    /*
        Instance variables to store the relevant information needed to draw a ship.
     */
    double translateX, translateY;
    double[] xs = {0, 20, 0, -20, 0};
    double[] ys = {24, -20, -12, -20, 24};
    double shipWidth, shipHeight;
    double[] displayXs, displayYs;
    double left, top, right, bottom;
    WritableImage buffer;
    PixelReader reader;
    double clickX, clickY;

    /**
     * Default constructor of this class. Creates a ship object with the given coordinates.
     *
     * @param newX : x coordinate
     * @param newY : y coordinate
     */
    public Ship(double newX, double newY) {
        Canvas shipCanvas;
        GraphicsContext gc;

        translateX = newX;
        translateY = newY;
        double minVal = DoubleStream.of(xs).min().getAsDouble();
        double maxVal = DoubleStream.of(xs).max().getAsDouble();
        shipWidth = maxVal - minVal;
        minVal = DoubleStream.of(ys).min().getAsDouble();
        maxVal = DoubleStream.of(ys).max().getAsDouble();
        shipHeight = maxVal - minVal;
        displayXs = new double[xs.length];
        displayYs = new double[ys.length];
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + shipWidth / 2;
            displayYs[i] = ys[i] + shipHeight / 2;
        }

        shipCanvas = new Canvas(shipWidth, shipHeight);
        gc = shipCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillPolygon(displayXs, displayYs, displayXs.length);
        buffer = shipCanvas.snapshot(null, null);
        reader = buffer.getPixelReader();

        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + translateX;
            displayYs[i] = ys[i] + translateY;
        }
        recalculateBounds();
    }

    /**
     * Helper method to move the ship to a new point.
     *
     * @param dx : Distance to move x coordinate by
     * @param dy : Distance to move y coordinate by
     */
    private void moveShip(double dx, double dy) {
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] += dx;
            displayYs[i] += dy;
        }
        translateX += dx;
        translateY += dy;
        recalculateBounds();
    }

    /**
     * Method to check if the class is a leaf node or not. A ship is a leaf node.
     *
     * @return : false because ship is always the leaf node
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /**
     * Method to get the list of children if not a leaf node.
     *
     * @return : null because ship is the leaf node
     */
    @Override
    public ArrayList<Groupable> getChildren() {
        return null;
    }

    /**
     * Method to check if a point exists within the ship.
     *
     * @param x : x coordinate of the point to check
     * @param y : y coordinate of the point to check
     * @return : true if point is inside ship else false
     */
    @Override
    public boolean contains(double x, double y) {
        clickX = x - translateX + shipWidth / 2;
        clickY = y - translateY + shipHeight / 2;
        // check bounding box first, then bitmap
        boolean inside = false;
        if (clickX >= 0 && clickX <= shipWidth && clickY >= 0 && clickY <= shipHeight) {
            if (reader.getColor((int) clickX, (int) clickY).equals(Color.BLACK)) inside = true;
        }
        return inside;
    }

    /**
     * Method to move the ship by a certain distance.
     *
     * @param dX : distance to move on x coordinate
     * @param dY : distance to move on y coordinate
     */
    @Override
    public void move(double dX, double dY) {
        moveShip(dX, dY);
    }

    /**
     * Method to get the left x coordinate of the bounding box.
     *
     * @return : left x coordinate of the bounding box
     */
    @Override
    public double getLeft() {
        return left;
    }

    /**
     * Method to get the left y coordinate of the bounding box.
     *
     * @return : left y coordinate of the bounding box
     */
    @Override
    public double getTop() {
        return top;
    }

    /**
     * Method to get right x coordinate of the bounding box.
     *
     * @return : right x coordinate
     */
    @Override
    public double getRight() {
        return right;
    }

    /**
     * Method to get right y coordinate of the bounding box.
     *
     * @return : right y coordinate
     */
    @Override
    public double getBottom() {
        return bottom;
    }

    /**
     * Method to check if the ship is within the rubber-band rectangle selection.
     *
     * @param x1 : left x coordinate of the rectangle
     * @param y1 : left y coordinate of the rectangle
     * @param x2 : right x coordinate of the rectangle
     * @param y2 : right y coordinate of the rectangle
     * @return : true if all points of the group are within the rectangle else false
     */
    @Override
    public boolean isContained(double x1, double y1, double x2, double y2) {
        return IntStream.range(0, displayXs.length).noneMatch(i -> (displayXs[i] < x1) || (displayXs[i] > x2) || (displayYs[i] < y1) || (displayYs[i] > y2));
    }

    /**
     * Method to get an array of x coordinates for a ship.
     *
     * @return : array of x coordinates to draw
     */
    public double[] getDisplayXs() {
        return displayXs;
    }

    /**
     * Method to set new display x coordinates for the ship.
     *
     * @param displayXs : x coordinates
     */
    public void setDisplayXs(double[] displayXs) {
        this.displayXs = displayXs;
    }

    /**
     * Method to get an array of y coordinates for a ship.
     *
     * @return : array of y coordinate to draw
     */
    public double[] getDisplayYs() {
        return displayYs;
    }

    /**
     * Method to set new display y coordinates for the ship.
     *
     * @param displayYs : y coordinates
     */
    public void setDisplayYs(double[] displayYs) {
        this.displayYs = displayYs;
    }

    /**
     * Method to get how many coordinates are saved to draw a ship.
     *
     * @return : number of coordinates
     */
    public int displayLength() {
        return displayXs.length;
    }

    /**
     * Method to return a deep copy of the ship.
     *
     * @return : deep copy
     */
    @Override
    public Groupable duplicate() {
        Ship deepCopy = new Ship(translateX, translateY);
        deepCopy.setDisplayXs(this.displayXs.clone());
        deepCopy.setDisplayYs(this.displayYs.clone());
        deepCopy.recalculateBounds();
        return deepCopy;
    }

    /**
     * Helper method to calculate bounding box coordinates for the ship.
     */
    private void recalculateBounds() {
        left = DoubleStream.of(displayXs).min().getAsDouble();
        right = DoubleStream.of(displayXs).max().getAsDouble();
        top = DoubleStream.of(displayYs).min().getAsDouble();
        bottom = DoubleStream.of(displayYs).max().getAsDouble();
    }
}

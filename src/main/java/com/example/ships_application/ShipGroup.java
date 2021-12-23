package com.example.ships_application;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class to represent a group of ships.
 */
public class ShipGroup implements Groupable {
    /*
     *  Instance variables to store the coordinates for the bounding box and store ships in the group.
     */
    double left, top, right, bottom;
    ArrayList<Groupable> ships;

    /**
     * Default constructor for this class.
     */
    public ShipGroup() {
        ships = new ArrayList<>();
    }

    /**
     * Method to get the ships in this group.
     *
     * @return : ships in the group
     */
    public ArrayList<Groupable> getShips() {
        return ships;
    }

    /**
     * Method to set a new list of ships/groups as the group.
     *
     * @param ships : new group
     */
    public void setShips(ArrayList<Groupable> ships) {
        this.ships = ships;
    }

    /**
     * Method to check if the class is a leaf node or not.
     *
     * @return : true because group would always have at least one child
     */
    @Override
    public boolean hasChildren() {
        return true;
    }

    /**
     * Method to get the list of children.
     *
     * @return : List of children in the group
     */
    @Override
    public ArrayList<Groupable> getChildren() {
        return getShips();
    }

    /**
     * Method to add a child.
     *
     * @param c : child to be added
     */
    public void addChild(Groupable c) {
        ships.add(c);
        recalculateBounds();
    }

    /**
     * Method to check if a set of point is within the group or not.
     *
     * @param x : x coordinate to check
     * @param y : y coordinate to check
     * @return : true if point is within else false
     */
    @Override
    public boolean contains(double x, double y) {
        return ships.stream().anyMatch(s -> s.contains(x, y));
    }

    /**
     * Method to move each child of the group by certain distance.
     *
     * @param dX : distance to move on x coordinate
     * @param dY : distance to move on y coordinate
     */
    @Override
    public void move(double dX, double dY) {
        ships.forEach(c -> c.move(dX, dY));
        recalculateBounds();
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
     * @return : width
     */
    @Override
    public double getRight() {
        return right;
    }

    /**
     * Method to get right y coordinate of the bounding box.
     *
     * @return : height
     */
    @Override
    public double getBottom() {
        return bottom;
    }

    /**
     * Method to check if the group is within the rubber-band rectangle selection.
     *
     * @param x1 : left x coordinate of the rectangle
     * @param y1 : left y coordinate of the rectangle
     * @param x2 : right x coordinate of the rectangle
     * @param y2 : right y coordinate of the rectangle
     * @return : true if all points of the group are within the rectangle else false
     */
    @Override
    public boolean isContained(double x1, double y1, double x2, double y2) {
        return ships.stream().allMatch(s -> s.isContained(x1, y1, x2, y2));
    }

    /**
     * Method to return a deep copy of the group.
     *
     * @return : deep copy of the ships/group within this group
     */
    @Override
    public Groupable duplicate() {
        ShipGroup copyOfGroup = new ShipGroup();

        // deep copy each ship/group within this group
        ArrayList<Groupable> copyOfShips = ships.stream().map(Groupable::duplicate).collect(Collectors.toCollection(ArrayList::new));
        copyOfGroup.setShips(copyOfShips);
        copyOfGroup.recalculateBounds();
        return copyOfGroup;
    }

    /**
     * Helper method to calculate bounding box coordinates based on children's coordinates.
     */
    private void recalculateBounds() {
        left = ships.stream().mapToDouble(Groupable::getLeft).min().getAsDouble();
        right = ships.stream().mapToDouble(Groupable::getRight).max().getAsDouble();
        top = ships.stream().mapToDouble(Groupable::getTop).min().getAsDouble();
        bottom = ships.stream().mapToDouble(Groupable::getBottom).max().getAsDouble();
    }
}

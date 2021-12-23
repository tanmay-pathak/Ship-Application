package com.example.ships_application;

import java.util.ArrayList;

/**
 * Interface class to allow grouping of ships. Has methods to support various operations on a group.
 */
public interface Groupable {
    /**
     * Method to check if the class is a leaf node or not.
     *
     * @return : true if not a leaf node else false
     */
    boolean hasChildren();

    /**
     * Method to get the list of children if not a leaf node.
     *
     * @return : List of children in the group
     */
    ArrayList<Groupable> getChildren();

    /**
     * Method to check if a set of point is within the ship/group or not.
     *
     * @param x : x coordinate to check
     * @param y : y coordinate to check
     * @return : true if point is within else false
     */
    boolean contains(double x, double y);

    /**
     * Method to move each child of the group or a single ship by certain distance.
     *
     * @param dX : distance to move on x coordinate
     * @param dY : distance to move on y coordinate
     */
    void move(double dX, double dY);

    /**
     * Method to get the left x coordinate of the bounding box.
     *
     * @return : left x coordinate of the bounding box
     */
    double getLeft();

    /**
     * Method to get the left y coordinate of the bounding box.
     *
     * @return : left y coordinate of the bounding box
     */
    double getTop();

    /**
     * Method to get right x coordinate of the bounding box.
     *
     * @return : width
     */
    double getRight();

    /**
     * Method to get right y coordinate of the bounding box.
     *
     * @return : height
     */
    double getBottom();

    /**
     * Method to check if the ship/group is fully inside the rubber-band rectangle selection.
     *
     * @param x1 : left x coordinate of the rectangle
     * @param y1 : left y coordinate of the rectangle
     * @param x2 : right x coordinate of the rectangle
     * @param y2 : right y coordinate of the rectangle
     * @return : true if all ships of the group or all points of a ship is within the rectangle else false
     */
    boolean isContained(double x1, double y1, double x2, double y2);

    /**
     * Method to return a deep copy of the groupable object.
     *
     * @return : deep copy
     */
    Groupable duplicate();
}

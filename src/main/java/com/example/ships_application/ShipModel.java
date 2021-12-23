package com.example.ships_application;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Model for this MVC Architecture. Stores a list of ships/group of ships.
 */
public class ShipModel {
    /*
        Instance variables to store the ships and subscribers of the model.
     */
    public ArrayList<Groupable> ships;
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Groupable> tempCollection;

    /**
     * Default constructor of this class.
     */
    public ShipModel() {
        subscribers = new ArrayList<>();
        ships = new ArrayList<>();
        tempCollection = new ArrayList<>();
    }

    /**
     * Method to create a new ship which is to be stored inside model.
     *
     * @param x : x coordinate for the ship to be created at
     * @param y : y coordinate for the ship to be created at
     * @return : newly created ship object
     */
    public Ship createShip(double x, double y) {
        Ship s = new Ship(x, y);
        ships.add(s);
        notifySubscribers();
        return s;
    }

    /**
     * Method to get the list of ships stored within the model.
     *
     * @return : data structure that stores all the ships
     */
    public ArrayList<Groupable> getShips() {
        return ships;
    }

    /**
     * Method to check if a point was on a ship.
     *
     * @param x : x coordinate of the mouse click
     * @param y : y coordinate of the mouse click
     * @return : If the point was within a ship then return the shape else not.
     */
    public Optional<Groupable> detectHit(double x, double y) {
        return ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    /**
     * Method to move ships and group of ships.
     *
     * @param ships : List of ships to be moved
     * @param dX    : Distance to move x coordinate by
     * @param dY    : Distance to move y coordinate by
     */
    public void move(ArrayList<Groupable> ships, double dX, double dY) {
        ships.forEach(s -> s.move(dX, dY));
        notifySubscribers();
    }

    /**
     * Method to add a subscriber to be notified of model changes.
     *
     * @param aSub : new subscriber
     */
    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    /**
     * Method to notify all the subscribers of model changes.
     */
    private void notifySubscribers() {
        subscribers.forEach(ShipModelSubscriber::modelChanged);
    }

    /**
     * Method to return all ships that are within the given rectangle for rubber-band selection.
     *
     * @param rubberBand : rectangle to check against
     * @return : list of ships that are within the rectangle
     */
    public ArrayList<Groupable> detectRubberBandHit(RubberBandRectangle rubberBand) {
        // calculate x, y coordinate of the rectangle (left and right)
        double x1, y1, x2, y2;
        x1 = rubberBand.left;
        y1 = rubberBand.top;
        x2 = x1 + rubberBand.width;
        y2 = y1 + rubberBand.height;
        // add every ship that is within the rectangle
        ships.stream().filter(s -> s.isContained(x1, y1, x2, y2)).forEach(s -> tempCollection.add(s));
        return tempCollection;
    }

    /**
     * Method to clear the temporary collection used for holding the selected shapes when checking for rubber-band hit.
     */
    public void clearSelection() {
        tempCollection = new ArrayList<>();
    }

    /**
     * Method to create a new group and add it to the list of ships.
     *
     * @param selectedShips : list of ships to be converted into a group
     * @return : newly created group
     */
    public Groupable createGroup(ArrayList<Groupable> selectedShips) {
        ShipGroup group = new ShipGroup();
        selectedShips.forEach(s -> {
            group.addChild(s);
            ships.remove(s);
        });
        ships.add(group);
        notifySubscribers();
        return group;
    }

    /**
     * Method to ungroup a group and add its items individually to the model.
     *
     * @param group : group to be ungrouped
     * @return : list of items that were ungrouped
     */
    public ArrayList<Groupable> ungroup(Groupable group) {
        ArrayList<Groupable> individualItems = new ArrayList<>();
        ships.remove(group);
        group.getChildren().forEach(ship -> {
            ships.add(ship);
            individualItems.add(ship);
        });
        return individualItems;
    }

    /**
     * Method to add ships/groups to the model.
     *
     * @param list : list of ships/groups to be added
     */
    public void add(ArrayList<Groupable> list) {
        ships.addAll(list);
        notifySubscribers();
    }

    /**
     * Method to remove ships/groups from the model.
     *
     * @param list : list of ships/groups to be removed
     */
    public void remove(ArrayList<Groupable> list) {
        ships.removeAll(list);
        notifySubscribers();
    }
}
package com.example.ships_application;

import java.util.ArrayList;

/**
 * Interaction Model for the MVC Architecture. Handles clipboard interactions and multi-selections.
 */
public class InteractionModel {
    /*
        Instance variables to store subscribers, reference to the selected ships, rubber-band rect used for selection
        and a reference to the clipboard.
     */
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Groupable> selectedShips;
    RubberBandRectangle rubberBandRectangle;
    ShipClipboard clipboard;

    /**
     * Default constructor for this class. Initializes the list of subscribers and selected ships, and initializes clipboard.
     */
    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShips = new ArrayList<>();
        clipboard = new ShipClipboard();
    }

    /**
     * Method to cut the current selected ships to the clipboard.
     *
     * @return : the list of current selected ships
     */
    public ArrayList<Groupable> cutToClipboard() {
        clipboard.add(selectedShips);
        return selectedShips;
    }

    /**
     * Method to copy the current selected ships to the clipboard.
     */
    public void copyToClipboard() {
        clipboard.add(selectedShips);
    }

    /**
     * Method to paste the ships from the clipboard and set those ships as current selection.
     *
     * @return : Ships from the clipboard
     */
    public ArrayList<Groupable> pasteFromClipboard() {
        ArrayList<Groupable> paste = clipboard.get();
        selectedShips = paste;
        return paste;
    }

    /**
     * Method to create an object to represent the rubber-band rectangle for selection.
     *
     * @param x : starting x coordinate for the rectangle
     * @param y : starting y coordinate for the rectangle
     */
    public void createRubberBand(double x, double y) {
        rubberBandRectangle = new RubberBandRectangle(x, y);
        notifySubscribers();
    }

    /**
     * Method to get rubber-band rectangle object.
     *
     * @return : rectangle to draw
     */
    public RubberBandRectangle getRubberBand() {
        return rubberBandRectangle;
    }

    /**
     * Method to resize the rubber-band rectangle for selection.
     *
     * @param prevX : starting x coordinate for the rectangle
     * @param prevY : starting y coordinate for the rectangle
     * @param x     : ending x coordinate for the rectangle
     * @param y     : ending y coordinate for the rectangle
     */
    public void resizeRubberBand(double prevX, double prevY, double x, double y) {
        rubberBandRectangle.resize(prevX, prevY, x, y);
        notifySubscribers();
    }

    /**
     * Method to clear the rubber-band rectangle used for selection.
     */
    public void clearRubberBand() {
        rubberBandRectangle = null;
        notifySubscribers();
    }

    /**
     * Method to clear selection in the iModel.
     */
    public void clearSelection() {
        selectedShips.clear();
        notifySubscribers();
    }

    /**
     * Method to set a new ship as one of the selected items if it is not already selected.
     *
     * @param newSelection : ship to be selected
     */
    public void addSelected(Groupable newSelection) {
        // remove the selected ship if it is already in selection
        if (selectedShips.contains(newSelection)) {
            selectedShips.remove(newSelection);
        } else {
            selectedShips.add(newSelection);
        }
        notifySubscribers();
    }

    /**
     * Method to set a new selection.
     *
     * @param newSelection : the only ship to be selected
     */
    public void newSelection(Groupable newSelection) {
        selectedShips.clear();
        selectedShips.add(newSelection);
        notifySubscribers();
    }

    /**
     * Method to set a new selection.
     *
     * @param newSelection : list of items to be selected
     */
    public void newSelection(ArrayList<Groupable> newSelection) {
        selectedShips.clear();
        selectedShips = newSelection;
        notifySubscribers();
    }

    /**
     * Method to get list of selected ships.
     *
     * @return : list of selected ships
     */
    public ArrayList<Groupable> getSelectedShips() {
        return selectedShips;
    }

    /**
     * Method to add a view as iModel's subscriber.
     *
     * @param aSub : new subscriber
     */
    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    /**
     * Method to notify subscribers of a change in iModel.
     */
    private void notifySubscribers() {
        subscribers.forEach(ShipModelSubscriber::modelChanged);
    }
}

package com.example.ships_application;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class to represent a clipboard to allow cut, copy and paste of ships in the application.
 */
public class ShipClipboard {
    // List of ships or groups that are copied or cut
    ArrayList<Groupable> clipboard;

    /**
     * Default constructor for this class. Initializes the clipboard ArrayList.
     */
    public ShipClipboard() {
        clipboard = new ArrayList<>();
    }

    /**
     * Method to add ships/groups to the clipboard.
     *
     * @param selectedShips : list of ships/groups to be added to the clipboard
     */
    public void add(ArrayList<Groupable> selectedShips) {
        // deep copy each ship/group in the list and add it to the clipboard
        clipboard = selectedShips.stream().map(Groupable::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Method to get ships/groups from the clipboard.
     *
     * @return : list of ships/group from the clipboard
     */
    public ArrayList<Groupable> get() {
        // return a deep copy of each ship/group from the clipboard
        return clipboard.stream().map(Groupable::duplicate).collect(Collectors.toCollection(ArrayList::new));
    }
}

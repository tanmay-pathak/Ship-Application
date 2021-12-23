package com.example.ships_application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for the MVC Architecture. Overloads mouse clicks and handles the interactions from view to model and imodel.
 */
public class ShipController {
    /*
        Instance variables to store iModel, Model and some mouse coordinates.
     */
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY, rubX, rubY;
    double dX, dY;

    /*
        States of the State-Machine used by the Controller to Over-load mouse clicks and keyboard presses.
     */
    protected enum State {
        READY, DRAGGING, RUBBER
    }

    /*
        Current state the machine is in.
     */
    protected State currentState;

    /**
     * Default constructor of this class.
     */
    public ShipController() {
        currentState = State.READY;
    }

    /**
     * Method to set the iModel for this controller.
     *
     * @param newModel : iModel to be saved
     */
    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    /**
     * Method to set the Model for this controller.
     *
     * @param newModel : Model to be saved
     */
    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    /**
     * Method to handle mouse presses.
     *
     * @param x     : x coordinate of the mouse click
     * @param y     : y coordinate of the mouse click
     * @param event : mouse event
     */
    public void handlePressed(double x, double y, MouseEvent event) {
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                Optional<Groupable> hit = model.detectHit(x, y);
                // context: clicked on a ship
                if (hit.isPresent()) {
                    if (event.isControlDown()) {
                        // context: control button is pressed
                        // side effect: add the ship to current selection if not already selected else remove it from selection
                        iModel.addSelected(hit.get());
                    } else if (iModel.getSelectedShips().contains(hit.get())) {
                        // context: control button is not pressed and ship clicked is selected already
                        // side effect: move to dragging state to move all selected ships together
                    } else {
                        // context: control button not pressed and shape clicked is not already selected
                        // side effect: clear selection and select the recently clicked ship
                        iModel.newSelection(hit.get());
                    }
                    currentState = State.DRAGGING;
                } else {
                    // context: clicked on background
                    if (event.isShiftDown()) {
                        // context: shift button is pressed
                        // side effect: create new ship and switch state to dragging
                        Ship newShip = model.createShip(x, y);
                        iModel.newSelection(newShip);
                        currentState = State.DRAGGING;
                    } else {
                        if (!(event.isControlDown())) {
                            // context: when control is not pressed
                            // side effect: allows rubber-band to deselect ships that are already selected when control is down
                            // and clear existing selection
                            iModel.clearSelection();
                        }
                        // context: shift button is not pressed
                        // side effect: clear selection and start drawing rubber band
                        // and switch state to Rubber Band drawing
                        iModel.createRubberBand(x, y);
                        rubX = x;
                        rubY = y;
                        currentState = State.RUBBER;
                    }
                }
            }
        }
    }

    /**
     * Method to handle mouse drags.
     *
     * @param x     : x coordinate of the mouse click
     * @param y     : y coordinate of the mouse click
     * @param event : mouse event
     */
    public void handleDragged(double x, double y, MouseEvent event) {
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {
            // Mouse move on Dragging state moves selected shapes
            case DRAGGING -> model.move(iModel.getSelectedShips(), dX, dY);
            // Mouse move on Rubber state resizes the rubber-band rectangle used for selection
            case RUBBER -> iModel.resizeRubberBand(rubX, rubY, x, y);
        }
    }

    /**
     * Method to handle mouse release.
     *
     * @param x     : x coordinate of the mouse click
     * @param y     : y coordinate of the mouse click
     * @param event : mouse event
     */
    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case DRAGGING -> {
                currentState = State.READY;
            }
            case RUBBER -> {
                // side effect: check all ships that are within the rectangle and select them in iModel
                // clear rectangle object and clear temporary model's selection
                currentState = State.READY;
                ArrayList<Groupable> rubberHitShips = model.detectRubberBandHit(iModel.getRubberBand());
                iModel.clearRubberBand();
                model.clearSelection();
                rubberHitShips.forEach(s -> iModel.addSelected(s));
            }
        }
    }

    /**
     * Method to handle key presses. Supports cut, copy, paste, grouping and ungrouping of ships.
     *
     * @param keyEvent key event
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        switch (currentState) {
            case READY -> {
                if (keyEvent.isControlDown()) {
                    if (keyEvent.getCode() == KeyCode.C) {
                        // event: c pressed for copy
                        // side effect: currently selected items are copied to the clipboard
                        iModel.copyToClipboard();
                    } else if (keyEvent.getCode() == KeyCode.X) {
                        // event: x pressed for cut
                        // side effect: currently selected items are removed from the selection and added to the clipboard
                        // and items are removed from the model
                        model.remove(iModel.cutToClipboard());
                    } else if (keyEvent.getCode() == KeyCode.V) {
                        // event: v pressed for paste
                        // side effect: items from the clipboard are made the new selection and are added to the model
                        model.add(iModel.pasteFromClipboard());
                    }
                } else {
                    if (keyEvent.getCode() == KeyCode.G) {
                        // event: G key is pressed
                        // side effect: currently selected items converted to a group
                        // and individual items removed and replaced with the group in model.
                        // also, new group is now selected in the iModel.
                        iModel.newSelection(model.createGroup(iModel.getSelectedShips()));
                    } else if (keyEvent.getCode() == KeyCode.U) {
                        // event: U key is pressed
                        ArrayList<Groupable> selectedShips = iModel.getSelectedShips();
                        if (selectedShips.size() == 1 && selectedShips.get(0).hasChildren()) {
                            // context: there is only item selected which is a group
                            // side effect: ungroup the item & remove group from model
                            // and items from the group individually added to model and selected in iModel
                            Groupable group = selectedShips.get(0);
                            iModel.newSelection(model.ungroup(group));
                        }
                    }
                }
            }
        }
    }
}

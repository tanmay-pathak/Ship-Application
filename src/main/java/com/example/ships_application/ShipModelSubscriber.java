package com.example.ships_application;

/**
 * Interface for all subscribers to get notified that the model has changed.
 */
public interface ShipModelSubscriber {
    /**
     * Method to be run when model has changed.
     */
    void modelChanged();
}

package com.example.ships_application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * View class for the MVC Architecture. Draws a canvas of 1000x700 size where ships are drawn. Can be subscribed for
 * model changes.
 */
public class ShipView extends StackPane implements ShipModelSubscriber {
    /*
        Instance variables to store canvas, graphic context and models from the MVC architecture.
     */
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;
    // flag to draw all children of a selected group with yellow fill
    boolean groupSelected;

    /**
     * Default constructor for this class. Sets up canvas with a black background.
     */
    public ShipView() {
        myCanvas = new Canvas(1000, 700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: black");
    }

    /**
     * Method to store reference to the model.
     *
     * @param newModel : model of this view
     */
    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    /**
     * Method to store reference to the iModel.
     *
     * @param newIModel : iModel of this view
     */
    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    /**
     * Method to set up event handlers for the view via the controller.
     *
     * @param controller : controller to trigger events (does not store the reference)
     */
    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(), e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(), e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(), e.getY(), e));
    }

    /**
     * Method to draw ships on the canvas.
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        // draw rubber band rectangle for selection
        RubberBandRectangle rb = iModel.getRubberBand();
        if (rb != null) {
            gc.setFill(Color.rgb(184, 134, 37));
            gc.strokeRect(rb.left, rb.top, rb.width, rb.height);
            gc.fillRect(rb.left, rb.top, rb.width, rb.height);
        }
        // draw each ship/group in model
        model.getShips().forEach(item -> {
            groupSelected = false;
            // draw individual ship
            if (!item.hasChildren()) {
                drawShip((Ship) item);
            } else {
                // draw group
                drawGroup(item);
            }
        });
    }

    /**
     * Helper method to draw an individual ship.
     *
     * @param ship : ship to be drawn.
     */
    private void drawShip(Ship ship) {
        if (iModel.getSelectedShips().contains(ship) || groupSelected) {
            gc.setFill(Color.YELLOW);
            gc.setStroke(Color.CORAL);
        } else {
            gc.setStroke(Color.YELLOW);
            gc.setFill(Color.CORAL);
        }
        gc.fillPolygon(ship.getDisplayXs(), ship.getDisplayYs(), ship.displayLength());
        gc.strokePolygon(ship.getDisplayXs(), ship.getDisplayYs(), ship.displayLength());
    }

    /**
     * Helper method to draw a group.
     *
     * @param group : group to be drawn
     */
    private void drawGroup(Groupable group) {
        // draw bounding box for the group if it is selected
        if (iModel.getSelectedShips().contains(group)) {
            groupSelected = true;
            gc.setStroke(Color.WHITE);
            gc.strokeRect(group.getLeft(), group.getTop(), Math.abs(group.getLeft() - group.getRight()), Math.abs(group.getTop() - group.getBottom()));
        }
        // draw each ship of the group recursively
        group.getChildren().forEach(s -> {
            if (!s.hasChildren()) {
                drawShip((Ship) s);
            } else {
                drawGroup(s);
            }
        });
    }

    /**
     * Method runs when model or iModel has a change.
     */
    @Override
    public void modelChanged() {
        draw();
    }
}

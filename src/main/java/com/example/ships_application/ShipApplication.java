package com.example.ships_application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Application class which starts up a JavaFX stage with a canvas to create, move and select Ships or group of Ships.
 */
public class ShipApplication extends Application {
    /**
     * Java method that runs first and starts up 'start' function to start the JavaFX application.
     *
     * @param args : NA
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Method that starts the JavaFX application.
     *
     * @param stage : Default stage.
     */
    @Override
    public void start(Stage stage) {
        /*
            Create MVC components required for this application.
         */
        ShipView view = new ShipView();
        ShipController controller = new ShipController();
        ShipModel model = new ShipModel();
        InteractionModel iModel = new InteractionModel();

        /*
            Connect MVC components to each other. View uses publish-subscribe communication model.
         */
        view.setModel(model);
        view.setController(controller);
        view.setInteractionModel(iModel);
        controller.setModel(model);
        controller.setInteractionModel(iModel);
        model.addSubscriber(view);
        iModel.addSubscriber(view);

        /*
            Setup stage and show scene.
         */
        StackPane root = new StackPane(view);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(controller::handleKeyPressed);
    }
}
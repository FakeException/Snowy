package me.darkboy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import me.darkboy.controllers.MainController;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Snowy extends Application {
    private double xOffset;
    private double yOffset;

    @Getter
    private static ScheduledExecutorService executorService;

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ResourcesLoader.loadURL("Launcher.fxml"));
        fxmlLoader.setControllerFactory(controller -> new MainController(primaryStage));

        StackPane snowyPane = fxmlLoader.load();

        snowyPane.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        snowyPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });

        primaryStage.setTitle("Snowy");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(snowyPane);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        executorService = Executors.newScheduledThreadPool(20);
        launch(args);
    }
}

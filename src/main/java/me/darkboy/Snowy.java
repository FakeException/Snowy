package me.darkboy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import me.darkboy.controllers.ChatController;
import me.darkboy.controllers.MainController;
import me.darkboy.utils.UserService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Snowy extends Application {
    private static double xOffset;
    private static double yOffset;

    public static String session;

    @Getter
    private static ScheduledExecutorService executorService;

    private static Stage primaryStage;

    private static UserService service;

    @Override
    public void start(Stage primaryStage) throws IOException {

        service = new UserService();

        Snowy.primaryStage = primaryStage;

        FXMLLoader fxmlLoader;
        StackPane snowyPane;

        if (service.isSessionAlive()) {

            session = service.getSession();

            fxmlLoader = new FXMLLoader(ResourcesLoader.loadURL("Chat.fxml"));
            fxmlLoader.setControllerFactory(controller -> new ChatController(primaryStage));

        } else {
            fxmlLoader = new FXMLLoader(ResourcesLoader.loadURL("Launcher.fxml"));
            fxmlLoader.setControllerFactory(controller -> new MainController(primaryStage));
        }


        snowyPane = fxmlLoader.load();

        primaryStage.setTitle("Snowy");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(snowyPane);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

        snowyPane.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        snowyPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }

    public static void main(String[] args) {
        executorService = Executors.newScheduledThreadPool(20);
        launch(args);
    }

    public static String getSession() {
        return session;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static double getxOffset() {
        return xOffset;
    }

    public static double getyOffset() {
        return yOffset;
    }

    public static void setxOffset(double xOffset) {
        Snowy.xOffset = xOffset;
    }

    public static void setyOffset(double yOffset) {
        Snowy.yOffset = yOffset;
    }

    public static UserService getService() {
        return service;
    }

    public static void loadMainUI() {
        Platform.runLater(() -> {
            Stage appStage = new Stage();

            appStage.setTitle("Snowy");
            appStage.initStyle(StageStyle.TRANSPARENT);

            FXMLLoader fxmlLoader = new FXMLLoader(ResourcesLoader.loadURL("Chat.fxml"));
            fxmlLoader.setControllerFactory(controller -> new ChatController(appStage));

            StackPane snowyPane;
            try {
                snowyPane = fxmlLoader.load();

                snowyPane.setOnMousePressed(e -> {
                    Snowy.setxOffset(appStage.getX() - e.getScreenX());
                    Snowy.setyOffset(appStage.getY() - e.getScreenY());
                });

                snowyPane.setOnMouseDragged(e -> {
                    appStage.setX(e.getScreenX() + Snowy.getxOffset());
                    appStage.setY(e.getScreenY() + Snowy.getyOffset());
                });

                Snowy.getPrimaryStage().hide();

                Scene scene = new Scene(snowyPane);
                scene.setFill(Color.TRANSPARENT);

                appStage.setScene(scene);
                appStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

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
import me.darkboy.mysql.MySQL;
import me.darkboy.mysql.queries.CreateTableQuery;
import me.darkboy.mysql.queries.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Snowy extends Application {
    private double xOffset;
    private double yOffset;

    @Getter
    private static ScheduledExecutorService executorService;

    @Getter
    public static MySQL mySQLConnection;

    @Override
    public void start(Stage primaryStage) throws IOException {

        connectToMySQL();

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

    public static void connectToMySQL() {
        mySQLConnection = new MySQL();
//        mySQLConnection.connect("", "", "", "", "");
//        setupUserTable();
    }

    public static void setupUserTable() {
        String sql = (new CreateTableQuery("accounts")).ifNotExists()
                .column("id", "INT(11) AUTO_INCREMENT")
                .column("username", "VARCHAR(16) NOT NULL")
                .column("email", "VARCHAR(50) NOT NULL")
                .column("password", "VARCHAR(100) NOT NULL")
                .column("salt", "VARCHAR(5000) NOT NULL")
                .column("ip", "VARCHAR(100) NOT NULL")
                .primaryKey("id").build();

        try {
            Query query = new Query(mySQLConnection, sql);
            query.executeUpdateAsync();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package me.darkboy.controllers;

import io.github.palexdev.materialfx.beans.MFXLoaderBean;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.AnimationUtils;
import io.github.palexdev.materialfx.utils.NodeUtils;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.darkboy.ResourcesLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private final Stage primaryStage;

    private MFXButton opNavButton;
    private ParallelTransition openNav;
    private ParallelTransition closeNav;
    private boolean isNavShown = false;

    @FXML
    private StackPane mainPane;

    @FXML
    private HBox windowButtons;

    @FXML
    private StackPane navBar;

    @FXML
    private MFXScrollPane scrollPane;

    @FXML
    private MFXVLoader vLoader;

    @FXML
    private StackPane contentPane;

    @FXML
    private VBox logoPane;

    @FXML
    private ImageView logo;

    public ChatController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Icons
        MFXFontIcon xIcon = new MFXFontIcon("mfx-x-circle", 16);
        MFXFontIcon minusIcon = new MFXFontIcon("mfx-minus-circle", 16);
        MFXFontIcon expandIcon = new MFXFontIcon("mfx-expand", 12.5);
        MFXFontIcon angleIcon = new MFXFontIcon("mfx-angle-right", 20);

        // Buttons
        MFXIconWrapper closeButton = new MFXIconWrapper(xIcon, 22);
        closeButton.setId("closeButton");
        closeButton.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> primaryStage.close());

        MFXIconWrapper minimizeButton = new MFXIconWrapper(minusIcon, 22);
        minimizeButton.setId("minimizeButton");
        minimizeButton.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> primaryStage.setIconified(true));

        MFXIconWrapper expandButton = new MFXIconWrapper(expandIcon, 22);
        expandButton.setId("expandButton");
        expandButton.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> primaryStage.setMaximized(!primaryStage.isMaximized()));

        opNavButton = new MFXButton("");
        opNavButton.setOpacity(0.0);
        opNavButton.setDisable(true);
        opNavButton.setId("navButton");
        opNavButton.setPrefSize(25, 25);
        opNavButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        opNavButton.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> animate());

        // Graphics
        opNavButton.setGraphic(angleIcon);

        // Layout and Utils
        StackPane.setAlignment(opNavButton, Pos.CENTER_LEFT);
        StackPane.setMargin(opNavButton, new Insets(0, 0, 0, 4));

        NodeUtils.makeRegionCircular(closeButton);
        NodeUtils.makeRegionCircular(minimizeButton);
        NodeUtils.makeRegionCircular(expandButton);
        NodeUtils.makeRegionCircular(opNavButton);

        // Add all
        windowButtons.getChildren().addAll(expandButton, minimizeButton, closeButton);
        mainPane.getChildren().addAll(opNavButton);

        // VLoader
        vLoader.setContentPane(contentPane);
        vLoader.addItem("Search", MFXLoaderBean.Builder.build(new MFXRectangleToggleNode("Search"), ResourcesLoader.loadURL("SearchChat.fxml")).setControllerFactory(controller -> new SearchChatController(mainPane)).setDefaultRoot(true));
        vLoader.addItem("Users", MFXLoaderBean.Builder.build(new MFXRectangleToggleNode("Users"), ResourcesLoader.loadURL("Users.fxml")).setControllerFactory(controller -> new UsersController(mainPane)));
        vLoader.start();

        // Others
        ScrollUtils.addSmoothScrolling(scrollPane, 2);
        ScrollUtils.animateScrollBars(scrollPane, 500, 500);

        primaryStage.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Scene scene = primaryStage.getScene();
                scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.F11) {
                        primaryStage.setFullScreen(!primaryStage.isFullScreen());
                    }
                });
                scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    if (isNavShown) {
                        animate();
                    }
                });
            }
        });
        navBar.setVisible(false);
        initAnimations();

        mainPane.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> mainPane.requestFocus());

        primaryStage.setOnShown(event -> presentation());
    }

    private void presentation() {
        AnimationUtils.SequentialBuilder.build()
                .add(AnimationUtils.TimelineBuilder.build().show(450, logo).setDelay(50).getAnimation())
                .setOnFinished(event -> AnimationUtils.SequentialBuilder.build()
                        .add(AnimationUtils.TimelineBuilder.build().hide(300, logoPane).setOnFinished(end -> logoPane.setVisible(false)).getAnimation())
                        .add(AnimationUtils.ParallelBuilder.build().show(800, contentPane, opNavButton, mainPane).setOnFinished(end -> {
                            opNavButton.setDisable(false);
                            animate();
                        }).getAnimation())
                        .setDelay(750)
                        .getAnimation().play())
                .setDelay(750)
                .getAnimation().play();

    }

    private void initAnimations() {
        openNav = (ParallelTransition) AnimationUtils.ParallelBuilder.build()
                .show(400, navBar)
                .add(new KeyFrame(Duration.millis(300), new KeyValue(navBar.translateXProperty(), 5)))
                .add(new KeyFrame(Duration.millis(200), new KeyValue(opNavButton.rotateProperty(), -180)))
                .setOnFinished(event -> isNavShown = true)
                .getAnimation();

        closeNav = (ParallelTransition) AnimationUtils.ParallelBuilder.build()
                .hide(50, navBar)
                .add(new KeyFrame(Duration.millis(300), new KeyValue(navBar.translateXProperty(), -240)))
                .add(new KeyFrame(Duration.millis(200), new KeyValue(opNavButton.rotateProperty(), 0)))
                .setOnFinished(event -> isNavShown = false)
                .getAnimation();
    }

    private void animate() {
        if (!isNavShown) {
            navBar.setVisible(true);
            openNav.play();
        } else {
            closeNav.play();
        }
    }
}

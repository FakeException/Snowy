package me.darkboy.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.base.AbstractMFXDialog;
import io.github.palexdev.materialfx.controls.enums.DialogType;
import io.github.palexdev.materialfx.controls.factories.MFXAnimationFactory;
import io.github.palexdev.materialfx.controls.factories.MFXDialogFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import me.darkboy.utils.UserDetails;
import me.darkboy.utils.UserService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final Pane pane;

    private final AbstractMFXDialog errorDialog;
    private final AbstractMFXDialog infoDialog;

    @FXML
    public MFXTextField emailField;

    @FXML
    public MFXPasswordField passwordField;

    @FXML
    public MFXButton doneButton;

    public LoginController(Pane pane) {
        this.pane = pane;

        errorDialog = MFXDialogFactory.buildDialog(DialogType.ERROR, "Please check your details", "");
        errorDialog.setAnimateIn(true);
        errorDialog.setAnimateOut(true);

        infoDialog = MFXDialogFactory.buildDialog(DialogType.INFO, "Logged In", "Successfully logged in!");
        infoDialog.setAnimateIn(true);
        infoDialog.setAnimateOut(true);

        Platform.runLater(() -> this.pane.getChildren().addAll(errorDialog, infoDialog));
    }

    private int errors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorDialog.setVisible(false);
        infoDialog.setVisible(false);

        doneButton.setOnAction(event -> {

            errorDialog.setContent("");
            errors = 0;

            if (passwordField.getPassword().length() > 32) {
                addContent(errorDialog, "The password should not be longer than 32 characters");
                errors++;
            } else if (passwordField.getPassword().isEmpty()) {
                addContent(errorDialog, "Please input a password");
                errors++;
            }

            if (!isValidEmailAddress(emailField.getText())) {
                addContent(errorDialog, "The given email is not valid or is empty");
                errors++;
            }

            if (errors > 0) {
                errorDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                errorDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                errorDialog.show();
            } else {
                // Login

                UserDetails details = new UserDetails();
                UserService service = new UserService();

                details.setEmail(emailField.getText());

                details.setPassword(passwordField.getPassword());

                new Thread(() -> {

                    try {

                        String body = Objects.requireNonNull(service.login(details).body()).string();

                        if (body.contains("Successfully")) {
                            infoDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                            infoDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                            infoDialog.show();
                        } else if (body.contains("Wrong")) {

                            Platform.runLater(() -> {
                                addContent(errorDialog, "Wrong password or email!");
                                errors++;
                                errorDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                                errorDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                                errorDialog.show();
                            });
                        } else {
                            Platform.runLater(() -> {
                                addContent(errorDialog, "This account doesn't exist!");
                                errors++;
                                errorDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                                errorDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                                errorDialog.show();
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();

            }
        });
    }

    private void addContent(AbstractMFXDialog dialog, String content) {
        if (dialog.getContent().equals("")) {
            dialog.setContent(content);
        } else {
            dialog.setContent(dialog.getContent() + "\n" + content);
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}

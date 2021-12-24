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
import me.darkboy.Snowy;
import me.darkboy.utils.UserDetails;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.net.URL;
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
            } else if (passwordField.getPassword().length() < 8) {
                addContent(errorDialog, "The password should be at least 8 characters");
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

                details.setEmail(emailField.getText());

                details.setPassword(passwordField.getPassword());

                new Thread(() -> {

                    try {

                        String body = Snowy.getService().login(details);

                        if (body.contains("Wrong")) {

                            Platform.runLater(() -> {
                                addContent(errorDialog, "Wrong password or email!");
                                errors++;
                                errorDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                                errorDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                                errorDialog.show();
                            });
                        } else if (body.contains("exist")) {
                            Platform.runLater(() -> {
                                addContent(errorDialog, "This account doesn't exist!");
                                errors++;
                                errorDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
                                errorDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
                                errorDialog.show();
                            });
                        } else {
//                            infoDialog.setInAnimationType(MFXAnimationFactory.SLIDE_IN_TOP);
//                            infoDialog.setOutAnimationType(MFXAnimationFactory.SLIDE_OUT_BOTTOM);
//                            infoDialog.show();

                            Snowy.loadMainUI();


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

    private boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}

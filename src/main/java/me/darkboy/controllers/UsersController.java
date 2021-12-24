package me.darkboy.controllers;

import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import me.darkboy.Snowy;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    private final Pane pane;

    public MFXListView<String> users;

    public UsersController(Pane pane) {
        this.pane = pane;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            List<String> usersList = Snowy.getService().fetchUsers();

            Collections.sort(usersList);
            Collections.reverse(usersList);

            for (String s : usersList) {
                users.getItems().add(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

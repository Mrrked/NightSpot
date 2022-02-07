package app.myapp.controller;

import app.myapp.controller.component.element.MySpotsLabel;
import app.myapp.controller.component.element.Spot_JoinedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.StackedFontIcon;

import java.io.IOException;

public class Main_Controller {

    @FXML
    private StackedFontIcon btnAdd;
    @FXML
    private StackedFontIcon btnSearch;
    @FXML
    private MenuButton menuUser;
    @FXML
    private TextField txtSearch;

    @FXML
    private HBox contentPane;
    @FXML
    private HBox spotPane;

    //OBJECTS
    Spot_JoinedList spotJoinedList = new Spot_JoinedList();
    MySpotsLabel mySpotsLabel = new MySpotsLabel("Hello World");

    public Main_Controller() throws IOException {

    }

    @FXML
    void initialize() throws IOException {
        spotPane.getChildren().add((Node) spotJoinedList);
        for (int i = 0; i < 5; i++) {
            spotJoinedList.paneList.getChildren().add(new MySpotsLabel("Spot #" + i ));
        }
    }



}



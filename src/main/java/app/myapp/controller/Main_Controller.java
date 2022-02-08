package app.myapp.controller;

import app.myapp.controller.component.Content_ListofPosts;
import app.myapp.controller.component.Content_ListofSpots;
import app.myapp.controller.component.Content_MainPost;
import app.myapp.controller.component.element.*;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
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
    MyRulesLabel myRulesLabel = new MyRulesLabel("1. English Only");
    Spot_Info spotInfo = new Spot_Info();
    Content_ListofSpots contentListofSpots = new Content_ListofSpots();
    Content_ListofPosts contentListofPosts = new Content_ListofPosts();
    Content_MainPost contentMainPost = new Content_MainPost();
    PageContainer pageContainer = new PageContainer();
    CardSpot cardSpot = new CardSpot();
    CardPost cardPost = new CardPost();
    CardComment cardComment = new CardComment();
    CardReply cardReply = new CardReply();

    //TODO
    //Database

    public Main_Controller() throws IOException {

    }

    @FXML
    void initialize() throws IOException {
        spotPane.getChildren().add(spotJoinedList);
        for (int i = 0; i < 5; i++) {
            spotJoinedList.paneList.getChildren().add(new MySpotsLabel("Spot #" + i ));
            pageContainer.getChildren().add(new CardSpot());
        }
//
        contentPane.getChildren().add(contentListofSpots);
        contentListofSpots.cPageSpots.setMaxPageIndicatorCount(5);
        contentListofSpots.cPageSpots.setPageCount(10);
        contentListofSpots.cPageSpots.setPageFactory((pageIndex) -> {
            return pageContainer;
        });

//        spotPane.getChildren().add(spotInfo);
//        for (int i = 0; i < 10; i++) {
//            spotInfo.spotRulesPane.getChildren().add(new MySpotsLabel("Rule #" + i ));
//        }
//        contentPane.getChildren().add(contentListofPosts);

//        contentPane.getChildren().add(contentMainPost);


    }



}


